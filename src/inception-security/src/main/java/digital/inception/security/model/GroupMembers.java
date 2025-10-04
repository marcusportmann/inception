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

/**
 * The {@code GroupMembers} class holds the results of a request to retrieve a list of group
 * members.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of group members")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"groupMembers", "total", "sortDirection", "pageIndex", "pageSize"})
@XmlRootElement(name = "GroupMembers", namespace = "https://inception.digital/security")
@XmlType(
    name = "GroupMembers",
    namespace = "https://inception.digital/security",
    propOrder = {"groupMembers", "total", "sortDirection", "pageIndex", "pageSize"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class GroupMembers implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The group members. */
  @Schema(description = "The group members", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "GroupMembers", required = true)
  @XmlElement(name = "GroupMember", required = true)
  private List<GroupMember> groupMembers;

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

  /** The sort direction that was applied to the group members. */
  @Schema(
      description = "The sort direction that was applied to the group members",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "SortDirection", required = true)
  private SortDirection sortDirection;

  /** The total number of group members. */
  @Schema(
      description = "The total number of group members",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** Constructs a new {@code GroupMembers}. */
  public GroupMembers() {}

  /**
   * Constructs a new {@code GroupMembers}.
   *
   * @param groupMembers the group members
   * @param total the total number of group members
   * @param sortDirection the sort direction that was applied to the group members
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public GroupMembers(
      List<GroupMember> groupMembers,
      long total,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.groupMembers = groupMembers;
    this.total = total;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the group members.
   *
   * @return the group members
   */
  public List<GroupMember> getGroupMembers() {
    return groupMembers;
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
   * Returns the sort direction that was applied to the group members.
   *
   * @return the sort direction that was applied to the group members
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of group members.
   *
   * @return the total number of group members
   */
  public long getTotal() {
    return total;
  }
}
