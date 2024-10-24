package digital.inception.workflow.flowable;

/**
 * The <b>DmnDbSchemaManager</b> class provides a no-op database schema manager for the DMN
 * component of the Flowable platform, which defers the Flowable database schema initialization to
 * the standard Inception Framework database initialization capabilities using the
 * <b>db/flowable-all.changelog.xml</b> Liquibase changelog on the classpath.
 *
 * @author Marcus Portmann
 */
public class DmnDbSchemaManager extends org.flowable.dmn.engine.impl.db.DmnDbSchemaManager {

  @Override
  public void initSchema(String databaseSchemaUpdate) {
    // Defer database schema management to the Inception Framework.
  }
}
