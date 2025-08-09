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

import digital.inception.operations.model.OutstandingWorkflowDocument;
import digital.inception.operations.model.WorkflowDocument;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/**
 * The {@code WorkflowDocumentRepository} interface provides the persistence operations for the
 * {@link WorkflowDocument} domain type.
 *
 * <p>This repository extends {@code JpaRepository} to provide standard CRUD (Create, Read, Update,
 * Delete) operations and {@code JpaSpecificationExecutor} to support more complex queries using
 * specifications.
 *
 * @author Marcus Portmann
 */
public interface WorkflowDocumentRepository
    extends JpaRepository<WorkflowDocument, UUID>, JpaSpecificationExecutor<WorkflowDocument> {

  /**
   * Retrieve the number of workflow documents associated with the document with specified ID.
   *
   * @param documentId the ID for the document
   * @return the number of workflow documents associated with the document with specified ID.
   */
  long countByDocumentId(UUID documentId);

  /**
   * Returns whether a workflow document with the specified tenant ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow document is associated with
   * @param workflowDocumentId the ID for the workflow document
   * @return {@code true} if a workflow document with the specified tenant ID and ID exists or
   *     {@code false} otherwise
   */
  boolean existsByTenantIdAndId(UUID tenantId, UUID workflowDocumentId);

  /**
   * Returns whether a workflow document with the specified tenant ID, workflow ID and ID exists.
   *
   * @param tenantId the ID for the tenant the workflow document is associated with
   * @param workflowId the ID for the workflow the workflow document is associated with
   * @param workflowDocumentId the ID for the workflow document
   * @return {@code true} if a workflow document with the specified tenant ID, workflow ID and ID
   *     exists or {@code false} otherwise
   */
  boolean existsByTenantIdAndWorkflowIdAndId(
      UUID tenantId, UUID workflowId, UUID workflowDocumentId);

  /**
   * Retrieve the workflow document.
   *
   * @param tenantId the ID for the tenant the workflow document is associated with
   * @param workflowDocumentId the ID for the workflow document
   * @return an Optional containing the workflow document or an empty Optional if the workflow
   *     document could not be found
   */
  Optional<WorkflowDocument> findByTenantIdAndId(UUID tenantId, UUID workflowDocumentId);

  /**
   * Retrieve the outstanding workflow documents for the workflow.
   *
   * @param workflowId the ID for the workflow the outstanding workflow documents are associated
   *     with
   * @return the outstanding workflow documents
   */
  @Query(
      """
      select new digital.inception.operations.model.OutstandingWorkflowDocument(
        wd.id,
        wd.tenantId,
        wd.workflowId,
        wd.documentDefinitionId,
        dd.name,
        wd.status,
        wd.requested
      )
      from WorkflowDocument wd
      join wd.documentDefinition dd
      where wd.workflowId = :workflowId
        and wd.status in (digital.inception.operations.model.WorkflowDocumentStatus.REJECTED,
                          digital.inception.operations.model.WorkflowDocumentStatus.REQUESTED)
      order by dd.name asc, wd.requested asc
    """)
  List<OutstandingWorkflowDocument> findOutstandingWorkflowDocumentsForWorkflow(
      @Param("workflowId") UUID workflowId);

  /**
   * Reject the workflow document.
   *
   * @param workflowDocumentId the ID for the workflow document
   * @param rejected the date and time the workflow document was rejected
   * @param rejectedBy the person or system that rejected the workflow document
   * @param rejectionReason the reason the workflow document was rejected
   * @return the number of workflow documents that were rejected
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update WorkflowDocument wd "
          + "set wd.status = digital.inception.operations.model.WorkflowDocumentStatus.REJECTED, "
          + " wd.rejected = :rejected, "
          + " wd.rejectedBy = :rejectedBy, "
          + " wd.rejectionReason = :rejectionReason, "
          + " wd.verified = null, "
          + " wd.verifiedBy = null "
          + "where wd.id = :workflowDocumentId")
  int rejectWorkflowDocument(
      @Param("workflowDocumentId") UUID workflowDocumentId,
      @Param("rejected") OffsetDateTime rejected,
      @Param("rejectedBy") String rejectedBy,
      @Param("rejectionReason") String rejectionReason);

  /**
   * Verify the workflow document.
   *
   * @param workflowDocumentId the ID for the workflow document
   * @param verified the date and time the workflow document was verified
   * @param verifiedBy the person or system that verified the workflow document
   * @return the number of workflow documents that were verified
   */
  @Transactional
  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query(
      "update WorkflowDocument wd "
          + "set wd.status = digital.inception.operations.model.WorkflowDocumentStatus.VERIFIED, "
          + " wd.verified = :verified, "
          + " wd.verifiedBy = :verifiedBy, "
          + " wd.rejected = null, "
          + " wd.rejectedBy = null, "
          + " wd.rejectionReason = null "
          + "where wd.id = :workflowDocumentId")
  int verifyWorkflowDocument(
      @Param("workflowDocumentId") UUID workflowDocumentId,
      @Param("verified") OffsetDateTime verified,
      @Param("verifiedBy") String verifiedBy);
}
