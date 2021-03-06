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

package digital.inception.party;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * The <b>RoleTypeAttributeConstraint</b> class holds the information for a constraint that should be
 * applied to an attribute for a party when the party is assigned a role type.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A constraint that should be applied to an attribute for a party when the party is assigned a role type")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"roleType", "attributeType", "type", "value"})
@XmlRootElement(name = "RoleTypeAttributeConstraint", namespace = "http://inception.digital/party")
@XmlType(
    name = "RoleTypeAttributeConstraint",
    namespace = "http://inception.digital/party",
    propOrder = {"roleType", "attributeType", "type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "role_type_attribute_constraints")
@IdClass(RoleTypeAttributeConstraintId.class)
public class RoleTypeAttributeConstraint implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The code for the standard or custom attribute type. */
  @Schema(description = "The code for the standard or custom attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "AttributeType", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "attribute_type", length = 30, nullable = false)
  private String attributeType;

  /** The code for the role type. */
  @Schema(description = "The code for the role type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleType", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "role_type", length = 30, nullable = false)
  private String roleType;

  /** The attribute constraint type. */
  @Schema(description = "The attribute constraint type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 30, nullable = false)
  private AttributeConstraintType type;

  /**
   * The optional value to apply when validating the attribute value, e.g. the length, the regular
   * expression pattern, etc.
   */
  @Schema(description = "The optional value to apply when validating the attribute value")
  @JsonProperty
  @XmlElement(name = "Value")
  @Size(max = 2000)
  @Column(name = "value", length = 1000)
  private String value;

  /**
   * Constructs a new <b>RoleTypeAttributeConstraint</b>.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the standard or custom attribute type
   * @param type the attribute constraint type
   * @param value the optional value to apply when validating the attribute value
   */
  public RoleTypeAttributeConstraint(
      String roleType, String attributeType, AttributeConstraintType type, String value) {
    this.roleType = roleType;
    this.attributeType = attributeType;
    this.type = type;
    this.value = value;
  }

  /**
   * Constructs a new <b>RoleTypeAttributeConstraint</b>.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the standard or custom attribute type
   * @param type the attribute constraint type
   */
  public RoleTypeAttributeConstraint(
      String roleType, String attributeType, AttributeConstraintType type) {
    this.roleType = roleType;
    this.attributeType = attributeType;
    this.type = type;
  }

  /** Constructs a new <b>RoleTypeAttributeConstraint</b>. */
  public RoleTypeAttributeConstraint() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    RoleTypeAttributeConstraint other = (RoleTypeAttributeConstraint) object;

    return Objects.equals(roleType, other.roleType)
        && Objects.equals(attributeType, other.attributeType)
        && Objects.equals(type, other.type);
  }

  /**
   * Returns the code for the standard or custom attribute type.
   *
   * @return the code for the standard or custom attribute type
   */
  public String getAttributeType() {
    return attributeType;
  }

  /**
   * Returns the code for the role type.
   *
   * @return the code for the role type
   */
  public String getRoleType() {
    return roleType;
  }

  /**
   * Returns the attribute constraint type.
   *
   * @return the attribute constraint type
   */
  public AttributeConstraintType getType() {
    return type;
  }

  /**
   * Returns the optional value to apply when validating the attribute value, e.g. the length, the
   * regular expression pattern, etc.
   *
   * @return the optional value to apply when validating the attribute value
   */
  public String getValue() {
    return value;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((roleType == null) ? 0 : roleType.hashCode())
        + ((attributeType == null) ? 0 : attributeType.hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the code for the standard or custom attribute type.
   *
   * @param attributeType the code for the standard or custom attribute type
   */
  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }

  /**
   * Set the code for the role type.
   *
   * @param roleType the code for the role type
   */
  public void setRoleType(String roleType) {
    this.roleType = roleType;
  }

  /**
   * Set the attribute constraint type.
   *
   * @param type the attribute constraint type
   */
  public void setType(AttributeConstraintType type) {
    this.type = type;
  }

  /**
   * Set the optional value to apply when validating the attribute value, e.g. the length, the
   * regular expression pattern, etc.
   *
   * @param value the optional value to apply when validating the attribute value
   */
  public void setValue(String value) {
    this.value = value;
  }
}
