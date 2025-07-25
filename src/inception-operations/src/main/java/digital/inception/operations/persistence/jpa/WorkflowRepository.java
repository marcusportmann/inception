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

package digital.inception.operations.persistence.jpa;

import digital.inception.operations.model.Workflow;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code WorkflowRepository} interface provides the persistence operations for the {@link
 * Workflow} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@code JpaSpecificationExecutor} to support more complex queries using
 * specifications. It is designed to handle all data access requirements for workflows within the
 * application.
 *
 * @author Marcus Portmann
 */
public interface WorkflowRepository
    extends JpaRepository<Workflow, UUID>, JpaSpecificationExecutor<Workflow> {

  /**
   * Returns whether a workflow with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param workflowId the ID for the workflow
   * @return {@code true} if a workflow with the specified tenant ID and ID exists or {@code false}
   *     otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID workflowId);

  /**
   * Retrieve the workflow.
   *
   * @param tenantId the ID for the tenant the workflow is associated with
   * @param workflowId the ID for the workflow
   * @return an Optional containing the workflow or an empty Optional if the workflow could not be
   *     found
   */
  Optional<Workflow> findByTenantIdAndId(UUID tenantId, UUID workflowId);
}
