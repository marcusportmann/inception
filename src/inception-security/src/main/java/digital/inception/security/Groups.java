/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.security;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.sorting.SortDirection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <code>Groups</code> class holds the results of a request to retrieve a list of groups.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Groups")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "userDirectoryId",
  "groups",
  "total",
  "filter",
  "sortDirection",
  "pageIndex",
  "pageSize"
})
@XmlRootElement(name = "Groups", namespace = "http://security.inception.digital")
@XmlType(
    name = "Groups",
    namespace = "http://security.inception.digital",
    propOrder = {
      "userDirectoryId",
      "groups",
      "total",
      "filter",
      "sortDirection",
      "pageIndex",
      "pageSize"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Groups implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the groups. */
  @Schema(description = "The optional filter that was applied to the groups")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The groups. */
  @Schema(description = "The groups", required = true)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Groups", required = true)
  @XmlElement(name = "Group", required = true)
  private List<Group> groups;

  /** The optional page index. */
  @Schema(description = "The optional page index")
  @JsonProperty
  @XmlElement(name = "PageIndex")
  private Integer pageIndex;

  /** The optional page size. */
  @Schema(description = "The optional page size")
  @JsonProperty
  @XmlElement(name = "PageSize")
  private Integer pageSize;

  /** The optional sort direction that was applied to the groups. */
  @Schema(description = "The optional sort direction that was applied to the groups")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of groups. */
  @Schema(description = "The total number of groups", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory the groups are
   * associated with.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the user directory the groups are associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private UUID userDirectoryId;

  /** Constructs a new <code>Groups</code>. */
  public Groups() {}

  /**
   * Constructs a new <code>Groups</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the groups are associated with
   * @param groups the groups
   * @param total the total number of groups
   * @param filter the optional filter that was applied to the groups
   * @param sortDirection the optional sort direction that was applied to the groups
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   */
  public Groups(
      UUID userDirectoryId,
      List<Group> groups,
      long total,
      String filter,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize) {
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
   * Returns the optional page index.
   *
   * @return the optional page index
   */
  public Integer getPageIndex() {
    return pageIndex;
  }

  /**
   * Returns the optional page size.
   *
   * @return the optional page size
   */
  public Integer getPageSize() {
    return pageSize;
  }

  /**
   * Returns the optional sort direction that was applied to the groups.
   *
   * @return the optional sort direction that was applied to the groups
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of groups.
   *
   * @return the total number of groups
   */
  public Long getTotal() {
    return total;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   * groups are associated with.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   *     groups are associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }
}
