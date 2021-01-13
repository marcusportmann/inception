/*
 * Copyright 2021 Marcus Portmann
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

// ~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

// ~--- JDK imports ------------------------------------------------------------

/**
 * The <code>GroupRole</code> class holds the information for a group role.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A group role association")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userDirectoryId", "groupName", "roleCode"})
@XmlRootElement(name = "GroupRole", namespace = "http://security.inception.digital")
@XmlType(
    name = "GroupRole",
    namespace = "http://security.inception.digital",
    propOrder = {"userDirectoryId", "groupName", "roleCode"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class GroupRole implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The name identifying the group. */
  @Schema(description = "The name identifying the group", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "GroupName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String groupName;

  /** The code uniquely identifying the role. */
  @Schema(description = "The code uniquely identifying the role", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleCode", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String roleCode;

  /**
   * The Universally Unique Identifier (UUID) uniquely identifying the user directory the group is
   * associated with.
   */
  @Schema(
      description =
          "The Universally Unique Identifier (UUID) uniquely identifying the user directory the "
              + "group is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  private UUID userDirectoryId;

  /** Constructs a new <code>GroupRole</code>. */
  public GroupRole() {}

  /**
   * Constructs a new <code>GroupRole</code>.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the group is associated with
   * @param groupName the name identifying the group
   * @param roleCode the code uniquely identifying the role
   */
  public GroupRole(UUID userDirectoryId, String groupName, String roleCode) {
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
    this.roleCode = roleCode;
  }

  /**
   * Returns the name identifying the group.
   *
   * @return the name identifying the group
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * Returns the code uniquely identifying the role.
   *
   * @return the code uniquely identifying the role
   */
  public String getRoleCode() {
    return roleCode;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   * group is associated with.
   *
   * @return the Universally Unique Identifier (UUID) uniquely identifying the user directory the
   *     group is associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Set the name identifying the group.
   *
   * @param groupName the name identifying the group
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Set the code uniquely identifying the role.
   *
   * @param roleCode the code uniquely identifying the role
   */
  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  /**
   * Set the Universally Unique Identifier (UUID) uniquely identifying the user directory the group
   * is associated with.
   *
   * @param userDirectoryId the Universally Unique Identifier (UUID) uniquely identifying the user
   *     directory the group is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }
}
