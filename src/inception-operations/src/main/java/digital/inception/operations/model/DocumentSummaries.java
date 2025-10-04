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

/**
 * The {@code DocumentSummaries} class holds the results of a request to retrieve a list of document
 * summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of document summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "DocumentSummaries", namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {"documentSummaries", "total", "sortBy", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class DocumentSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The document summaries. */
  @Schema(description = "The document summaries", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "DocumentSummaries", required = true)
  @XmlElement(name = "DocumentSummary", required = true)
  private List<DocumentSummary> documentSummaries;

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

  /** The method used to sort the document summaries e.g. by definition ID. */
  @Schema(description = "The method used to sort the document summaries e.g. by definition ID")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private DocumentSortBy sortBy;

  /** The sort direction that was applied to the document summaries. */
  @Schema(description = "The sort direction that was applied to the document summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of document summaries. */
  @Schema(
      description = "The total number of document summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code DocumentSummaries}. */
  public DocumentSummaries() {}

  /**
   * Constructs a new {@code DocumentSummaries}.
   *
   * @param documentSummaries the document summaries
   * @param total the total number of document summaries
   * @param sortBy the method used to sort the document summaries e.g. by definition ID
   * @param sortDirection the sort direction that was applied to the document summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public DocumentSummaries(
      List<DocumentSummary> documentSummaries,
      long total,
      DocumentSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.documentSummaries = documentSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the document summaries.
   *
   * @return the document summaries
   */
  public List<DocumentSummary> getDocumentSummaries() {
    return documentSummaries;
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
   * Returns the method used to sort the document summaries e.g. by definition ID.
   *
   * @return the method used to sort the document summaries
   */
  public DocumentSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the document summaries.
   *
   * @return the sort direction that was applied to the document summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of document summaries.
   *
   * @return the total number of document summaries
   */
  public long getTotal() {
    return total;
  }
}
