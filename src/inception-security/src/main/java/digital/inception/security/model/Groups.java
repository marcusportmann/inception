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

package digital.inception.security.model;

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
 * The <b>Groups</b> class holds the results of a request to retrieve a list of groups.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "userDirectoryId",
  "groups",
  "total",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "Groups", namespace = "http://inception.digital/security")
@XmlType(
    name = "Groups",
    namespace = "http://inception.digital/security",
    propOrder = {
      "userDirectoryId",
      "groups",
      "total",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Groups implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the groups. */
  @Schema(description = "The optional filter that was applied to the groups")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The groups. */
  @Schema(description = "The groups", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Groups", required = true)
  @XmlElement(name = "Group", required = true)
  private List<Group> groups;

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

  /** The sort direction that was applied to the groups. */
  @Schema(
      description = "The sort direction that was applied to the groups",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of groups. */
  @Schema(description = "The total number of groups", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The ID for the user directory the groups are associated with. */
  @Schema(
      description = "The ID for the user directory the groups are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  private UUID userDirectoryId;

  /** Constructs a new <b>Groups</b>. */
  public Groups() {}

  /**
   * Constructs a new <b>Groups</b>.
   *
   * @param userDirectoryId the ID for the user directory the groups are associated with
   * @param groups the groups
   * @param total the total number of groups
   * @param filter the optional filter that was applied to the groups
   * @param sortDirection the sort direction that was applied to the groups
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Groups(
      UUID userDirectoryId,
      List<Group> groups,
      long total,
      String filter,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.userDirectoryId = userDirectoryId;
    this.groups = groups;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the groups.
   *
   * @return the optional filter that was applied to the groups
   */
  public String getFilter() {
    return filter;
  }

  /**
   * Returns the groups.
   *
   * @return the groups
   */
  public List<Group> getGroups() {
    return groups;
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
   * Returns the sort direction that was applied to the groups.
   *
   * @return the sort direction that was applied to the groups
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of groups.
   *
   * @return the total number of groups
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the ID for the user directory the groups are associated with.
   *
   * @return the ID for the user directory the groups are associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }
}
