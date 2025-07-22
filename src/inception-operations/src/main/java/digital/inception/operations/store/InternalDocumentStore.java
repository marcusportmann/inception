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
import digital.inception.operations.model.Document;
import digital.inception.operations.model.DocumentNote;
import digital.inception.operations.model.DocumentNoteSortBy;
import digital.inception.operations.model.DocumentNotes;
import digital.inception.operations.model.DocumentSortBy;
import digital.inception.operations.model.DocumentSummaries;
import digital.inception.operations.model.DocumentSummary;
import digital.inception.operations.persistence.jpa.DocumentNoteRepository;
import digital.inception.operations.persistence.jpa.DocumentRepository;
import digital.inception.operations.persistence.jpa.DocumentSummaryRepository;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
        throw new DocumentNotFoundException(documentId);
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
        throw new DocumentNoteNotFoundException(documentNoteId);
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
        throw new DocumentNotFoundException(documentId);
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
        throw new DocumentNoteNotFoundException(documentNoteId);
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
        throw new DocumentNotFoundException(documentId);
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
  public DocumentSummaries getDocumentSummaries(
      UUID tenantId,
      String filter,
      DocumentSortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize,
      int maxResults)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (sortBy == DocumentSortBy.DEFINITION_ID) {
        pageRequest =
            PageRequest.of(
                pageIndex,
                Math.min(pageSize, maxResults),
                (sortDirection == SortDirection.ASCENDING)
                    ? Sort.Direction.ASC
                    : Sort.Direction.DESC,
                "definitionId");
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

      Page<DocumentSummary> documentSummaryPage =
          documentSummaryRepository.findAll(
              (Specification<DocumentSummary>)
                  (root, query, criteriaBuilder) -> {
                    List<Predicate> predicates = new ArrayList<>();

                    predicates.add(criteriaBuilder.equal(root.get("tenantId"), tenantId));

                    if (StringUtils.hasText(filter)) {
                      predicates.add(
                          criteriaBuilder.or(
                              criteriaBuilder.like(
                                  criteriaBuilder.lower(root.get("definitionId")),
                                  "%" + filter.toLowerCase() + "%")));
                    }

                    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
                  },
              pageRequest);

      return new DocumentSummaries(
          tenantId,
          documentSummaryPage.toList(),
          documentSummaryPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the filtered document summaries for the tenant (" + tenantId + ")",
          e);
    }
  }

  @Override
  public Document updateDocument(UUID tenantId, Document document)
      throws DocumentNotFoundException, ServiceUnavailableException {
    try {
      if (!documentRepository.existsByTenantIdAndId(tenantId, document.getId())) {
        throw new DocumentNotFoundException(document.getId());
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
        throw new DocumentNoteNotFoundException(documentNote.getId());
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
}
