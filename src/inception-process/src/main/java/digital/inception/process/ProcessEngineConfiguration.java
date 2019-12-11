/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.process;

//~--- non-JDK imports --------------------------------------------------------

import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;

import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

//~--- JDK imports ------------------------------------------------------------

import javax.sql.DataSource;

/**
 * The <code>ProcessEngineConfiguration</code> class is used to configure the Camunda Process
 * Engine.
 *
 * @author Marcus Portmann
 */
public class ProcessEngineConfiguration extends SpringProcessEngineConfiguration
{
  /**
   * Constructs a new <code>ProcessEngineConfiguration</code>.
   *
   * @param processEngineName   the name of the process engine
   * @param applicationContext  the Spring application context
   * @param dataSource          the data source used to provide connections to the application
   *                            database
   * @param transactionManager  the Spring platform transaction manager
   * @param activateJobExecutor should the job executor be activated
   */
  public ProcessEngineConfiguration(String processEngineName,
      ApplicationContext applicationContext, DataSource dataSource,
      PlatformTransactionManager transactionManager, boolean activateJobExecutor)
  {
    super();

    try
    {
      setApplicationContext(applicationContext);
      setDatabaseSchema("CAMUNDA");
      setDatabaseSchemaUpdate(DB_SCHEMA_UPDATE_FALSE);
      setDatabaseTablePrefix("CAMUNDA.");
      setDataSource(dataSource);
      setJobExecutorActivate(activateJobExecutor);
      setProcessEngineName(processEngineName);
      setTransactionManager(transactionManager);

      // Explicitly disable specific features
      setAuthorizationEnabled(false);
      setCmmnEnabled(false);
      setDbHistoryUsed(false);
      setDbIdentityUsed(false);
      setDmnEnabled(false);
      setHistory(HISTORY_NONE);
    }
    catch (Throwable e)
    {
      throw new RuntimeException("Failed to initialize the Camunda Process Engine configuration",
          e);
    }
  }
}
