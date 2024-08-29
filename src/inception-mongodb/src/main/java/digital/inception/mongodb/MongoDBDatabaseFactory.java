/*
 * Copyright Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package digital.inception.mongodb;

import com.mongodb.ClientSessionOptions;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.WriteConcern;
import com.mongodb.client.ClientSession;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.util.ArrayList;
import java.util.List;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.bson.UuidRepresentation;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.mongo.MongoClientFactory;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.boot.autoconfigure.mongo.MongoProperties.Ssl;
import org.springframework.boot.autoconfigure.mongo.StandardMongoClientSettingsBuilderCustomizer;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.SessionAwareMethodInterceptor;
import org.springframework.data.mongodb.core.MongoExceptionTranslator;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * The <b>MongoDBDatabaseFactory-</b> class.
 *
 * @author Marcus Portmann
 */
@Slf4j
@SuppressWarnings({"unused", "NullableProblems"})
public class MongoDBDatabaseFactory implements MongoDatabaseFactory, DisposableBean {

  private final String databaseName;

  private final PersistenceExceptionTranslator exceptionTranslator;

  private final MongoClient mongoClient;

  /** The writeConcern controlling the acknowledgment of write operations. */
  @Setter private WriteConcern writeConcern;

  /**
   * Constructs a new <b>MongoDBDatabaseFactory</b>.
   * @param applicationContext the Spring application context
   * @param uri the MongoDB URI
   * @param databaseName the MongoDB database name
   */
  public MongoDBDatabaseFactory(
      ApplicationContext applicationContext, String uri, String databaseName) {
    if (!StringUtils.hasText(uri)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified to initialize the MongoDB database factory");
    }

    if (!StringUtils.hasText(databaseName)) {
      throw new BeanInitializationException(
          "A MongoDB database name must be specified to initialize the MongoDB database factory");
    }

    this.databaseName = databaseName;
    this.exceptionTranslator = new MongoExceptionTranslator();
    this.mongoClient = createMongoClient(applicationContext, uri);
  }

  /**
   * Constructs a new <b>MongoDBDatabaseFactory</b>.
   * @param applicationContext the Spring application context
   * @param databaseName the MongoDB database name
   */
  public MongoDBDatabaseFactory(ApplicationContext applicationContext, String databaseName) {
    Environment environment = applicationContext.getEnvironment();

    // Ensure that we have a MongoDB URI specified via spring.data.mongodb.uri
    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");

    if (!StringUtils.hasText(mongodbUri)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB database factory");
    }

    /*
     * We chose to explicitly initialize the database factory with a database name, so we must
     * provide one.
     */
    if (!StringUtils.hasText(databaseName)) {
      throw new BeanInitializationException(
          "A MongoDB database name must be specified to initialize the MongoDB database factory");
    }

    this.databaseName = databaseName;
    this.exceptionTranslator = new MongoExceptionTranslator();
    this.mongoClient = createMongoClient(applicationContext, mongodbUri);
  }

  /**
   * Constructs a new <b>MongoDBDatabaseFactory</b>.
   * @param applicationContext the Spring application context
   */
  public MongoDBDatabaseFactory(ApplicationContext applicationContext) {
    Environment environment = applicationContext.getEnvironment();

    // Ensure that we have a MongoDB URI specified via spring.data.mongodb.uri
    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");

    if (!StringUtils.hasText(mongodbUri)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB database factory");
    }

    String mongodbDatabaseName = environment.getProperty("spring.data.mongodb.database");

    /*
     * If we did not explicity specify a database name via spring.data.mongodb.database, look for
     * one in the URI.
     */
    if (!StringUtils.hasText(mongodbDatabaseName)) {
      ConnectionString connectionString = new ConnectionString(mongodbUri);
      mongodbDatabaseName = connectionString.getDatabase();
    }

    if (!StringUtils.hasText(mongodbDatabaseName)) {
      throw new BeanInitializationException(
          "A MongoDB database name specified via the MongoDB URI (spring.data.mongodb.uri) or explicitly (spring.data.mongodb.database) is required to initialize the default MongoDB database factory");
    }

    this.databaseName = mongodbDatabaseName;
    this.exceptionTranslator = new MongoExceptionTranslator();
    this.mongoClient = createMongoClient(applicationContext, mongodbUri);
  }

  @Override
  public void destroy() throws Exception {
    try {
      this.mongoClient.close();
    } catch (Throwable e) {
      log.error("Failed to close the mongo client", e);
    }
  }

  @Override
  public PersistenceExceptionTranslator getExceptionTranslator() {
    return exceptionTranslator;
  }

  @Override
  public MongoDatabase getMongoDatabase(String databaseName) throws DataAccessException {
    MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);

    if (writeConcern == null) {
      return mongoDatabase;
    } else {
      return mongoDatabase.withWriteConcern(writeConcern);
    }
  }

  @Override
  public MongoDatabase getMongoDatabase() throws DataAccessException {
    return getMongoDatabase(databaseName);
  }

  @Override
  public ClientSession getSession(ClientSessionOptions options) {
    return mongoClient.startSession(options);
  }

  @Override
  public MongoDatabaseFactory withSession(ClientSession session) {
    return new ClientSessionBoundMongoDbFactory(session, this);
  }

  private MongoClient createMongoClient(ApplicationContext applicationContext, String mongodbUri) {
    Environment environment = applicationContext.getEnvironment();

    // Check whether an alternative UUID representation was specified via the
    // spring.data.mongodb.uuid-representation configuration value
    UuidRepresentation uuidRepresentation;

    try {
      uuidRepresentation =
          environment.getProperty(
              "spring.data.mongodb.uuid-representation",
              UuidRepresentation.class,
              UuidRepresentation.JAVA_LEGACY);
    } catch (Throwable e) {
      uuidRepresentation = UuidRepresentation.JAVA_LEGACY;
    }

    ConnectionString connectionString = new ConnectionString(mongodbUri);

    SslBundles sslBundles;
    try {
      sslBundles = applicationContext.getBean(SslBundles.class);
    } catch (Throwable e) {
      sslBundles = null;
    }

    StandardMongoClientSettingsBuilderCustomizer standardMongoClientSettingsBuilderCustomizer =
        new StandardMongoClientSettingsBuilderCustomizer(
            connectionString, uuidRepresentation, new Ssl(), sslBundles);

    List<MongoClientSettingsBuilderCustomizer> mongoClientSettingsBuilderCustomizers =
        new ArrayList<>();
    mongoClientSettingsBuilderCustomizers.add(standardMongoClientSettingsBuilderCustomizer);

    MongoClientSettings mongoClientSettings = MongoClientSettings.builder().build();

    return new MongoClientFactory(mongoClientSettingsBuilderCustomizers)
        .createMongoClient(mongoClientSettings);
  }

  /**
   * {@link ClientSession} bound {@link MongoDatabaseFactory} decorating the database with a {@link
   * SessionAwareMethodInterceptor}.
   */
  static final class ClientSessionBoundMongoDbFactory implements MongoDatabaseFactory {

    private final MongoDatabaseFactory delegate;

    private final ClientSession session;

    public ClientSessionBoundMongoDbFactory(ClientSession session, MongoDatabaseFactory delegate) {
      this.session = session;
      this.delegate = delegate;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      ClientSessionBoundMongoDbFactory that = (ClientSessionBoundMongoDbFactory) o;

      if (!ObjectUtils.nullSafeEquals(this.session, that.session)) {
        return false;
      }
      return ObjectUtils.nullSafeEquals(this.delegate, that.delegate);
    }

    public MongoDatabaseFactory getDelegate() {
      return this.delegate;
    }

    @Override
    public PersistenceExceptionTranslator getExceptionTranslator() {
      return delegate.getExceptionTranslator();
    }

    @Override
    public MongoDatabase getMongoDatabase() throws DataAccessException {
      return proxyMongoDatabase(delegate.getMongoDatabase());
    }

    @Override
    public MongoDatabase getMongoDatabase(String dbName) throws DataAccessException {
      return proxyMongoDatabase(delegate.getMongoDatabase(dbName));
    }

    @Override
    public ClientSession getSession(ClientSessionOptions options) {
      return delegate.getSession(options);
    }

    public ClientSession getSession() {
      return this.session;
    }

    @Override
    public int hashCode() {
      int result = ObjectUtils.nullSafeHashCode(this.session);
      result = 31 * result + ObjectUtils.nullSafeHashCode(this.delegate);
      return result;
    }

    @Override
    public boolean isTransactionActive() {
      return session != null && session.hasActiveTransaction();
    }

    public String toString() {
      return "MongoDatabaseFactorySupport.ClientSessionBoundMongoDbFactory(session="
          + this.getSession()
          + ", delegate="
          + this.getDelegate()
          + ")";
    }

    @Override
    public MongoDatabaseFactory withSession(ClientSession session) {
      return delegate.withSession(session);
    }

    private <T> T createProxyInstance(
        com.mongodb.session.ClientSession session, T target, Class<T> targetType) {

      ProxyFactory factory = new ProxyFactory();
      factory.setTarget(target);
      factory.setInterfaces(targetType);
      factory.setOpaque(true);

      factory.addAdvice(
          new SessionAwareMethodInterceptor<>(
              session,
              target,
              ClientSession.class,
              MongoDatabase.class,
              this::proxyDatabase,
              MongoCollection.class,
              this::proxyCollection));

      return targetType.cast(factory.getProxy(target.getClass().getClassLoader()));
    }

    private MongoCollection<?> proxyCollection(
        com.mongodb.session.ClientSession session, MongoCollection<?> collection) {
      return createProxyInstance(session, collection, MongoCollection.class);
    }

    private MongoDatabase proxyDatabase(
        com.mongodb.session.ClientSession session, MongoDatabase database) {
      return createProxyInstance(session, database, MongoDatabase.class);
    }

    private MongoDatabase proxyMongoDatabase(MongoDatabase database) {
      return createProxyInstance(session, database, MongoDatabase.class);
    }
  }
}
