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
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * The {@code DocumentNotes} class holds the results of a request to retrieve a list of document
 * notes.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of document notes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "tenantId",
  "documentId",
  "documentNotes",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "DocumentNotes", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentNotes",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "tenantId",
      "documentId",
      "documentNotes",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class DocumentNotes implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the document the document notes are associated with. */
  @Schema(
      description = "The ID for the document the document notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "DocumentId", required = true)
  private UUID documentId;

  /** The document notes. */
  @Schema(description = "The document notes", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "DocumentNotes", required = true)
  @XmlElement(name = "DocumentNote", required = true)
  private List<DocumentNote> documentNotes;

  /** The filter that was applied to the document notes. */
  @Schema(description = "The filter that was applied to the document notes")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The page index. */
  @Schema(description = "The page index", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PageIndex", required = true)
  private int pageIndex;

  /** The page size. */
  @Schema(description = "The page size", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "PageSize", required = true)
  private int pageSize;

  /** The method used to sort the document notes e.g. by captured at. */
  @Schema(description = "The method used to sort the document notes e.g. by captured at")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private DocumentNoteSortBy sortBy;

  /** The sort direction that was applied to the document notes. */
  @Schema(description = "The sort direction that was applied to the document notes")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The ID for the tenant the document notes are associated with. */
  @Schema(
      description = "The ID for the tenant the document notes are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TenantId", required = true)
  private UUID tenantId;

  /** The total number of document notes. */
  @Schema(
      description = "The total number of document notes",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code DocumentNotes}. */
  public DocumentNotes() {}

  /**
   * Constructs a new {@code DocumentNotes}.
   *
   * @param tenantId the ID for the tenant the document notes are associated with
   * @param documentId the ID for the document the document notes are associated with
   * @param documentNotes the document notes
   * @param total the total number of document notes
   * @param filter the filter that was applied to the document notes
   * @param sortBy the method used to sort the document notes e.g. by captured at
   * @param sortDirection the sort direction that was applied to the document notes
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public DocumentNotes(
      UUID tenantId,
      UUID documentId,
      List<DocumentNote> documentNotes,
      long total,
      String filter,
      DocumentNoteSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.tenantId = tenantId;
    this.documentId = documentId;
    this.documentNotes = documentNotes;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the ID for the document the document notes are associated with.
   *
   * @return the ID for the document the document notes are associated with
   */
  public UUID getDocumentId() {
    return documentId;
  }

  /**
   * Returns the document notes.
   *
   * @return the document notes
   */
  public List<DocumentNote> getDocumentNotes() {
    return documentNotes;
  }

  /**
   * Returns the filter that was applied to the document notes.
   *
   * @return the filter that was applied to the document notes
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the page index.
   *
   * @return the page index
   */
  public int getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the page size.
   *
   * @return the page size
   */
  public int getPageSize() {
    return pageSize;
  }

  /**
   * Returns the method used to sort the document notes e.g. by captured at.
   *
   * @return the method used to sort the document notes
   */
  public DocumentNoteSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the document notes.
   *
   * @return the sort direction that was applied to the document notes
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the ID for the tenant the document notes are associated with.
   *
   * @return the ID for the tenant the document notes are associated with
   */
  public UUID getTenantId() {
    return tenantId;
  }

  /**
   * Returns the total number of document notes.
   *
   * @return the total number of document notes
   */
  public long getTotal() {
    return total;
  }

  /**
   * Set the ID for the document the document notes are associated with.
   *
   * @param documentId the ID for the document the document notes are associated with
   */
  public void setDocumentId(UUID documentId) {
    this.documentId = documentId;
  }
}
