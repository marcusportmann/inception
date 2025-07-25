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

import digital.inception.operations.model.WorkflowNote;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * The {@code WorkflowNoteRepository} interface provides the persistence operations for the {@link
 * WorkflowNote} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@code JpaSpecificationExecutor} to support more complex queries using
 * specifications.
 *
 * @author Marcus Portmann
 */
public interface WorkflowNoteRepository
    extends JpaRepository<WorkflowNote, UUID>, JpaSpecificationExecutor<WorkflowNote> {

  /**
   * Returns whether a workflow note with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return {@code true} if a workflow note with the specified tenant ID and ID exists or {@code
   *     false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID workflowNoteId);

  /**
   * Returns whether a workflow note with the specified tenant ID, workflow ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   * @param workflowId the ID for the workflow the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return {@code true} if a workflow note with the specified tenant ID, workflow ID and ID exists
   *     or {@code false} otherwise
   */
  boolean existsByTenantIdAndWorkflowIdAndId(UUID tenantId, UUID workflowId, UUID workflowNoteId);

  /**
   * Retrieve the workflow note.
   *
   * @param tenantId the ID for the tenant the workflow note is associated with
   * @param workflowNoteId the ID for the workflow note
   * @return an Optional containing the workflow note or an empty Optional if the workflow note
   *     could not be found
   */
  Optional<WorkflowNote> findByTenantIdAndId(UUID tenantId, UUID workflowNoteId);
}
