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

package digital.inception.security;

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
 * The <b>GroupMembers</b> class holds the results of a request to retrieve a list of group members.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of group members")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "userDirectoryId",
  "groupName",
  "groupMembers",
  "total",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "GroupMembers", namespace = "http://inception.digital/security")
@XmlType(
    name = "GroupMembers",
    namespace = "http://inception.digital/security",
    propOrder = {
      "userDirectoryId",
      "groupName",
      "groupMembers",
      "total",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class GroupMembers implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the group members. */
  @Schema(description = "The optional filter that was applied to the group members")
  @JsonProperty
  @XmlElement(name = "Filter")
  private String filter;

  /** The group members. */
  @Schema(description = "The group members", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "GroupMembers", required = true)
  @XmlElement(name = "GroupMember", required = true)
  private List<GroupMember> groupMembers;

  /** The name of the group the group members are associated with. */
  @Schema(
      description = "The name of the group the group members are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "GroupName", required = true)
  private String groupName;

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

  /** The ID for the user directory the group members are associated with. */
  @Schema(
      description = "The ID for the user directory the group members are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  private UUID userDirectoryId;

  /** Constructs a new <b>GroupMembers</b>. */
  public GroupMembers() {}

  /**
   * Constructs a new <b>GroupMembers</b>.
   *
   * @param userDirectoryId the ID for the user directory the group members are associated with
   * @param groupName the name of the group the group members are associated with
   * @param groupMembers the group members
   * @param total the total number of group members
   * @param filter the optional filter that was applied to the group members
   * @param sortDirection the sort direction that was applied to the group members
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public GroupMembers(
      UUID userDirectoryId,
      String groupName,
      List<GroupMember> groupMembers,
      long total,
      String filter,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
    this.groupMembers = groupMembers;
    this.total = total;
    this.filter = filter;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the group members.
   *
   * @return the optional filter that was applied to the group members
   */
  public String getFilter() {
    return filter;
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
   * Returns the name of the group the group members are associated with.
   *
   * @return the name of the group the group members are associated with
   */
  public String getGroupName() {
    return groupName;
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

  /**
   * Returns the ID for the user directory the group members are associated with.
   *
   * @return the ID for the user directory the group members are associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }
}
