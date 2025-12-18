/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.operations.store;
//
// import digital.inception.core.exception.ServiceUnavailableException;
// import digital.inception.core.sorting.SortDirection;
// import digital.inception.operations.exception.DocumentDefinitionNotFoundException;
// import digital.inception.operations.exception.DocumentNotFoundException;
// import digital.inception.operations.exception.DuplicateWorkflowDocumentException;
// import digital.inception.operations.exception.DuplicateWorkflowException;
// import digital.inception.operations.exception.DuplicateWorkflowNoteException;
// import digital.inception.operations.exception.InteractionNotFoundException;
// import digital.inception.operations.exception.WorkflowDocumentNotFoundException;
// import digital.inception.operations.exception.WorkflowInteractionLinkNotFoundException;
// import digital.inception.operations.exception.WorkflowNotFoundException;
// import digital.inception.operations.exception.WorkflowNoteNotFoundException;
// import digital.inception.operations.exception.WorkflowStepNotFoundException;
// import digital.inception.operations.model.AttributeSearchCriteria;
// import digital.inception.operations.model.Document;
// import digital.inception.operations.model.ExternalReferenceSearchCriteria;
// import digital.inception.operations.model.OutstandingWorkflowDocument;
// import digital.inception.operations.model.ProvideWorkflowDocumentRequest;
// import digital.inception.operations.model.RejectWorkflowDocumentRequest;
// import digital.inception.operations.model.RequestWorkflowDocumentRequest;
// import digital.inception.operations.model.ResetWorkflowDocumentRequest;
// import digital.inception.operations.model.SearchWorkflowsRequest;
// import digital.inception.operations.model.VariableSearchCriteria;
// import digital.inception.operations.model.VerifyWorkflowDocumentRequest;
// import digital.inception.operations.model.WaiveWorkflowDocumentRequest;
// import digital.inception.operations.model.Workflow;
// import digital.inception.operations.model.WorkflowAttribute;
// import digital.inception.operations.model.WorkflowDefinition;
// import digital.inception.operations.model.WorkflowDefinitionDocumentDefinition;
// import digital.inception.operations.model.WorkflowDefinitionId;
// import digital.inception.operations.model.WorkflowDocument;
// import digital.inception.operations.model.WorkflowDocumentSortBy;
// import digital.inception.operations.model.WorkflowDocumentStatus;
// import digital.inception.operations.model.WorkflowDocuments;
// import digital.inception.operations.model.WorkflowEngineIds;
// import digital.inception.operations.model.WorkflowExternalReference;
// import digital.inception.operations.model.WorkflowInteractionLink;
// import digital.inception.operations.model.WorkflowInteractionLinkId;
// import digital.inception.operations.model.WorkflowNote;
// import digital.inception.operations.model.WorkflowNoteSortBy;
// import digital.inception.operations.model.WorkflowNotes;
// import digital.inception.operations.model.WorkflowSortBy;
// import digital.inception.operations.model.WorkflowStatus;
// import digital.inception.operations.model.WorkflowStep;
// import digital.inception.operations.model.WorkflowStepId;
// import digital.inception.operations.model.WorkflowStepStatus;
// import digital.inception.operations.model.WorkflowSummaries;
// import digital.inception.operations.model.WorkflowSummary;
// import digital.inception.operations.model.WorkflowVariable;
// import digital.inception.operations.persistence.jpa.WorkflowDocumentRepository;
// import digital.inception.operations.persistence.jpa.WorkflowInteractionLinkRepository;
// import digital.inception.operations.persistence.jpa.WorkflowNoteRepository;
// import digital.inception.operations.persistence.jpa.WorkflowRepository;
// import digital.inception.operations.persistence.jpa.WorkflowStepRepository;
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
// import jakarta.persistence.TypedQuery;
// import jakarta.persistence.criteria.CriteriaBuilder;
// import jakarta.persistence.criteria.CriteriaQuery;
// import jakarta.persistence.criteria.Expression;
// import jakarta.persistence.criteria.Predicate;
// import jakarta.persistence.criteria.Root;
// import java.sql.Connection;
// import java.sql.DatabaseMetaData;
// import java.time.OffsetDateTime;
// import java.util.ArrayList;
// import java.util.List;
// import java.util.Optional;
// import java.util.UUID;
// import javax.sql.DataSource;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.context.annotation.Conditional;
// import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
// import org.springframework.data.domain.Sort;
// import org.springframework.data.jpa.domain.Specification;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
//
/// **
// * The {@code InternalWorkflowStore} class provides the internal workflow store implementation.
// *
// * @author Marcus Portmann
// */
// @Component
// @Conditional(InternalWorkflowStoreEnabledCondition.class)
// @SuppressWarnings("unused")
// public class InternalWorkflowStore implements WorkflowStore {
//
//  /* Logger */
//  private static final Logger log = LoggerFactory.getLogger(InternalWorkflowStore.class);
//
//  /** The Document Store. */
//  private final DocumentStore documentStore;
//
//  /** The Interaction Store. */
//  private final InteractionStore interactionStore;
//
//
//
////
////
////  @Override
////  public WorkflowDocuments getWorkflowDocuments(
////      UUID tenantId,
////      UUID workflowId,
////      String filter,
////      WorkflowDocumentSortBy sortBy,
////      SortDirection sortDirection,
////      Integer pageIndex,
////      Integer pageSize,
////      int maxResults)
////      throws WorkflowNotFoundException, ServiceUnavailableException {
////    try {
////      if (!workflowRepository.existsByTenantIdAndId(tenantId, workflowId)) {
////        throw new WorkflowNotFoundException(tenantId, workflowId);
////      }
////
////      PageRequest pageRequest;
////
////      if (sortBy == WorkflowDocumentSortBy.REQUESTED) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "requested");
////      } else if (sortBy == WorkflowDocumentSortBy.REQUESTED_BY) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "requestedBy");
////      } else if (sortBy == WorkflowDocumentSortBy.PROVIDED) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "provided");
////      } else if (sortBy == WorkflowDocumentSortBy.PROVIDED_BY) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "providedBy");
////      } else if (sortBy == WorkflowDocumentSortBy.VERIFIED) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "verified");
////      } else if (sortBy == WorkflowDocumentSortBy.VERIFIED_BY) {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "verifiedBy");
////      } else {
////        pageRequest =
////            PageRequest.of(
////                pageIndex,
////                Math.min(pageSize, maxResults),
////                (sortDirection == SortDirection.ASCENDING)
////                    ? Sort.Direction.ASC
////                    : Sort.Direction.DESC,
////                "requested");
////      }
////
////      Page<WorkflowDocument> workflowDocumentPage =
////          workflowDocumentRepository.findAll(
////              (Specification<WorkflowDocument>)
////                  (root, query, criteriaBuilder) -> {
////                    List<Predicate> predicates = new ArrayList<>();
////
////                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
////                    predicates.add(criteriaBuilder.equal(root.get("workflowId"), workflowId));
////
////                    if (StringUtils.hasText(filter)) {
////                      predicates.add(
////                          criteriaBuilder.or(
////                              criteriaBuilder.like(
////                                  criteriaBuilder.lower(root.get("documentDefinitionId")),
////                                  "%" + filter.toLowerCase() + "%"),
////                              criteriaBuilder.like(
////                                  criteriaBuilder.lower(root.get("requestedBy")),
////                                  "%" + filter.toLowerCase() + "%"),
////                              criteriaBuilder.like(
////                                  criteriaBuilder.lower(root.get("providedBy")),
////                                  "%" + filter.toLowerCase() + "%"),
////                              criteriaBuilder.like(
////                                  criteriaBuilder.lower(root.get("verifiedBy")),
////                                  "%" + filter.toLowerCase() + "%")));
////                    }
////
////                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
////                  },
////              pageRequest);
////
////      return new WorkflowDocuments(
////          workflowDocumentPage.toList(),
////          workflowDocumentPage.getTotalElements(),
////          sortBy,
////          sortDirection,
////          pageIndex,
////          pageSize);
////    } catch (WorkflowNotFoundException e) {
////      throw e;
////    } catch (Throwable e) {
////      throw new ServiceUnavailableException(
////          "Failed to retrieve the filtered workflow documents for the workflow ("
////              + workflowId
////              + ") for the tenant ("
////              + tenantId
////              + ")",
////          e);
////    }
////  }
//
//
//
//
//  @Override
//  public WorkflowDocument updateWorkflowDocument(UUID tenantId, WorkflowDocument workflowDocument)
//      throws WorkflowDocumentNotFoundException, ServiceUnavailableException {
//    try {
//      if (!workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocument.getId())) {
//        throw new WorkflowDocumentNotFoundException(tenantId, workflowDocument.getId());
//      }
//
//      return workflowDocumentRepository.saveAndFlush(workflowDocument);
//    } catch (WorkflowDocumentNotFoundException e) {
//      throw e;
//    } catch (Throwable e) {
//      throw new ServiceUnavailableException(
//          "Failed to update the workflow document ("
//              + workflowDocument.getId()
//              + ") for the tenant ("
//              + tenantId
//              + ")",
//          e);
//    }
//  }
//
//
//
//  @Override
//  public boolean workflowDocumentExists(UUID tenantId, UUID workflowDocumentId)
//      throws ServiceUnavailableException {
//    try {
//      return workflowDocumentRepository.existsByTenantIdAndId(tenantId, workflowDocumentId);
//    } catch (Throwable e) {
//      throw new ServiceUnavailableException(
//          "Failed to check whether the workflow document ("
//              + workflowDocumentId
//              + ") exists for the tenant ("
//              + tenantId
//              + ")",
//          e);
//    }
//  }
//
//
//
//
// }
