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
 * The {@code DocumentTemplateSummaries} class represents the results of a request to retrieve a
 * list of document template summaries.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of document template summaries")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "documentTemplateSummaries",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(
    name = "DocumentTemplateSummaries",
    namespace = "https://inception.digital/operations")
@XmlType(
    name = "DocumentTemplateSummaries",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "documentTemplateSummaries",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class DocumentTemplateSummaries implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The document template summaries. */
  @Schema(
      description = "The document template summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "DocumentTemplateSummaries", required = true)
  @XmlElement(name = "DocumentTemplateSummary", required = true)
  private List<DocumentTemplateSummary> documentTemplateSummaries;

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

  /** The method used to sort the document template summaries e.g. by name. */
  @Schema(description = "The method used to sort the document template summaries e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private DocumentTemplateSortBy sortBy;

  /** The sort direction that was applied to the document template summaries. */
  @Schema(description = "The sort direction that was applied to the document template summaries")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of document template summaries. */
  @Schema(
      description = "The total number of document template summaries",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code DocumentTemplateSummaries}. */
  public DocumentTemplateSummaries() {}

  /**
   * Constructs a new {@code DocumentTemplateSummaries}.
   *
   * @param documentTemplateSummaries the document template summaries
   * @param total the total number of document template summaries
   * @param sortBy the method used to sort the document template summaries e.g. by name
   * @param sortDirection the sort direction that was applied to the document template summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public DocumentTemplateSummaries(
      List<DocumentTemplateSummary> documentTemplateSummaries,
      long total,
      DocumentTemplateSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.documentTemplateSummaries = documentTemplateSummaries;
    this.total = total;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the document template summaries.
   *
   * @return the document template summaries
   */
  public List<DocumentTemplateSummary> getDocumentTemplateSummaries() {
    return documentTemplateSummaries;
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
   * Returns the method used to sort the document template summaries e.g. by name.
   *
   * @return the method used to sort the document template summaries
   */
  public DocumentTemplateSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the document template summaries.
   *
   * @return the sort direction that was applied to the document template summaries
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of document template summaries.
   *
   * @return the total number of document template summaries
   */
  public long getTotal() {
    return total;
  }
}
