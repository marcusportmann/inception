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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;

/**
 * The <b>RelationshipProperty</b> class holds the information for a relationship property for a
 * relationship.
 *
 * @author Marcus Portmann
 */
@Schema(name = "RelationshipProperty", description = "A relationship property for a relationship")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "type",
  "booleanValue",
  "dateValue",
  "decimalValue",
  "doubleValue",
  "integerValue",
  "stringValue"
})
@XmlRootElement(name = "RelationshipProperty", namespace = "http://inception.digital/party")
@XmlType(
    name = "RelationshipProperty",
    namespace = "http://inception.digital/party",
    propOrder = {
      "type",
      "booleanValue",
      "dateValue",
      "decimalValue",
      "doubleValue",
      "integerValue",
      "stringValue"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "relationship propertys")
@IdClass(RelationshipPropertyId.class)
public class RelationshipProperty implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The boolean value for the relationship property. */
  @Schema(description = "The boolean value for the relationship property")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  @Column(name = "boolean_value")
  private Boolean booleanValue;

  /** The date and time the relationship property was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date value for the relationship property. */
  @Schema(description = "The date value for the relationship property")
  @JsonProperty
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_value")
  private LocalDate dateValue;

  /** The decimal value for the relationship property. */
  @Schema(description = "The decimal value for the relationship property")
  @JsonProperty
  @XmlElement(name = "DecimalValue")
  @Column(name = "decimal_value")
  private BigDecimal decimalValue;

  /** The double value for the relationship property. */
  @Schema(description = "The double value for the relationship property")
  @JsonProperty
  @XmlElement(name = "DoubleValue")
  @Column(name = "double_value")
  private Double doubleValue;

  /** The integer value for the relationship property. */
  @Schema(description = "The integer value for the relationship property")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  @Column(name = "integer_value")
  private Integer integerValue;

  /** The relationship the relationship property is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("relationshipPropertyReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "relationship_id")
  private Relationship relationship;

  /** The string value for the relationship property. */
  @Schema(description = "The string value for the relationship property")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 200)
  @Column(name = "string_value", length = 200)
  private String stringValue;

  /** The code for the relationship property type. */
  @Schema(description = "The code for the relationship property type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the relationship property was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>RelationshipProperty</b>. */
  public RelationshipProperty() {}

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   */
  public RelationshipProperty(String type) {
    this.type = type;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param stringValue the string value for the relationship property
   */
  public RelationshipProperty(String type, String stringValue) {
    this.type = type;
    this.stringValue = stringValue;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param booleanValue the boolean value for the relationship property
   */
  public RelationshipProperty(String type, boolean booleanValue) {
    this.type = type;
    this.booleanValue = booleanValue;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param doubleValue the double value for the relationship property
   */
  public RelationshipProperty(String type, double doubleValue) {
    this.type = type;
    this.doubleValue = doubleValue;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param dateValue the date value for the relationship property
   */
  public RelationshipProperty(String type, LocalDate dateValue) {
    this.type = type;
    this.dateValue = dateValue;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param decimalValue the decimal value for the relationship property
   */
  public RelationshipProperty(String type, BigDecimal decimalValue) {
    this.type = type;
    this.decimalValue = decimalValue;
  }

  /**
   * Constructs a new <b>RelationshipProperty</b>.
   *
   * @param type the relationship property type
   * @param integerValue the integer value for the relationship property
   */
  public RelationshipProperty(String type, Integer integerValue) {
    this.type = type;
    this.integerValue = integerValue;
  }

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

    RelationshipProperty other = (RelationshipProperty) object;

    return Objects.equals(relationship, other.relationship) && Objects.equals(type, other.type);
  }

  /**
   * Returns the boolean value for the relationship property.
   *
   * @return the boolean value for the relationship property
   */
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * Returns the date and time the relationship property was created.
   *
   * @return the date and time the relationship property was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the date value for the relationship property.
   *
   * @return the date value for the relationship property
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the decimal value for the relationship property.
   *
   * @return the decimal value for the relationship property
   */
  public BigDecimal getDecimalValue() {
    return decimalValue;
  }

  /**
   * Returns the double value for the relationship property.
   *
   * @return the double value for the relationship property
   */
  public Double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Returns the integer value for the relationship property.
   *
   * @return the integer value for the relationship property
   */
  public Integer getIntegerValue() {
    return integerValue;
  }

  /**
   * Returns the relationship the relationship property is associated with.
   *
   * @return the relationship the relationship property is associated with
   */
  @Schema(hidden = true)
  public Relationship getRelationship() {
    return relationship;
  }

  /**
   * Returns the string value for the relationship property.
   *
   * @return the string value for the relationship property
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Returns the code for the relationship property type.
   *
   * @return the code for the relationship property type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the date and time the relationship property was last updated.
   *
   * @return the date and time the relationship property was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns whether the relationship property has a valid value.
   *
   * @param valueType the value type
   * @return <b>true</b> if the relationship property has a valid value or <b>false</b> otherwise
   */
  public boolean hasValue(ValueType valueType) {
    return (((valueType == ValueType.STRING) && StringUtils.hasText(stringValue))
        || ((valueType == ValueType.BOOLEAN) && (booleanValue != null))
        || ((valueType == ValueType.DATE) && (dateValue != null))
        || ((valueType == ValueType.DECIMAL) && (decimalValue != null))
        || ((valueType == ValueType.DOUBLE) && (doubleValue != null))
        || ((valueType == ValueType.INTEGER) && (integerValue != null)));
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((relationship == null) || (relationship.getId() == null))
            ? 0
            : relationship.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the boolean value for the relationship property.
   *
   * @param booleanValue the boolean value for the relationship property
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Set the date value for the relationship property.
   *
   * @param dateValue the date value for the relationship property
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the decimal value for the relationship property.
   *
   * @param decimalValue the decimal value for the relationship property
   */
  public void setDecimalValue(BigDecimal decimalValue) {
    this.decimalValue = decimalValue;
  }

  /**
   * Set the decimal value for the relationship property.
   *
   * @param decimalValue the decimal value for the relationship property
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(String decimalValue) {
    this.decimalValue = new BigDecimal(decimalValue);
  }

  /**
   * Set the decimal value for the relationship property.
   *
   * @param decimalValue the decimal value for the relationship property
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(long decimalValue) {
    this.decimalValue = BigDecimal.valueOf(decimalValue);
  }

  /**
   * Set the decimal value for the relationship property.
   *
   * @param decimalValue the decimal value for the relationship property
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(double decimalValue) {
    this.decimalValue = BigDecimal.valueOf(decimalValue);
  }

  /**
   * Set the double value for the relationship property.
   *
   * @param doubleValue the double value for the relationship property
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the integer value for the relationship property.
   *
   * @param integerValue the integer value for the relationship property
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Set the relationship the relationship property is associated with.
   *
   * @param relationship the relationship the relationship property is associated with
   */
  @Schema(hidden = true)
  public void setRelationship(Relationship relationship) {
    this.relationship = relationship;
  }

  /**
   * Set the string value for the relationship property.
   *
   * @param stringValue the string value for the relationship property
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the code for the relationship property type.
   *
   * @param type the code for the relationship property type
   */
  public void setType(String type) {
    this.type = type;
  }
}
