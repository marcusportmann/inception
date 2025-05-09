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
import org.flowable.common.engine.api.management.TableMetaData;
import org.flowable.common.engine.api.management.TablePageQuery;
import org.flowable.common.engine.api.tenant.ChangeTenantIdBuilder;

/**
 * The {@code FormManagementService} class.
 *
 * @author Marcus Portmann
 */
public class FormManagementService implements org.flowable.form.api.FormManagementService {

  /** Constructs a new {@code FormManagementService}. */
  public FormManagementService() {}

  @Override
  public ChangeTenantIdBuilder createChangeTenantIdBuilder(String fromTenantId, String toTenantId) {
    throw new RuntimeException(
        "FormManagementService::createChangeTenantIdBuilder(String fromTenantId, String toTenantId) NOT IMPLEMENTED");
  }

  @Override
  public TablePageQuery createTablePageQuery() {
    throw new RuntimeException("FormManagementService::createTablePageQuery() NOT IMPLEMENTED");
  }

  @Override
  public Map<String, Long> getTableCount() {
    throw new RuntimeException("FormManagementService::getTableCount() NOT IMPLEMENTED");
  }

  @Override
  public TableMetaData getTableMetaData(String tableName) {
    throw new RuntimeException(
        "FormManagementService::getTableMetaData(String tableName) NOT IMPLEMENTED");
  }

  @Override
  public String getTableName(Class<?> idmEntityClass) {
    throw new RuntimeException(
        "FormManagementService::getTableName(Class<?> idmEntityClass) NOT IMPLEMENTED");
  }
}
