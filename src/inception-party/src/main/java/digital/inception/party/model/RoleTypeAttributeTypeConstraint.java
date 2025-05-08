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

package digital.inception.party.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.util.StringUtils;

/**
 * The {@code RoleTypeAttributeTypeConstraint} class holds the information for a constraint that
 * should be applied to an attribute for a party that is assigned a particular role.
 *
 * @author Marcus Portmann
 */
@Schema(
    description =
        "A constraint that should be applied to an attribute for a party that is assigned a particular role")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"roleType", "attributeType", "attributeTypeQualifier", "type", "value"})
@XmlRootElement(
    name = "RoleTypeAttributeTypeConstraint",
    namespace = "https://inception.digital/party")
@XmlType(
    name = "RoleTypeAttributeTypeConstraint",
    namespace = "https://inception.digital/party",
    propOrder = {"roleType", "attributeType", "attributeTypeQualifier", "type", "value"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_role_type_attribute_type_constraints")
@IdClass(RoleTypeAttributeTypeConstraintId.class)
public class RoleTypeAttributeTypeConstraint implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The code for the attribute type. */
  @Schema(
      description = "The code for the attribute type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "AttributeType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "attribute_type", length = 50, nullable = false)
  private String attributeType;

  /** The qualifier for the attribute type. */
  @Schema(
      description = "The qualifier for the attribute type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "AttributeTypeQualifier", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "attribute_type_qualifier", length = 50, nullable = false)
  private String attributeTypeQualifier;

  /** The code for the role type. */
  @Schema(description = "The code for the role type", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "RoleType", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "role_type", length = 50, nullable = false)
  private String roleType;

  /** The constraint type. */
  @Schema(
      description = "The constraint type",
      requiredMode = Schema.RequiredMode.REQUIRED,
      allowableValues = {"max_size", "min_size", "pattern", "reference", "required", "size"})
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Column(name = "type", length = 50, nullable = false)
  private ConstraintType type;

  /**
   * The value to apply when validating the attribute value, e.g. the length, the regular expression
   * pattern, etc.
   */
  @Schema(description = "The value to apply when validating the attribute value")
  @JsonProperty
  @XmlElement(name = "Value")
  @Size(max = 1000)
  @Column(name = "value", length = 1000)
  private String value;

  /**
   * Creates a new {@code RoleTypeAttributeTypeConstraint} instance.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the attribute type
   * @param type the constraint type
   * @param value the value to apply when validating the attribute value
   */
  public RoleTypeAttributeTypeConstraint(
      String roleType, String attributeType, ConstraintType type, String value) {
    this(roleType, attributeType, "", type, value);
  }

  /**
   * Creates a new {@code RoleTypeAttributeTypeConstraint} instance.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the attribute type
   * @param attributeTypeQualifier the qualifier for the attribute type
   * @param type the constraint type
   * @param value the value to apply when validating the attribute value
   */
  public RoleTypeAttributeTypeConstraint(
      String roleType,
      String attributeType,
      String attributeTypeQualifier,
      ConstraintType type,
      String value) {
    this.roleType = roleType;
    this.attributeType = attributeType;
    this.attributeTypeQualifier = attributeTypeQualifier;
    this.type = type;
    this.value = value;
  }

  /**
   * Creates a new {@code RoleTypeAttributeTypeConstraint} instance.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the attribute type
   * @param attributeTypeQualifier the qualifier for the attribute type
   * @param type the constraint type
   */
  public RoleTypeAttributeTypeConstraint(
      String roleType, String attributeType, String attributeTypeQualifier, ConstraintType type) {
    this(roleType, attributeType, attributeTypeQualifier, type, null);
  }

  /**
   * Creates a new {@code RoleTypeAttributeTypeConstraint} instance.
   *
   * @param roleType the code for the role type
   * @param attributeType the code for the attribute type
   * @param type the constraint type
   */
  public RoleTypeAttributeTypeConstraint(
      String roleType, String attributeType, ConstraintType type) {
    this(roleType, attributeType, "", type, null);
  }

  /** Creates a new {@code RoleTypeAttributeTypeConstraint} instance. */
  public RoleTypeAttributeTypeConstraint() {}

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    RoleTypeAttributeTypeConstraint other = (RoleTypeAttributeTypeConstraint) object;

    return Objects.equals(roleType, other.roleType)
        && Objects.equals(attributeType, other.attributeType)
        && Objects.equals(attributeTypeQualifier, other.attributeTypeQualifier)
        && Objects.equals(type, other.type);
  }

  /**
   * Returns the code for the attribute type.
   *
   * @return the code for the attribute type
   */
  public String getAttributeType() {
    return attributeType;
  }

  /**
   * Returns the qualifier for the attribute type.
   *
   * @return the qualifier for the attribute type
   */
  public String getAttributeTypeQualifier() {
    return attributeTypeQualifier;
  }

  /**
   * Returns the integer value to apply when validating the attribute value.
   *
   * @return the integer value to apply when validating the attribute value
   */
  @JsonIgnore
  @XmlTransient
  public int getIntegerValue() {
    if (!StringUtils.hasText(value)) {
      return 0;
    } else {
      return Integer.parseInt(value);
    }
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
   * Returns the constraint type.
   *
   * @return the constraint type
   */
  public ConstraintType getType() {
    return type;
  }

  /**
   * Returns the value to apply when validating the attribute value, e.g. the length, the regular
   * expression pattern, etc.
   *
   * @return the value to apply when validating the attribute value
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
        + ((attributeTypeQualifier == null) ? 0 : attributeTypeQualifier.hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the code for the attribute type.
   *
   * @param attributeType the code for the attribute type
   */
  public void setAttributeType(String attributeType) {
    this.attributeType = attributeType;
  }

  /**
   * Set the qualifier for the attribute type.
   *
   * @param attributeTypeQualifier the qualifier for the attribute type
   */
  public void setAttributeTypeQualifier(String attributeTypeQualifier) {
    this.attributeTypeQualifier = attributeTypeQualifier;
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
   * Set the constraint type.
   *
   * @param type the constraint type
   */
  public void setType(ConstraintType type) {
    this.type = type;
  }

  /**
   * Set the value to apply when validating the attribute value, e.g. the length, the regular
   * expression pattern, etc.
   *
   * @param value the value to apply when validating the attribute value
   */
  public void setValue(String value) {
    this.value = value;
  }
}
