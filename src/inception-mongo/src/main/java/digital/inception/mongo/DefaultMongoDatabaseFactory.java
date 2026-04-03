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

package digital.inception.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.WriteConcern;
import com.mongodb.client.MongoClient;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.util.StringUtils;

/**
 * The {@code DefaultMongoDatabaseFactory} class provides a MongoDB database factory implementation
 * based on Spring Data MongoDB's {@link SimpleMongoClientDatabaseFactory}.
 *
 * <p>This implementation preserves the original Inception Framework behaviour for resolving the
 * default MongoDB database name while delegating MongoDB client lifecycle management to Spring Data
 * MongoDB.
 *
 * <p>When initialized from a Spring {@link ApplicationContext}, the MongoDB database name is
 * resolved using the following rules:
 *
 * <ol>
 *   <li>Use the value of {@code spring.data.mongodb.database} when explicitly configured.
 *   <li>If no explicit database name has been configured, fall back to the database name embedded
 *       in {@code spring.data.mongodb.uri}.
 *   <li>If neither source provides a database name, fail initialization with a {@link
 *       BeanInitializationException}.
 * </ol>
 *
 * <p>When initialized directly from a MongoDB URI or {@link ConnectionString}, the URI must contain
 * a database name.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class DefaultMongoDatabaseFactory extends SimpleMongoClientDatabaseFactory {

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * @param mongoClient the MongoDB client
   * @param databaseName the MongoDB database name
   */
  public DefaultMongoDatabaseFactory(MongoClient mongoClient, String databaseName) {
    super(mongoClient, requireDatabaseName(databaseName));
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * @param mongoClient the MongoDB client
   * @param databaseName the MongoDB database name
   * @param writeConcern the write concern controlling the acknowledgment of write operations
   */
  public DefaultMongoDatabaseFactory(
      MongoClient mongoClient, String databaseName, WriteConcern writeConcern) {
    super(mongoClient, requireDatabaseName(databaseName));
    setWriteConcern(writeConcern);
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * <p>The connection string must contain a database name.
   *
   * @param connectionString the MongoDB connection string
   */
  public DefaultMongoDatabaseFactory(String connectionString) {
    super(requireConnectionStringWithDatabase(connectionString).toString());
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * <p>The connection string must contain a database name.
   *
   * @param connectionString the MongoDB connection string
   */
  public DefaultMongoDatabaseFactory(ConnectionString connectionString) {
    super(requireConnectionStringWithDatabase(connectionString));
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * <p>The MongoDB database name is resolved explicitly from the {@code databaseName} parameter.
   * The MongoDB URI is resolved from {@code spring.data.mongodb.uri}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param databaseName the MongoDB database name
   */
  public DefaultMongoDatabaseFactory(ApplicationContext applicationContext, String databaseName) {
    this(
        buildConnectionString(
            applicationContext.getEnvironment(), requireDatabaseName(databaseName)));
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory}.
   *
   * <p>The MongoDB database name is resolved from {@code spring.data.mongodb.database} or, if not
   * configured, from the database name embedded in {@code spring.data.mongodb.uri}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   */
  public DefaultMongoDatabaseFactory(ApplicationContext applicationContext) {
    this(resolveConnectionString(applicationContext.getEnvironment()));
  }

  /**
   * Constructs a new {@code DefaultMongoDatabaseFactory} from a MongoDB URI.
   *
   * <p>The MongoDB URI must contain a database name.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param uri the MongoDB URI
   * @return the {@code DefaultMongoDatabaseFactory} instance for the MongoDB URI
   */
  public static DefaultMongoDatabaseFactory fromUri(
      ApplicationContext applicationContext, String uri) {
    return new DefaultMongoDatabaseFactory(requireConnectionStringWithDatabase(uri));
  }

  /**
   * Resolve the MongoDB connection string from the Spring {@link Environment}.
   *
   * <p>The MongoDB database name is resolved from {@code spring.data.mongodb.database}. If no
   * explicit database name has been configured, the MongoDB URI configured via {@code
   * spring.data.mongodb.uri} is inspected for the database name.
   *
   * @param environment the Spring {@link Environment}
   * @return the resolved MongoDB connection string
   */
  private static ConnectionString resolveConnectionString(Environment environment) {
    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");

    if (!StringUtils.hasText(mongodbUri)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB database factory");
    }

    ConnectionString connectionString = new ConnectionString(mongodbUri);

    String databaseName = environment.getProperty("spring.data.mongodb.database");

    if (!StringUtils.hasText(databaseName)) {
      databaseName = connectionString.getDatabase();
    }

    if (!StringUtils.hasText(databaseName)) {
      throw new BeanInitializationException(
          "A MongoDB database name specified via the MongoDB URI (spring.data.mongodb.uri) or explicitly (spring.data.mongodb.database) is required to initialize the default MongoDB database factory");
    }

    return buildConnectionString(environment, databaseName);
  }

  /**
   * Build a MongoDB connection string using the base URI from the Spring {@link Environment} and
   * the specified database name.
   *
   * @param environment the Spring {@link Environment}
   * @param databaseName the MongoDB database name
   * @return the MongoDB connection string
   */
  private static ConnectionString buildConnectionString(
      Environment environment, String databaseName) {
    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");

    if (!StringUtils.hasText(mongodbUri)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB database factory");
    }

    ConnectionString original = new ConnectionString(mongodbUri);

    StringBuilder builder = new StringBuilder();

    builder.append(original.getConnectionString());

    if (!StringUtils.hasText(original.getDatabase())) {
      if (mongodbUri.contains("?")) {
        int queryIndex = mongodbUri.indexOf('?');
        builder = new StringBuilder(mongodbUri.substring(0, queryIndex));
        if (!builder.toString().endsWith("/")) {
          builder.append('/');
        }
        builder.append(databaseName);
        builder.append(mongodbUri.substring(queryIndex));
      } else {
        builder = new StringBuilder(mongodbUri);
        if (!builder.toString().endsWith("/")) {
          builder.append('/');
        }
        builder.append(databaseName);
      }
    }

    return new ConnectionString(builder.toString());
  }

  /**
   * Validate and return the MongoDB connection string.
   *
   * @param connectionString the MongoDB connection string
   * @return the MongoDB connection string
   */
  private static ConnectionString requireConnectionStringWithDatabase(
      ConnectionString connectionString) {
    if (connectionString == null || !StringUtils.hasText(connectionString.getDatabase())) {
      throw new BeanInitializationException(
          "A MongoDB URI must include a database name to initialize the MongoDB database factory");
    }

    return connectionString;
  }

  /**
   * Validate and return the MongoDB connection string.
   *
   * @param connectionString the MongoDB connection string
   * @return the MongoDB connection string
   */
  private static ConnectionString requireConnectionStringWithDatabase(String connectionString) {
    if (!StringUtils.hasText(connectionString)) {
      throw new BeanInitializationException(
          "A MongoDB URI must be specified to initialize the MongoDB database factory");
    }

    return requireConnectionStringWithDatabase(new ConnectionString(connectionString));
  }

  /**
   * Validate and return the MongoDB database name.
   *
   * @param databaseName the MongoDB database name
   * @return the MongoDB database name
   */
  private static String requireDatabaseName(String databaseName) {
    if (!StringUtils.hasText(databaseName)) {
      throw new BeanInitializationException(
          "A MongoDB database name must be specified explicitly or via spring.data.mongodb.database / spring.data.mongodb.uri");
    }

    return databaseName;
  }
}

//
// @SuppressWarnings({"unused", "NullableProblems"})
// public class DefaultMongoDatabaseFactory
//    implements org.springframework.data.mongodb.MongoDatabaseFactory, DisposableBean {
//
//  /* Logger */
//  private static final Logger log = LoggerFactory.getLogger(DefaultMongoDatabaseFactory.class);
//
//  private final String databaseName;
//
//  private final PersistenceExceptionTranslator exceptionTranslator;
//
//  private final MongoClient mongoClient;
//
//  /** The writeConcern controlling the acknowledgment of write operations. */
//  private WriteConcern writeConcern;
//
//  /**
//   * Constructs a new {@code DefaultMongoDatabaseFactory}.
//   *
//   * @param applicationContext the Spring {@link ApplicationContext}
//   * @param uri the MongoDB URI
//   * @param databaseName the MongoDB database name
//   */
//  public DefaultMongoDatabaseFactory(
//      ApplicationContext applicationContext, String uri, String databaseName) {
//    if (!StringUtils.hasText(uri)) {
//      throw new BeanInitializationException(
//          "A MongoDB URI must be specified to initialize the MongoDB database factory");
//    }
//
//    if (!StringUtils.hasText(databaseName)) {
//      throw new BeanInitializationException(
//          "A MongoDB database name must be specified to initialize the MongoDB database factory");
//    }
//
//    this.databaseName = databaseName;
//    this.exceptionTranslator = new MongoExceptionTranslator();
//    this.mongoClient = createMongoClient(applicationContext, uri);
//  }
//
//  /**
//   * Constructs a new {@code DefaultMongoDatabaseFactory}.
//   *
//   * @param applicationContext the Spring {@link ApplicationContext}
//   * @param databaseName the MongoDB database name
//   */
//  public DefaultMongoDatabaseFactory(ApplicationContext applicationContext, String databaseName) {
//    Environment environment = applicationContext.getEnvironment();
//
//    // Ensure that we have a MongoDB URI specified via spring.data.mongodb.uri
//    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");
//
//    if (!StringUtils.hasText(mongodbUri)) {
//      throw new BeanInitializationException(
//          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB
// database factory");
//    }
//
//    /*
//     * We chose to explicitly initialize the database factory with a database name, so we must
//     * provide one.
//     */
//    if (!StringUtils.hasText(databaseName)) {
//      throw new BeanInitializationException(
//          "A MongoDB database name must be specified to initialize the MongoDB database factory");
//    }
//
//    this.databaseName = databaseName;
//    this.exceptionTranslator = new MongoExceptionTranslator();
//    this.mongoClient = createMongoClient(applicationContext, mongodbUri);
//  }
//
//  /**
//   * Constructs a new {@code DefaultMongoDatabaseFactory}.
//   *
//   * @param applicationContext the Spring {@link ApplicationContext}
//   */
//  public DefaultMongoDatabaseFactory(ApplicationContext applicationContext) {
//    Environment environment = applicationContext.getEnvironment();
//
//    // Ensure that we have a MongoDB URI specified via spring.data.mongodb.uri
//    String mongodbUri = environment.getProperty("spring.data.mongodb.uri");
//
//    if (!StringUtils.hasText(mongodbUri)) {
//      throw new BeanInitializationException(
//          "A MongoDB URI must be specified via (spring.data.mongodb.uri) to initialize the MongoDB
// database factory");
//    }
//
//    String mongodbDatabaseName = environment.getProperty("spring.data.mongodb.database");
//
//    /*
//     * If we did not explicity specify a database name via spring.data.mongodb.database, look for
//     * one in the URI.
//     */
//    if (!StringUtils.hasText(mongodbDatabaseName)) {
//      ConnectionString connectionString = new ConnectionString(mongodbUri);
//      mongodbDatabaseName = connectionString.getDatabase();
//    }
//
//    if (!StringUtils.hasText(mongodbDatabaseName)) {
//      throw new BeanInitializationException(
//          "A MongoDB database name specified via the MongoDB URI (spring.data.mongodb.uri) or
// explicitly (spring.data.mongodb.database) is required to initialize the default MongoDB database
// factory");
//    }
//
//    this.databaseName = mongodbDatabaseName;
//    this.exceptionTranslator = new MongoExceptionTranslator();
//    this.mongoClient = createMongoClient(applicationContext, mongodbUri);
//  }
//
//  /**
//   * Constructs a new {@code DefaultMongoDatabaseFactory} from a MongoDB URI.
//   *
//   * @param applicationContext the Spring {@link ApplicationContext}
//   * @param uri the MongoDB URI
//   * @return the {@code DefaultMongoDatabaseFactory} instance for the MongoDB URI
//   */
//  public static DefaultMongoDatabaseFactory fromUri(
//      ApplicationContext applicationContext, String uri) {
//    ConnectionString connectionString = new ConnectionString(uri);
//
//    return new DefaultMongoDatabaseFactory(applicationContext, uri,
// connectionString.getDatabase());
//  }
//
//  @Override
//  public void destroy() throws Exception {
//    try {
//      this.mongoClient.close();
//    } catch (Throwable e) {
//      log.error("Failed to close the mongo client", e);
//    }
//  }
//
//  @Override
//  public PersistenceExceptionTranslator getExceptionTranslator() {
//    return exceptionTranslator;
//  }
//
//  @Override
//  public MongoDatabase getMongoDatabase(String databaseName) throws DataAccessException {
//    MongoDatabase mongoDatabase = mongoClient.getDatabase(databaseName);
//
//    if (writeConcern == null) {
//      return mongoDatabase;
//    } else {
//      return mongoDatabase.withWriteConcern(writeConcern);
//    }
//  }
//
//  @Override
//  public MongoDatabase getMongoDatabase() throws DataAccessException {
//    return getMongoDatabase(databaseName);
//  }
//
//  @Override
//  public ClientSession getSession(ClientSessionOptions options) {
//    return mongoClient.startSession(options);
//  }
//
//  /**
//   * Sets the writeConcern controlling the acknowledgment of write operations.
//   *
//   * @param writeConcern the writeConcern controlling the acknowledgment of write operations
//   */
//  public void setWriteConcern(WriteConcern writeConcern) {
//    this.writeConcern = writeConcern;
//  }
//
//  @Override
//  public org.springframework.data.mongodb.MongoDatabaseFactory withSession(ClientSession session)
// {
//    return new ClientSessionBoundMongoDbFactory(session, this);
//  }
//
//  private MongoClient createMongoClient(ApplicationContext applicationContext, String mongodbUri)
// {
//    Environment environment = applicationContext.getEnvironment();
//
//    // Check whether an alternative UUID representation was specified via the
//    // spring.data.mongodb.uuid-representation configuration value
//    UuidRepresentation uuidRepresentation;
//
//    try {
//      uuidRepresentation =
//          environment.getProperty(
//              "spring.data.mongodb.uuid-representation",
//              UuidRepresentation.class,
//              UuidRepresentation.JAVA_LEGACY);
//    } catch (Throwable e) {
//      uuidRepresentation = UuidRepresentation.JAVA_LEGACY;
//    }
//
//    ConnectionString connectionString = new ConnectionString(mongodbUri);
//
//    SslBundles sslBundles;
//    try {
//      sslBundles = applicationContext.getBean(SslBundles.class);
//    } catch (Throwable e) {
//      sslBundles = null;
//    }
//
//    StandardMongoClientSettingsBuilderCustomizer standardMongoClientSettingsBuilderCustomizer =
//        new StandardMongoClientSettingsBuilderCustomizer(
//            connectionString, uuidRepresentation, new Ssl(), sslBundles);
//
//    List<MongoClientSettingsBuilderCustomizer> mongoClientSettingsBuilderCustomizers =
//        new ArrayList<>();
//    mongoClientSettingsBuilderCustomizers.add(standardMongoClientSettingsBuilderCustomizer);
//
//    MongoClientSettings mongoClientSettings = MongoClientSettings.builder().build();
//
//    return new MongoClientFactory(mongoClientSettingsBuilderCustomizers)
//        .createMongoClient(mongoClientSettings);
//  }
//
//  /**
//   * {@link ClientSession} bound {@link org.springframework.data.mongodb.MongoDatabaseFactory}
//   * decorating the database with a {@link SessionAwareMethodInterceptor}.
//   */
//  static final class ClientSessionBoundMongoDbFactory
//      implements org.springframework.data.mongodb.MongoDatabaseFactory {
//
//    private final org.springframework.data.mongodb.MongoDatabaseFactory delegate;
//
//    private final ClientSession session;
//
//    public ClientSessionBoundMongoDbFactory(
//        ClientSession session, org.springframework.data.mongodb.MongoDatabaseFactory delegate) {
//      this.session = session;
//      this.delegate = delegate;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//      if (this == o) return true;
//      if (o == null || getClass() != o.getClass()) return false;
//
//      ClientSessionBoundMongoDbFactory that = (ClientSessionBoundMongoDbFactory) o;
//
//      if (!ObjectUtils.nullSafeEquals(this.session, that.session)) {
//        return false;
//      }
//      return ObjectUtils.nullSafeEquals(this.delegate, that.delegate);
//    }
//
//    public org.springframework.data.mongodb.MongoDatabaseFactory getDelegate() {
//      return this.delegate;
//    }
//
//    @Override
//    public PersistenceExceptionTranslator getExceptionTranslator() {
//      return delegate.getExceptionTranslator();
//    }
//
//    @Override
//    public MongoDatabase getMongoDatabase() throws DataAccessException {
//      return proxyMongoDatabase(delegate.getMongoDatabase());
//    }
//
//    @Override
//    public MongoDatabase getMongoDatabase(String dbName) throws DataAccessException {
//      return proxyMongoDatabase(delegate.getMongoDatabase(dbName));
//    }
//
//    @Override
//    public ClientSession getSession(ClientSessionOptions options) {
//      return delegate.getSession(options);
//    }
//
//    public ClientSession getSession() {
//      return this.session;
//    }
//
//    @Override
//    public int hashCode() {
//      int result = ObjectUtils.nullSafeHashCode(this.session);
//      result = 31 * result + ObjectUtils.nullSafeHashCode(this.delegate);
//      return result;
//    }
//
//    @Override
//    public boolean isTransactionActive() {
//      return session != null && session.hasActiveTransaction();
//    }
//
//    public String toString() {
//      return "MongoDatabaseFactorySupport.ClientSessionBoundMongoDbFactory(session="
//          + this.getSession()
//          + ", delegate="
//          + this.getDelegate()
//          + ")";
//    }
//
//    @Override
//    public org.springframework.data.mongodb.MongoDatabaseFactory withSession(
//        ClientSession session) {
//      return delegate.withSession(session);
//    }
//
//    private <T> T createProxyInstance(
//        com.mongodb.session.ClientSession session, T target, Class<T> targetType) {
//
//      ProxyFactory factory = new ProxyFactory();
//      factory.setTarget(target);
//      factory.setInterfaces(targetType);
//      factory.setOpaque(true);
//
//      factory.addAdvice(
//          new SessionAwareMethodInterceptor<>(
//              session,
//              target,
//              ClientSession.class,
//              MongoDatabase.class,
//              this::proxyDatabase,
//              MongoCollection.class,
//              this::proxyCollection));
//
//      return targetType.cast(factory.getProxy(target.getClass().getClassLoader()));
//    }
//
//    private MongoCollection<?> proxyCollection(
//        com.mongodb.session.ClientSession session, MongoCollection<?> collection) {
//      return createProxyInstance(session, collection, MongoCollection.class);
//    }
//
//    private MongoDatabase proxyDatabase(
//        com.mongodb.session.ClientSession session, MongoDatabase database) {
//      return createProxyInstance(session, database, MongoDatabase.class);
//    }
//
//    private MongoDatabase proxyMongoDatabase(MongoDatabase database) {
//      return createProxyInstance(session, database, MongoDatabase.class);
//    }
//  }
// }
