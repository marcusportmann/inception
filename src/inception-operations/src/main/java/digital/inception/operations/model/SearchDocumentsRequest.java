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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * The {@code SearchDocumentsRequest} class represents a request to search for documents matching
 * specific criteria.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A request to search for documents matching specific criteria")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentDefinitionId",
  "status",
  "attributes",
  "externalReferences",
  "variables",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "SearchDocumentsRequest", namespace = "https://inception.digital/operations")
@XmlType(
    name = "SearchDocumentsRequest",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "documentDefinitionId",
      "status",
      "attributes",
      "externalReferences",
      "variables",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class SearchDocumentsRequest implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The attribute search criteria to apply when searching for documents. */
  @Schema(description = "The attribute search criteria to apply when searching for documents")
  @JsonProperty
  @XmlElementWrapper(name = "Attributes")
  @XmlElement(name = "Attribute")
  @Valid
  private List<AttributeSearchCriteria> attributes;

  /** The document definition ID filter to apply to the documents. */
  @Schema(description = "The ID for the document")
  @JsonProperty
  @XmlElement(name = "DocumentDefinitionId")
  private String documentDefinitionId;

  /** The external reference search criteria to apply when searching for documents. */
  @Schema(
      description = "The external reference search criteria to apply when searching for documents")
  @JsonProperty
  @XmlElementWrapper(name = "ExternalReferences")
  @XmlElement(name = "ExternalReference")
  @Valid
  private List<ExternalReferenceSearchCriteria> externalReferences;

  /** The page index. */
  @Schema(description = "The page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The page size. */
  @Schema(description = "The page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The method used to sort the documents e.g. by definition ID. */
  @Schema(description = "The method used to sort the documents e.g. by definition ID")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private DocumentSortBy sortBy;

  /** The sort direction to apply to the documents. */
  @Schema(description = "The sort direction to apply to the documents")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The variable search criteria to apply when searching for documents. */
  @Schema(description = "The variable search criteria to apply when searching for documents")
  @JsonProperty
  @XmlElementWrapper(name = "Variables")
  @XmlElement(name = "Variable")
  @Valid
  private List<VariableSearchCriteria> variables;

  /** Constructs a new {@code SearchDocumentsRequest}. */
  public SearchDocumentsRequest() {}

  /**
   * Constructs a new {@code SearchDocumentsRequest}.
   *
   * @param documentDefinitionId the document definition ID filter to apply to the documents
   * @param attributes the attribute search criteria to apply when searching for documents
   * @param externalReferences the external reference search criteria to apply when searching for
   *     documents
   * @param variables the variable search criteria to apply when searching for documents
   * @param sortBy the method used to sort the documents e.g. by definition ID
   * @param sortDirection the sort direction to apply to the documents
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public SearchDocumentsRequest(
      String documentDefinitionId,
      List<AttributeSearchCriteria> attributes,
      List<ExternalReferenceSearchCriteria> externalReferences,
      List<VariableSearchCriteria> variables,
      DocumentSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.documentDefinitionId = documentDefinitionId;
    this.attributes = attributes;
    this.externalReferences = externalReferences;
    this.variables = variables;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the attribute search criteria to apply when searching for documents.
   *
   * @return the attribute search criteria to apply when searching for documents
   */
  public List<AttributeSearchCriteria> getAttributes() {
    return attributes;
  }

  /**
   * Returns the document definition ID filter to apply to the documents.
   *
   * @return the document definition ID filter to apply to the documents
   */
  public String getDocumentDefinitionId() {
    return documentDefinitionId;
  }

  /**
   * Returns the external reference search criteria to apply when searching for documents.
   *
   * @return the external reference search criteria to apply when searching for documents
   */
  public List<ExternalReferenceSearchCriteria> getExternalReferences() {
    return externalReferences;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the method used to sort the documents e.g. by definition ID.
   *
   * @return the method used to sort the documents e.g. by definition ID
   */
  public DocumentSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction to apply to the documents.
   *
   * @return the sort direction to apply to the documents
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the variable search criteria to apply when searching for documents.
   *
   * @return the variable search criteria to apply when searching for documents
   */
  public List<VariableSearchCriteria> getVariables() {
    return variables;
  }

  /**
   * Set the attribute search criteria to apply when searching for documents.
   *
   * @param attributes the attribute search criteria to apply when searching for documents
   */
  public void setAttributes(List<AttributeSearchCriteria> attributes) {
    this.attributes = attributes;
  }

  /**
   * Set the document definition ID filter to apply to the documents.
   *
   * @param documentDefinitionId the document definition ID filter to apply to the documents
   */
  public void setDocumentDefinitionId(String documentDefinitionId) {
    this.documentDefinitionId = documentDefinitionId;
  }

  /**
   * Set the external reference search criteria to apply when searching for documents.
   *
   * @param externalReferences the external reference search criteria to apply when searching for
   *     documents
   */
  public void setExternalReferences(List<ExternalReferenceSearchCriteria> externalReferences) {
    this.externalReferences = externalReferences;
  }

  /**
   * Set the page index.
   *
   * @param pageIndex the page index
   */
  public void setPageIndex(Integer pageIndex) {
    this.pageIndex = pageIndex;
  }

  /**
   * Set the page size.
   *
   * @param pageSize the page size
   */
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   * Set the method used to sort the documents e.g. by definition ID.
   *
   * @param sortBy the method used to sort the documents e.g. by definition ID
   */
  public void setSortBy(DocumentSortBy sortBy) {
    this.sortBy = sortBy;
  }

  /**
   * Set the sort direction to apply to the documents.
   *
   * @param sortDirection the sort direction to apply to the documents
   */
  public void setSortDirection(SortDirection sortDirection) {
    this.sortDirection = sortDirection;
  }

  /**
   * Set the variable search criteria to apply when searching for documents.
   *
   * @param variables the variable search criteria to apply when searching for documents
   */
  public void setVariables(List<VariableSearchCriteria> variables) {
    this.variables = variables;
  }
}
