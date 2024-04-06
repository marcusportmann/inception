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

package digital.inception.workflow.flowable;

import java.util.Map;
import org.flowable.form.api.FormInfo;
import org.flowable.form.api.FormInstance;
import org.flowable.form.api.FormInstanceInfo;
import org.flowable.form.api.FormInstanceQuery;
import org.springframework.stereotype.Service;

/**
 * The <b>FormService</b> class.
 *
 * @author Marcus Portmann
 */
@Service
public class FormService implements org.flowable.form.api.FormService {

  /** Constructs a new <b>FormService</b>. */
  public FormService() {}

  @Override
  public FormInstance createFormInstance(
      Map<String, Object> variables,
      FormInfo formInfo,
      String taskId,
      String processInstanceId,
      String processDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public FormInstanceQuery createFormInstanceQuery() {
    return null;
  }

  @Override
  public FormInstance createFormInstanceWithScopeId(
      Map<String, Object> variables,
      FormInfo formInfo,
      String taskId,
      String scopeId,
      String scopeType,
      String scopeDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public void deleteFormInstance(String formInstanceId) {}

  @Override
  public void deleteFormInstancesByFormDefinition(String formDefinitionId) {}

  @Override
  public void deleteFormInstancesByProcessDefinition(String processDefinitionId) {}

  @Override
  public void deleteFormInstancesByScopeDefinition(String scopeDefinitionId) {}

  @Override
  public FormInstanceInfo getFormInstanceModelById(
      String formInstanceId, Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelById(
      String formDefinitionId,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelById(
      String formDefinitionId,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKey(
      String formDefinitionKey,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKey(
      String formDefinitionKey,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndParentDeploymentId(
      String formDefinitionKey,
      String parentDeploymentId,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndParentDeploymentId(
      String formDefinitionKey,
      String parentDeploymentId,
      String taskId,
      String processInstanceId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndParentDeploymentIdAndScopeId(
      String formDefinitionKey,
      String parentDeploymentId,
      String scopeId,
      String scopeType,
      Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndParentDeploymentIdAndScopeId(
      String formDefinitionKey,
      String parentDeploymentId,
      String scopeId,
      String scopeType,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndScopeId(
      String formDefinitionKey, String scopeId, String scopeType, Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInstanceInfo getFormInstanceModelByKeyAndScopeId(
      String formDefinitionKey,
      String scopeId,
      String scopeType,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public byte[] getFormInstanceValues(String formInstanceId) {
    return new byte[0];
  }

  @Override
  public FormInfo getFormModelWithVariablesById(
      String formDefinitionId, String taskId, Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInfo getFormModelWithVariablesById(
      String formDefinitionId,
      String taskId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInfo getFormModelWithVariablesByKey(
      String formDefinitionKey, String taskId, Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInfo getFormModelWithVariablesByKey(
      String formDefinitionKey,
      String taskId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public FormInfo getFormModelWithVariablesByKeyAndParentDeploymentId(
      String formDefinitionKey,
      String parentDeploymentId,
      String taskId,
      Map<String, Object> variables) {
    return null;
  }

  @Override
  public FormInfo getFormModelWithVariablesByKeyAndParentDeploymentId(
      String formDefinitionKey,
      String parentDeploymentId,
      String taskId,
      Map<String, Object> variables,
      String tenantId,
      boolean fallbackToDefaultTenant) {
    return null;
  }

  @Override
  public Map<String, Object> getVariablesFromFormSubmission(
      String elementId,
      String elementType,
      String scopeId,
      String scopeDefinitionId,
      String scopeType,
      FormInfo formInfo,
      Map<String, Object> values,
      String outcome) {
    return null;
  }

  @Override
  public FormInstance saveFormInstance(
      Map<String, Object> variables,
      FormInfo formInfo,
      String taskId,
      String processInstanceId,
      String processDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public FormInstance saveFormInstanceByFormDefinitionId(
      Map<String, Object> variables,
      String formDefinitionId,
      String taskId,
      String processInstanceId,
      String processDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public FormInstance saveFormInstanceWithScopeId(
      Map<String, Object> variables,
      FormInfo formInfo,
      String taskId,
      String scopeId,
      String scopeType,
      String scopeDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public FormInstance saveFormInstanceWithScopeId(
      Map<String, Object> variables,
      String formDefinitionId,
      String taskId,
      String scopeId,
      String scopeType,
      String scopeDefinitionId,
      String tenantId,
      String outcome) {
    return null;
  }

  @Override
  public void validateFormFields(
      String elementId,
      String elementType,
      String scopeId,
      String scopeDefinitionId,
      String scopeType,
      FormInfo formInfo,
      Map<String, Object> values) {}
}
