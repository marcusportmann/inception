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

import java.util.ArrayList;
import java.util.List;
import org.flowable.common.engine.impl.AbstractEngineConfiguration;
import org.flowable.common.engine.impl.EngineConfigurator;
import org.flowable.common.engine.impl.EngineDeployer;
import org.flowable.common.engine.impl.interceptor.EngineConfigurationConstants;

/**
 * The <b>FormEngineConfigurator</b> class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({"LombokGetterMayBeUsed", "unused"})
public class FormEngineConfigurator implements EngineConfigurator {

  private FormEngineConfiguration formEngineConfiguration;

  /** Constructs a new <b>FormEngineConfigurator</b>. */
  public FormEngineConfigurator() {}

  @Override
  public void beforeInit(AbstractEngineConfiguration engineConfiguration) {
    registerCustomDeployers(engineConfiguration);
  }

  @Override
  public void configure(AbstractEngineConfiguration engineConfiguration) {
    if (formEngineConfiguration == null) {
      formEngineConfiguration = new FormEngineConfiguration();
    }

    engineConfiguration.addEngineConfiguration(
        formEngineConfiguration.getEngineCfgKey(),
        formEngineConfiguration.getEngineScopeType(),
        formEngineConfiguration);

    // engineConfiguration.addEngineConfiguration();

    //    initialiseCommonProperties(engineConfiguration, this.formEngineConfiguration);

    //    if (engineConfiguration instanceof SpringEngineConfiguration springEngineConfiguration) {
    //
    // formEngineConfiguration.setTransactionManager(springEngineConfiguration.getTransactionManager());
    //
    //      if (formEngineConfiguration.getBeans() == null) {
    //        formEngineConfiguration.setBeans(engineConfiguration.getBeans());
    //      }
    //    }

    // initFormEngine();

    // initServiceConfigurations(engineConfiguration, formEngineConfiguration);
  }

  /**
   * Returns the form engine configuration.
   *
   * @return the form engine configuration
   */
  public FormEngineConfiguration getFormEngineConfiguration() {
    return formEngineConfiguration;
  }

  @Override
  public int getPriority() {
    return EngineConfigurationConstants.PRIORITY_ENGINE_FORM;
  }

  /**
   * Set the form engine configuration.
   *
   * @param formEngineConfiguration the form engine configuration
   * @return the form engine configurator
   */
  public FormEngineConfigurator setFormEngineConfiguration(
      FormEngineConfiguration formEngineConfiguration) {
    this.formEngineConfiguration = formEngineConfiguration;
    return this;
  }

  /**
   * Returns the custom deployers.
   *
   * @return the custom deployers
   */
  protected List<EngineDeployer> getCustomDeployers() {
    List<EngineDeployer> deployers = new ArrayList<>();
    deployers.add(new FormDeployer());
    return deployers;
  }

  /**
   * Register the customer deployers
   *
   * @param engineConfiguration the engine configuration
   */
  protected void registerCustomDeployers(AbstractEngineConfiguration engineConfiguration) {
    List<EngineDeployer> deployers = getCustomDeployers();
    if (deployers != null) {
      if (engineConfiguration.getCustomPostDeployers() == null) {
        engineConfiguration.setCustomPostDeployers(new ArrayList<>());
      }
      engineConfiguration.getCustomPostDeployers().addAll(deployers);
    }
  }

  //  @Override
  //  protected List<EngineDeployer> getCustomDeployers() {
  //    List<EngineDeployer> deployers = new ArrayList<>();
  //    deployers.add(new FormDeployer());
  //    return deployers;
  //  }

  //  @Override
  //  protected String getMybatisCfgPath() {
  //    return FormEngineConfiguration.DEFAULT_MYBATIS_MAPPING_FILE;
  //  }

  //  @Override
  //  protected List<Class<? extends Entity>> getEntityInsertionOrder() {
  //    return null;
  //  }

  //  @Override
  //  protected List<Class<? extends Entity>> getEntityDeletionOrder() {
  //    return null;
  //  }

  //  protected synchronized FormEngine initFormEngine() {
  //    if (formEngineConfiguration == null) {
  //      throw new FlowableException("FormEngineConfiguration is required");
  //    }
  //
  //    return formEngineConfiguration.buildFormEngine();
  //  }

  //  protected void initServiceConfigurations(AbstractEngineConfiguration engineConfiguration,
  // AbstractEngineConfiguration targetEngineConfiguration) {
  //    for (String serviceConfigurationKey :
  // engineConfiguration.getServiceConfigurations().keySet()) {
  //      if (targetEngineConfiguration.getServiceConfigurations() == null
  //          ||
  // !targetEngineConfiguration.getServiceConfigurations().containsKey(serviceConfigurationKey)) {
  //        targetEngineConfiguration.addServiceConfiguration(serviceConfigurationKey,
  // engineConfiguration.getServiceConfigurations().get(serviceConfigurationKey));
  //      }
  //    }
  //  }

}
