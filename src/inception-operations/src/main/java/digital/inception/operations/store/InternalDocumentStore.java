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

package digital.inception.operations.store;

import digital.inception.core.exception.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.operations.exception.DocumentNotFoundException;
import digital.inception.operations.exception.DocumentNoteNotFoundException;
import digital.inception.operations.exception.DuplicateDocumentException;
import digital.inception.operations.exception.DuplicateDocumentNoteException;
import digital.inception.operations.model.AttributeSearchCriteria;
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentAttribute;
import digital.inception.operations.model.DocumentExternalReference;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.DocumentSummary;
import digital.inception.operations.model.ExternalReferenceSearchCriteria;
import digital.inception.operations.model.SearchDocumentsRequest;
import digital.inception.operations.persistence.jpa.DocumentNoteRepository;
import digital.inception.operations.persistence.jpa.DocumentRepository;
import digital.inception.operations.persistence.jpa.DocumentSummaryRepository;
import jakarta.persistence.criteria.Predicate;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * The {@code InternalDocumentStore} class provides the internal document store implementation.
 *
 * @author Marcus Portmann
 */
@Component
@Conditional(InternalDocumentStoreEnabledCondition.class)
@SuppressWarnings("unused")
public class InternalDocumentStore implements DocumentStore {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(InternalDocumentStore.class);

  /** The Document Note Repository. */
  private final DocumentNoteRepository documentNoteRepository;

  /** The Document Repository. */
  private final DocumentRepository documentRepository;

  /** The Document Summary Repository. */
  private final DocumentSummaryRepository documentSummaryRepository;

  /**
   * Constructs a new {@code InternalDocumentStore}.
   *
   * @param documentNoteRepository the Document Note Repository
   * @param documentRepository the Document Repository
   * @param documentSummaryRepository the Document Summary Repository
   */
  public InternalDocumentStore(
      DocumentNoteRepository documentNoteRepository,
      DocumentRepository documentRepository,
      DocumentSummaryRepository documentSummaryRepository) {
    this.documentNoteRepository = documentNoteRepository;
    this.documentRepository = documentRepository;
    this.documentSummaryRepository = documentSummaryRepository;
  }

  @Override
  public String calculateDocumentDataHash(byte[] documentData) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");

      digest.update(documentData);

      return Base64.getEncoder().encodeToString(digest.digest());
    } catch (Throwable e) {
      throw new RuntimeException("Failed to calculate the SHA-256 hash for the document data", e);
    }
  }

  @Override
  public Document createDocument(UUID tenantId, Document document)
      throws DuplicateDocumentException, ServiceUnavailableException {
    try {
      if (documentRepository.existsById(document.getId())) {
        throw new DuplicateDocumentException(document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (DuplicateDocumentException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document ("
              + document.getId()
              + ") with the definition ID ("
              + document.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentNote createDocumentNote(UUID tenantId, DocumentNote documentNote)
      throws DuplicateDocumentNoteException, ServiceUnavailableException {
    try {
      if (documentNoteRepository.existsById(documentNote.getId())) {
        throw new DuplicateDocumentNoteException(documentNote.getId());
      }

      return documentNoteRepository.saveAndFlush(documentNote);
    } catch (DuplicateDocumentNoteException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the document note ("
              + documentNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public void deleteDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      documentRepository.deleteById(documentId);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document (" + documentId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public void deleteDocumentNote(UUID tenantId, UUID documentNoteId)
      throws DocumentNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!documentNoteRepository.existsByTenantIdAndId(tenantId, documentNoteId)) {
        throw new DocumentNoteNotFoundException(tenantId, documentNoteId);
      }

      documentNoteRepository.deleteById(documentNoteId);
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to delete the document note ("
              + documentNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean documentExists(UUID tenantId, UUID documentId) throws ServiceUnavailableException {
    try {
      return documentRepository.existsByTenantIdAndId(tenantId, documentId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document ("
              + documentId
              + ") exists for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public boolean documentNoteExists(UUID tenantId, UUID documentId, UUID documentNoteId)
      throws ServiceUnavailableException {
    try {
      return documentNoteRepository.existsByTenantIdAndDocumentIdAndId(
          tenantId, documentId, documentNoteId);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to check whether the document note ("
              + documentNoteId
              + ") exists for the document ("
              + documentId
              + ") and tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public Document getDocument(UUID tenantId, UUID documentId)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and document ID includes a security check to ensure
       * that the document not only exists, but is also associated with the specified tenant.
       */
      Optional<Document> documentOptional =
          documentRepository.findByTenantIdAndId(tenantId, documentId);

      if (documentOptional.isEmpty()) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      return documentOptional.get();
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document (" + documentId + ") for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public DocumentNote getDocumentNote(UUID tenantId, UUID documentNoteId)
      throws DocumentNoteNotFoundException, ServiceUnavailableException {
    try {
      /*
       * NOTE: The search by both tenant ID and document note ID includes a security check to ensure
       * that the document note not only exists, but is also associated with the specified tenant.
       */
      Optional<DocumentNote> documentNoteOptional =
          documentNoteRepository.findByTenantIdAndId(tenantId, documentNoteId);

      if (documentNoteOptional.isEmpty()) {
        throw new DocumentNoteNotFoundException(tenantId, documentNoteId);
      }

      return documentNoteOptional.get();
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the document note ("
              + documentNoteId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentNotes getDocumentNotes(
      UUID tenantId,
      UUID documentId,
      String filter,
      DocumentNoteSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, documentId)) {
        throw new DocumentNotFoundException(tenantId, documentId);
      }

      PageRequest pageRequest;

      if (sortBy == DocumentNoteSortBy.CREATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      } else if (sortBy == DocumentNoteSortBy.CREATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "createdBy");
      } else if (sortBy == DocumentNoteSortBy.UPDATED) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updated");
      } else if (sortBy == DocumentNoteSortBy.UPDATED_BY) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "updatedBy");
      } else {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "created");
      }

      Page<DocumentNote> documentNotePage =
          documentNoteRepository.findAll(
              (Specification<DocumentNote>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
                    predicates.add(criteriaBuilder.equal(root.get("documentId"), documentId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("createdBy")),
                                  "%" + filter.toLowerCase() + "%"),
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("updatedBy")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new DocumentNotes(
          tenantId,
          documentId,
          documentNotePage.toList(),
          documentNotePage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered document notes for the document ("
              + documentId
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentSummaries searchDocuments(
      UUID tenantId, SearchDocumentsRequest searchDocumentsRequest, int maxResults)
      throws ServiceUnavailableException {
    try {
      // Build Specification
      Specification<DocumentSummary> specification =
          (root, query, criteriaBuilder) -> {
            // Avoid duplicates when joins or subqueries are involved
            query.distinct(true);

            // AND'ed top-level predicates
            List<Predicate> andPredicates = new ArrayList<>();

            // Tenant filter
            if (tenantId != null) {
              andPredicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));
            }

            // Top-level filters
            if (StringUtils.hasText(searchDocumentsRequest.getDocumentDefinitionId())) {
              andPredicates.add(
                  criteriaBuilder.equal(
                      criteriaBuilder.lower(root.get("definitionId")),
                      searchDocumentsRequest.getDocumentDefinitionId().toLowerCase()));
            }

            // Collect OR buckets from attributes, external refs, variables
            List<Predicate> orBuckets = new ArrayList<>();

            // Attribute criteria (OR all attribute pairs)
            if (searchDocumentsRequest.getAttributes() != null
                && !searchDocumentsRequest.getAttributes().isEmpty()) {
              List<Predicate> attributePredicates = new ArrayList<>();

              for (AttributeSearchCriteria attributeSearchCriteria :
                  searchDocumentsRequest.getAttributes()) {
                if (attributeSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var documentAttributeRoot = subQuery.from(DocumentAttribute.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(documentAttributeRoot.get("documentId"), root.get("id"));

                if (StringUtils.hasText(attributeSearchCriteria.getCode())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentAttributeRoot.get("code")),
                              attributeSearchCriteria.getCode().toLowerCase()));
                }
                if (StringUtils.hasText(attributeSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentAttributeRoot.get("value")),
                              attributeSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                attributePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!attributePredicates.isEmpty()) {
                orBuckets.add(criteriaBuilder.or(attributePredicates.toArray(new Predicate[0])));
              }
            }

            // External reference criteria (OR all external reference pairs)
            if (searchDocumentsRequest.getExternalReferences() != null
                && !searchDocumentsRequest.getExternalReferences().isEmpty()) {
              List<Predicate> externalReferencePredicates = new ArrayList<>();

              for (ExternalReferenceSearchCriteria externalReferenceSearchCriteria :
                  searchDocumentsRequest.getExternalReferences()) {
                if (externalReferenceSearchCriteria == null) continue;

                var subQuery = query.subquery(Integer.class);
                var documentExternalReferenceRoot = subQuery.from(DocumentExternalReference.class);
                Predicate subPredicate =
                    criteriaBuilder.equal(
                        documentExternalReferenceRoot.get("objectId"), root.get("id"));

                if (StringUtils.hasText(externalReferenceSearchCriteria.getType())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentExternalReferenceRoot.get("type")),
                              externalReferenceSearchCriteria.getType().toLowerCase()));
                }
                if (StringUtils.hasText(externalReferenceSearchCriteria.getValue())) {
                  subPredicate =
                      criteriaBuilder.and(
                          subPredicate,
                          criteriaBuilder.equal(
                              criteriaBuilder.lower(documentExternalReferenceRoot.get("value")),
                              externalReferenceSearchCriteria.getValue().toLowerCase()));
                }

                subQuery.select(criteriaBuilder.literal(1)).where(subPredicate);
                externalReferencePredicates.add(criteriaBuilder.exists(subQuery));
              }

              if (!externalReferencePredicates.isEmpty()) {
                orBuckets.add(
                    criteriaBuilder.or(externalReferencePredicates.toArray(new Predicate[0])));
              }
            }

            // If any of the groups were supplied, OR the groups together
            if (!orBuckets.isEmpty()) {
              andPredicates.add(criteriaBuilder.or(orBuckets.toArray(new Predicate[0])));
            }

            return criteriaBuilder.and(andPredicates.toArray(new Predicate[0]));
          };

      // Sorting
      String sortByPropertyName =
          resolveDocumentSortByPropertyName(searchDocumentsRequest.getSortBy());
      Sort.Direction dir = resolveSortDirection(searchDocumentsRequest.getSortDirection());
      Sort sort = Sort.by(dir, sortByPropertyName);

      // Paging
      int pageIndex =
          searchDocumentsRequest.getPageIndex() == null
              ? 0
              : Math.max(0, searchDocumentsRequest.getPageIndex());
      int pageSize =
          searchDocumentsRequest.getPageSize() == null
              ? 50
              : Math.max(1, Math.min(searchDocumentsRequest.getPageSize(), maxResults));
      Pageable pageable = PageRequest.of(pageIndex, pageSize, sort);

      Page<DocumentSummary> documentSummaryPage =
          documentSummaryRepository.findAll(specification, pageable);

      return new DocumentSummaries(
          tenantId,
          documentSummaryPage.toList(),
          documentSummaryPage.getTotalElements(),
          searchDocumentsRequest.getSortBy(),
          searchDocumentsRequest.getSortDirection(),
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to search for the documents for the tenant (" + tenantId + ")", e);
    }
  }

  @Override
  public Document updateDocument(UUID tenantId, Document document)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, document.getId())) {
        throw new DocumentNotFoundException(tenantId, document.getId());
      }

      return documentRepository.saveAndFlush(document);
    } catch (DocumentNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document ("
              + document.getId()
              + ") with the definition ID ("
              + document.getDefinitionId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  @Override
  public DocumentNote updateDocumentNote(UUID tenantId, DocumentNote documentNote)
      throws DocumentNoteNotFoundException, ServiceUnavailableException {
    try {
      if (!documentNoteRepository.existsByTenantIdAndId(tenantId, documentNote.getId())) {
        throw new DocumentNoteNotFoundException(tenantId, documentNote.getId());
      }

      return documentNoteRepository.saveAndFlush(documentNote);
    } catch (DocumentNoteNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the document note ("
              + documentNote.getId()
              + ") for the tenant ("
              + tenantId
              + ")",
          e);
    }
  }

  private String resolveDocumentSortByPropertyName(DocumentSortBy sortBy) {
    if (sortBy == null) return "definitionId";
    return switch (sortBy) {
      case DocumentSortBy.DEFINITION_ID -> "definitionId";
      default -> "definitionId";
    };
  }

  private Sort.Direction resolveSortDirection(
      digital.inception.core.sorting.SortDirection sortDirection) {
    if (sortDirection == null) {
      return Sort.Direction.DESC;
    } else if (sortDirection == SortDirection.ASCENDING) {
      return Sort.Direction.ASC;
    } else {
      return Sort.Direction.DESC;
    }
  }
}
