/*
 * Copyright 2019 Marcus Portmann
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

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>GroupMember</code> class holds the information for a group member.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "GroupMember")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "userDirectoryId", "groupName", "memberName", "memberType" })
@XmlRootElement(name = "GroupMember", namespace = "http://security.inception.digital")
@XmlType(name = "GroupMember", namespace = "http://security.inception.digital",
    propOrder = { "userDirectoryId", "groupName", "memberName", "memberType" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused" })
public class GroupMember
{
  private static final long serialVersionUID = 1000000;

  /**
   * The name identifying the group.
   */
  @ApiModelProperty(value = "The name identifying the group", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "GroupName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String groupName;

  /**
   * The name identifying the group member.
   */
  @ApiModelProperty(value = "The name identifying the group member", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MemberName", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String memberName;

  /**
   * The group member type.
   */
  @ApiModelProperty(value = "The group member type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MemberType", required = true)
  @NotNull
  private GroupMemberType memberType;

  /**
   * The ID used to uniquely identify the user directory the group is associated with.
   */
  @ApiModelProperty(
      value = "The ID used to uniquely identify the user directory the group is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "UserDirectoryId", required = true)
  @NotNull
  private UUID userDirectoryId;

  /**
   * Constructs a new <code>GroupMember</code>.
   */
  public GroupMember() {}

  /**
   * Constructs a new <code>GroupMember</code>.
   *
   * @param userDirectoryId the ID used to uniquely identify the user directory the group is
   *                        associated with
   * @param groupName       the name identifying the group
   * @param memberType      the group member type
   * @param memberName      the name identifying the group member
   */
  public GroupMember(UUID userDirectoryId, String groupName, GroupMemberType memberType,
      String memberName)
  {
    this.userDirectoryId = userDirectoryId;
    this.groupName = groupName;
    this.memberType = memberType;
    this.memberName = memberName;
  }

  /**
   * Returns the name identifying the group.
   *
   * @return the name identifying the group
   */
  public String getGroupName()
  {
    return groupName;
  }

  /**
   * Returns the name identifying the group member.
   *
   * @return the name identifying the group member
   */
  public String getMemberName()
  {
    return memberName;
  }

  /**
   * Returns the group member type.
   *
   * @return the group member type
   */
  public GroupMemberType getMemberType()
  {
    return memberType;
  }

  /**
   * Returns the ID used to uniquely identify the user directory the group is associated with.
   *
   * @return the ID used to uniquely identify the user directory the group is associated with
   */
  public UUID getUserDirectoryId()
  {
    return userDirectoryId;
  }

  /**
   * Set the name identifying the group.
   *
   * @param groupName the name identifying the group
   */
  public void setGroupName(String groupName)
  {
    this.groupName = groupName;
  }

  /**
   * Set the name identifying the group member.
   *
   * @param memberName the name identifying the group member
   */
  public void setMemberName(String memberName)
  {
    this.memberName = memberName;
  }

  /**
   * Set the group member type.
   *
   * @param memberType the group member type
   */
  public void setMemberType(GroupMemberType memberType)
  {
    this.memberType = memberType;
  }

  /**
   * Set the ID used to uniquely identify the user directory the group is associated with.
   *
   * @param userDirectoryId the ID used to uniquely identify the user directory the group is
   *                        associated with
   */
  public void setUserDirectoryId(UUID userDirectoryId)
  {
    this.userDirectoryId = userDirectoryId;
  }
}