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
import java.util.List;
import org.flowable.form.api.FormDefinition;
import org.flowable.form.api.FormDefinitionQuery;
import org.flowable.form.api.FormDeploymentBuilder;
import org.flowable.form.api.FormDeploymentQuery;
import org.flowable.form.api.FormInfo;
import org.flowable.form.api.NativeFormDefinitionQuery;
import org.flowable.form.api.NativeFormDeploymentQuery;

/**
 * The <b>FormRepositoryService</b> class.
 *
 * @author Marcus Portmann
 */
public class FormRepositoryService implements org.flowable.form.api.FormRepositoryService {

  /** Constructs a new <b>FormRepositoryService</b>. */
  public FormRepositoryService() {}

  @Override
  public FormDeploymentBuilder createDeployment() {
    throw new RuntimeException("FormRepositoryService::createDeployment() NOT IMPLEMENTED");
  }

  @Override
  public void deleteDeployment(String deploymentId) {
    throw new RuntimeException("FormRepositoryService::deleteDeployment(String deploymentId) NOT IMPLEMENTED");
  }

  @Override
  public void deleteDeployment(String deploymentId, boolean cascade) {
    throw new RuntimeException("FormRepositoryService::deleteDeployment(String deploymentId, boolean cascade) NOT IMPLEMENTED");
  }

  @Override
  public FormDefinitionQuery createFormDefinitionQuery() {
    throw new RuntimeException("FormRepositoryService::createFormDefinitionQuery() NOT IMPLEMENTED");
  }

  @Override
  public NativeFormDefinitionQuery createNativeFormDefinitionQuery() {
    throw new RuntimeException("FormRepositoryService::createNativeFormDefinitionQuery() NOT IMPLEMENTED");
  }

  @Override
  public void setDeploymentCategory(String deploymentId, String category) {
    throw new RuntimeException("FormRepositoryService::setDeploymentCategory(String deploymentId, String category) NOT IMPLEMENTED");
  }

  @Override
  public void setDeploymentTenantId(String deploymentId, String newTenantId) {
    throw new RuntimeException("FormRepositoryService::setDeploymentTenantId(String deploymentId, String newTenantId) NOT IMPLEMENTED");
  }

  @Override
  public void changeDeploymentParentDeploymentId(String deploymentId, String newParentDeploymentId) {
    throw new RuntimeException("FormRepositoryService::changeDeploymentParentDeploymentId(String deploymentId, String newParentDeploymentId) NOT IMPLEMENTED");
  }

  @Override
  public List<String> getDeploymentResourceNames(String deploymentId) {
    throw new RuntimeException("FormRepositoryService::getDeploymentResourceNames(String deploymentId) NOT IMPLEMENTED");
  }

  @Override
  public InputStream getResourceAsStream(String deploymentId, String resourceName) {
    throw new RuntimeException("FormRepositoryService::getResourceAsStream(String deploymentId, String resourceName) NOT IMPLEMENTED");
  }

  @Override
  public FormDeploymentQuery createDeploymentQuery() {
    throw new RuntimeException("FormRepositoryService::createDeploymentQuery() NOT IMPLEMENTED");
  }

  @Override
  public NativeFormDeploymentQuery createNativeDeploymentQuery() {
    throw new RuntimeException("FormRepositoryService::createNativeDeploymentQuery() NOT IMPLEMENTED");
  }

  @Override
  public FormDefinition getFormDefinition(String formDefinitionId) {
    throw new RuntimeException("FormRepositoryService::getFormDefinition(String formDefinitionId) NOT IMPLEMENTED");
  }

  @Override
  public FormInfo getFormModelById(String formDefinitionId) {
    throw new RuntimeException("FormRepositoryService::getFormModelById(String formDefinitionId) NOT IMPLEMENTED");
  }

  @Override
  public FormInfo getFormModelByKey(String formDefinitionKey) {
    throw new RuntimeException("FormRepositoryService::getFormModelByKey(String formDefinitionKey) NOT IMPLEMENTED");
  }

  @Override
  public FormInfo getFormModelByKey(String formDefinitionKey, String tenantId, boolean fallbackToDefaultTenant) {
    throw new RuntimeException("FormRepositoryService::getFormModelByKey(String formDefinitionKey, String tenantId, boolean fallbackToDefaultTenant) NOT IMPLEMENTED");
  }

  @Override
  public FormInfo getFormModelByKeyAndParentDeploymentId(String formDefinitionKey, String parentDeploymentId) {
    throw new RuntimeException("FormRepositoryService::getFormModelByKeyAndParentDeploymentId(String formDefinitionKey, String parentDeploymentId) NOT IMPLEMENTED");
  }

  @Override
  public FormInfo getFormModelByKeyAndParentDeploymentId(String formDefinitionKey, String parentDeploymentId, String tenantId, boolean fallbackToDefaultTenant) {
    throw new RuntimeException("FormRepositoryService::getFormModelByKeyAndParentDeploymentId(String formDefinitionKey, String parentDeploymentId, String tenantId, boolean fallbackToDefaultTenant) NOT IMPLEMENTED");
  }

  @Override
  public InputStream getFormDefinitionResource(String formDefinitionId) {
    throw new RuntimeException("FormRepositoryService::getFormDefinitionResource(String formDefinitionId) NOT IMPLEMENTED");
  }

  @Override
  public void setFormDefinitionCategory(String formDefinitionId, String category) {
    throw new RuntimeException("FormRepositoryService::setFormDefinitionCategory(String formDefinitionId, String category) NOT IMPLEMENTED");
  }
}
