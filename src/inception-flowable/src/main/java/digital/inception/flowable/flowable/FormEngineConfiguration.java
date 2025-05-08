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

package digital.inception.flowable.flowable;

import java.io.InputStream;
import org.flowable.app.engine.AppEngines;
import org.flowable.common.engine.api.scope.ScopeTypes;
import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.db.SchemaManager;
import org.flowable.common.engine.impl.interceptor.CommandInterceptor;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;
import org.flowable.form.api.FormEngineConfigurationApi;
import org.flowable.form.api.FormManagementService;
import org.flowable.form.api.FormRepositoryService;
import org.flowable.form.api.FormService;

/**
 * The {@code FormEngineConfiguration} class.
 *
 * @author Marcus Portmann
 */
public class FormEngineConfiguration extends AbstractEngineConfiguration
    implements FormEngineConfigurationApi {

  private final String formEngineName = AppEngines.NAME_DEFAULT;

  private final digital.inception.flowable.flowable.FormManagementService formManagementService;

  private final digital.inception.flowable.flowable.FormRepositoryService formRepositoryService;

  private final digital.inception.flowable.flowable.FormService formService;

  private AppEngines FormEngines;

  /** Creates a new {@code FormEngineConfiguration} instance. */
  public FormEngineConfiguration() {
    this.formManagementService = new digital.inception.flowable.flowable.FormManagementService();
    this.formRepositoryService = new digital.inception.flowable.flowable.FormRepositoryService();
    this.formService = new digital.inception.flowable.flowable.FormService();
  }

  @Override
  public CommandInterceptor createTransactionInterceptor() {
    return null;
  }

  @Override
  public String getEngineCfgKey() {
    return EngineConfigurationConstants.KEY_FORM_ENGINE_CONFIG;
  }

  @Override
  public String getEngineName() {
    return formEngineName;
  }

  @Override
  public String getEngineScopeType() {
    return ScopeTypes.FORM;
  }

  @Override
  public FormManagementService getFormManagementService() {
    return formManagementService;
  }

  @Override
  public FormRepositoryService getFormRepositoryService() {
    return formRepositoryService;
  }

  @Override
  public FormService getFormService() {
    return formService;
  }

  @Override
  public InputStream getMyBatisXmlConfigurationStream() {
    return null;
  }

  @Override
  protected SchemaManager createEngineSchemaManager() {
    return null;
  }

  @Override
  protected void initDbSqlSessionFactoryEntitySettings() {}
}
