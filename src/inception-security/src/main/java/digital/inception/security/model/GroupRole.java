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
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

/**
 * The {@code GroupRole} class holds the information for a group role.
 *
 * @author Marcus Portmann
 */
@Schema(description = "A group role association")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"userDirectoryId", "groupName", "roleCode"})
@XmlRootElement(name = "GroupRole", namespace = "https://inception.digital/security")
@XmlType(
    name = "GroupRole",
    namespace = "https://inception.digital/security",
    propOrder = {"userDirectoryId", "groupName", "roleCode"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused"})
public class GroupRole implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The name of the group. */
  @Schema(description = "The name of the group", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "GroupName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String groupName;

  /** The code for the role. */
  @Schema(description = "The code for the role", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleCode", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String roleCode;

  /** The ID for the user directory the group is associated with. */
  @Schema(
      description = "The ID for the user directory the " + "group is associated with",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  private UUID userDirectoryId;

  /** Creates a new {@code GroupRole} instance. */
  public GroupRole() {}

  /**
   * Creates a new {@code GroupRole} instance.
   *
   * @param userDirectoryId the ID for the user directory the group is associated with
   * @param groupName the name of the group
   * @param roleCode the code for the role
   */
  public GroupRole(UUID userDirectoryId, String groupName, String roleCode) {
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
    this.roleCode = roleCode;
  }

  /**
   * Returns the name of the group.
   *
   * @return the name of the group
   */
  public String getGroupName() {
    return groupName;
  }

  /**
   * Returns the code for the role.
   *
   * @return the code for the role
   */
  public String getRoleCode() {
    return roleCode;
  }

  /**
   * Returns the ID for the user directory the group is associated with.
   *
   * @return the ID for the user directory the group is associated with
   */
  public UUID getUserDirectoryId() {
    return userDirectoryId;
  }

  /**
   * Set the name of the group.
   *
   * @param groupName the name of the group
   */
  public void setGroupName(String groupName) {
    this.groupName = groupName;
  }

  /**
   * Set the code for the role.
   *
   * @param roleCode the code for the role
   */
  public void setRoleCode(String roleCode) {
    this.roleCode = roleCode;
  }

  /**
   * Set the ID for the user directory the group is associated with.
   *
   * @param userDirectoryId the ID for the user directory the group is associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId) {
    this.userDirectoryId = userDirectoryId;
  }
}
