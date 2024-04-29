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
 * The <b>Users</b> class holds the results of a request to retrieve a list of users.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The results of a request to retrieve a list of users")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "userDirectoryId",
  "users",
  "total",
  "sortBy",
  "sortDirection",
  "pageIndex",
  "pageSize",
  "filter"
})
@XmlRootElement(name = "Users", namespace = "https://inception.digital/security")
@XmlType(
    name = "Users",
    namespace = "https://inception.digital/security",
    propOrder = {
      "userDirectoryId",
      "users",
      "total",
      "sortBy",
      "sortDirection",
      "pageIndex",
      "pageSize",
      "filter"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class Users implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The optional filter that was applied to the users. */
  @Schema(description = "The optional filter that was applied to the users")
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

  /** The optional method used to sort the users e.g. by name. */
  @Schema(description = "The optional method used to sort the users e.g. by name")
  @JsonProperty
  @XmlElement(name = "SortBy")
  private UserSortBy sortBy;

  /** The optional sort direction that was applied to the users. */
  @Schema(description = "The optional sort direction that was applied to the users")
  @JsonProperty
  @XmlElement(name = "SortDirection")
  private SortDirection sortDirection;

  /** The total number of users. */
  @Schema(description = "The total number of users", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Total", required = true)
  private long total;

  /** The ID for the user directory the users are associated with. */
  @Schema(
      description = "The ID for the user directory the users are associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  private UUID userDirectoryId;

  /** The users. */
  @Schema(description = "The users", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElementWrapper(name = "Users", required = true)
  @XmlElement(name = "User", required = true)
  private List<User> users;

  /** Constructs a new <b>Users</b>. */
  public Users() {}

  /**
   * Constructs a new <b>Users</b>.
   *
   * @param userDirectoryId the ID for the user directory the users are associated with
   * @param users the users
   * @param total the total number of users
   * @param filter the optional filter that was applied to the users
   * @param sortBy the method used to sort the users e.g. by name
   * @param sortDirection the sort direction that was applied to the users
   * @param pageIndex the page index
   * @param pageSize the page size
   */
  public Users(
      UUID userDirectoryId,
      List<User> users,
      long total,
      String filter,
      UserSortBy sortBy,
      SortDirection sortDirection,
      int pageIndex,
      int pageSize) {
    this.userDirectoryId = userDirectoryId;
    this.users = users;
    this.total = total;
    this.filter = filter;
    this.sortBy = sortBy;
    this.sortDirection = sortDirection;
    this.pageIndex = pageIndex;
    this.pageSize = pageSize;
  }

  /**
   * Returns the optional filter that was applied to the users.
   *
   * @return the optional filter that was applied to the users
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
   * Returns the optional method used to sort the users e.g. by name.
   *
   * @return the optional method used to sort the users
   */
  public UserSortBy getSortBy() {
    return sortBy;
  }

  /**
   * Returns the sort direction that was applied to the users.
   *
   * @return the sort direction that was applied to the users
   */
  public SortDirection getSortDirection() {
    return sortDirection;
  }

  /**
   * Returns the total number of users.
   *
   * @return the total number of users
   */
  public long getTotal() {
    return total;
  }

  /**
   * Returns the ID for the user directory the users are associated with.
   *
   * @return the ID for the user directory the users are associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Returns the users.
   *
   * @return the users
   */
  public List<User> getUsers() {
    return users;
  }
}
