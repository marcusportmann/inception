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

import java.util.Map;
import org.flowable.common.engine.api.repository.EngineDeployment;
import org.flowable.common.engine.api.repository.EngineResource;
import org.flowable.common.engine.impl.EngineDeployer;
import org.flowable.engine.impl.persistence.entity.DeploymentEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The <b>FormDeployer</b> class.
 *
 * @author Marcus Portmann
 */
public class FormDeployer implements EngineDeployer {

  /** The suffixes for form resources. */
  public static final String[] FORM_RESOURCE_SUFFIXES = new String[] {".form"};

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(FormDeployer.class);

  /** Constructs a new <b>FormDeployer</b>. */
  public FormDeployer() {}

  @Override
  public void deploy(EngineDeployment deployment, Map<String, Object> deploymentSettings) {

    if (deployment instanceof DeploymentEntity deploymentEntity) {

      for (EngineResource resource : deploymentEntity.getResources().values()) {


        if (isFormResource(resource.getName())) {
          log.debug("Processing form resource {}", resource.getName());

          System.out.println(new String(resource.getBytes()));

          //        LOGGER.debug("Processing BPMN resource {}", resource.getName());
          //        BpmnParse parse = createBpmnParseFromResource(resource);
          //        for (ProcessDefinitionEntity processDefinition : parse.getProcessDefinitions())
          // {
          //          processDefinitions.add(processDefinition);
          //          processDefinitionsToBpmnParseMap.put(processDefinition, parse);
          //          processDefinitionsToResourceMap.put(processDefinition, resource);
          //        }
        }
      }
    }
  }

  /**
   * Check whether the resource with the specified name is a form.
   *
   * @param resourceName the name of the resource
   * @return <b>true</b> if the resource is a form resource or <b>false</b> otherwise
   */
  protected boolean isFormResource(String resourceName) {
    for (String suffix : FORM_RESOURCE_SUFFIXES) {
      if (resourceName.endsWith(suffix)) {
        return true;
      }
    }

    return false;
  }
}
