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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.Unmarshaller;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlTransient;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;
import org.springframework.util.StringUtils;

/**
 * The {@code Attribute} class holds the information for an attribute for an organization or person.
 *
 * @author Marcus Portmann
 */
@Schema(name = "Attribute", description = "An attribute for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "type",
  "booleanValue",
  "dateValue",
  "decimalValue",
  "doubleValue",
  "integerValue",
  "stringValue",
  "unit"
})
@XmlRootElement(name = "Attribute", namespace = "https://inception.digital/party")
@XmlType(
    name = "Attribute",
    namespace = "https://inception.digital/party",
    propOrder = {
      "type",
      "booleanValue",
      "dateValue",
      "decimalValue",
      "doubleValue",
      "integerValue",
      "stringValue",
      "unit"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "party_attributes")
@IdClass(AttributeId.class)
@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public class Attribute implements Serializable {

  /** The attribute type codes reserved for standard attributes. */
  public static final String[] RESERVED_ATTRIBUTE_TYPE_CODES = {
    "attribute",
    "attributes",
    "consent",
    "consents",
    "contact_mechanism",
    "contact_mechanisms",
    "country_of_citizenship",
    "countries_of_citizenship",
    "country_of_tax_residence",
    "countries_of_tax_residence",
    "country_of_birth",
    "country_of_residence",
    "date_of_birth",
    "date_of_death",
    "education",
    "educations",
    "employment_status",
    "employment_type",
    "employment",
    "employments",
    "externalReference",
    "externalReferences",
    "gender",
    "given_name",
    "highest_qualification_type",
    "id",
    "identification_type",
    "identification_number",
    "identification_issue_date",
    "identification_expiry_date",
    "identification_country_of_issue",
    "identification",
    "identifications",
    "industry_allocation",
    "industry_allocations",
    "initials",
    "language",
    "language_proficiency",
    "language_proficiencies",
    "lock",
    "locks",
    "maiden_name",
    "marital_status",
    "marital_status_date",
    "marriage_type",
    "measurement_system",
    "middle_names",
    "name",
    "nextOfKin",
    "occupation",
    "physical_address",
    "physical_addresses",
    "preference",
    "preferences",
    "preferred_name",
    "race",
    "residence_permit",
    "residence_permits",
    "residency_status",
    "residential_type",
    "role",
    "roles",
    "segment_allocation",
    "segment_allocations",
    "skill",
    "skills",
    "source_of_funds",
    "sources_of_funds",
    "source_of_wealth",
    "sources_of_wealth",
    "status",
    "statuses",
    "surname",
    "tax_number",
    "tax_numbers",
    "tenant_id",
    "time_zone",
    "title"
  };

  @Serial private static final long serialVersionUID = 1000000;

  /** The boolean value for the attribute. */
  @Schema(description = "The boolean value for the attribute")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  @Column(name = "boolean_value")
  private Boolean booleanValue;

  /** The date value for the attribute. */
  @Schema(description = "The ISO 8601 format date value for the attribute")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_value")
  private LocalDate dateValue;

  /** The decimal value for the attribute. */
  @Schema(description = "The decimal value for the attribute")
  @JsonProperty
  @XmlElement(name = "DecimalValue")
  @Column(name = "decimal_value")
  private BigDecimal decimalValue;

  /** The double value for the attribute. */
  @Schema(description = "The double value for the attribute")
  @JsonProperty
  @XmlElement(name = "DoubleValue")
  @Column(name = "double_value")
  private Double doubleValue;

  /** The integer value for the attribute. */
  @Schema(description = "The integer value for the attribute")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  @Column(name = "integer_value")
  private Integer integerValue;

  /** The ID for the party the attribute is associated with. */
  @Schema(hidden = true)
  @JsonIgnore
  @XmlTransient
  @Id
  @Column(name = "party_id", nullable = false)
  private UUID partyId;

  /** The string value for the attribute. */
  @Schema(description = "The string value for the attribute")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 200)
  @Column(name = "string_value", length = 200)
  private String stringValue;

  /** The code for the attribute type. */
  @Schema(
      description = "The code for the attribute type",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 50)
  @Id
  @Column(name = "type", length = 50, nullable = false)
  private String type;

  /** The measurement unit for the attribute. */
  @Schema(description = "The measurement unit for the attribute")
  @JsonProperty
  @XmlElement(name = "Unit")
  @Column(name = "unit", length = 50)
  private MeasurementUnit unit;

  /** Constructs a new {@code Attribute}. */
  public Attribute() {}

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   */
  public Attribute(String type) {
    this.type = type;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param stringValue the string value for the attribute
   */
  public Attribute(String type, String stringValue) {
    this.type = type;
    this.stringValue = stringValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param booleanValue the boolean value for the attribute
   */
  public Attribute(String type, boolean booleanValue) {
    this.type = type;
    this.booleanValue = booleanValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param doubleValue the double value for the attribute
   */
  public Attribute(String type, double doubleValue) {
    this.type = type;
    this.doubleValue = doubleValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param dateValue the date value for the attribute
   */
  public Attribute(String type, LocalDate dateValue) {
    this.type = type;
    this.dateValue = dateValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param decimalValue the decimal value for the attribute
   */
  public Attribute(String type, BigDecimal decimalValue) {
    this.type = type;
    this.decimalValue = decimalValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param decimalValue the decimal value for the attribute
   * @param unit the measurement unit for the attribute
   */
  public Attribute(String type, BigDecimal decimalValue, MeasurementUnit unit) {
    this.type = type;
    this.decimalValue = decimalValue;
    this.unit = unit;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param integerValue the integer value for the attribute
   */
  public Attribute(String type, Integer integerValue) {
    this.type = type;
    this.integerValue = integerValue;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param integerValue the integer value for the attribute
   * @param unit the measurement unit for the attribute
   */
  public Attribute(String type, Integer integerValue, MeasurementUnit unit) {
    this.type = type;
    this.integerValue = integerValue;
    this.unit = unit;
  }

  /**
   * Constructs a new {@code Attribute}.
   *
   * @param type the attribute type
   * @param doubleValue the double value for the attribute
   * @param unit the measurement unit for the attribute
   */
  public Attribute(String type, double doubleValue, MeasurementUnit unit) {
    this.type = type;
    this.doubleValue = doubleValue;
    this.unit = unit;
  }

  /**
   * Returns the name of the attribute for the specified value type.
   *
   * @param valueType the value type
   * @return the name of the attribute
   */
  public static String getAttributeNameForValueType(ValueType valueType) {
    return switch (valueType) {
      case BOOLEAN -> "booleanValue";
      case DATE -> "dateValue";
      case DECIMAL -> "decimalValue";
      case DOUBLE -> "doubleValue";
      case INTEGER -> "integerValue";
      case STRING -> "stringValue";
      default -> "unknown";
    };
  }

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

    Attribute other = (Attribute) object;

    return Objects.equals(partyId, other.partyId) && Objects.equals(type, other.type);
  }

  /**
   * Returns the boolean value for the attribute.
   *
   * @return the boolean value for the attribute
   */
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * Returns the date value for the attribute.
   *
   * @return the date value for the attribute
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the decimal value for the attribute.
   *
   * @return the decimal value for the attribute
   */
  public BigDecimal getDecimalValue() {
    return decimalValue;
  }

  /**
   * Returns the double value for the attribute.
   *
   * @return the double value for the attribute
   */
  public Double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Returns the integer value for the attribute.
   *
   * @return the integer value for the attribute
   */
  public Integer getIntegerValue() {
    return integerValue;
  }

  /**
   * Returns the string value for the attribute.
   *
   * @return the string value for the attribute
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Returns the code for the attribute type.
   *
   * @return the code for the attribute type
   */
  public String getType() {
    return type;
  }

  /**
   * Returns the measurement unit for the attribute.
   *
   * @return the measurement unit for the attribute
   */
  public MeasurementUnit getUnit() {
    return unit;
  }

  /**
   * Returns whether the attribute has a valid value.
   *
   * @param valueType the value type
   * @return {@code true} if the attribute has a valid value or {@code false} otherwise
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
    return ((partyId == null) ? 0 : partyId.hashCode()) + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Sets the boolean value for the attribute.
   *
   * @param booleanValue the boolean value for the attribute
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Sets the date value for the attribute.
   *
   * @param dateValue the date value for the attribute
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Sets the decimal value for the attribute.
   *
   * @param decimalValue the decimal value for the attribute
   */
  public void setDecimalValue(BigDecimal decimalValue) {
    this.decimalValue = decimalValue;
  }

  /**
   * Sets the decimal value for the attribute.
   *
   * @param decimalValue the decimal value for the attribute
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(String decimalValue) {
    this.decimalValue = new BigDecimal(decimalValue);
  }

  /**
   * Sets the decimal value for the attribute.
   *
   * @param decimalValue the decimal value for the attribute
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(long decimalValue) {
    this.decimalValue = BigDecimal.valueOf(decimalValue);
  }

  /**
   * Sets the decimal value for the attribute.
   *
   * @param decimalValue the decimal value for the attribute
   */
  @JsonIgnore
  @XmlTransient
  public void setDecimalValue(double decimalValue) {
    this.decimalValue = BigDecimal.valueOf(decimalValue);
  }

  /**
   * Sets the double value for the attribute.
   *
   * @param doubleValue the double value for the attribute
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Sets the integer value for the attribute.
   *
   * @param integerValue the integer value for the attribute
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Sets the party the attribute is associated with.
   *
   * @param party the party the attribute is associated with
   */
  @JsonBackReference("attributeReference")
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    if (party != null) {
      this.partyId = party.getId();
    } else {
      this.partyId = null;
    }
  }

  /**
   * Sets the string value for the attribute.
   *
   * @param stringValue the string value for the attribute
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Sets the code for the attribute type.
   *
   * @param type the code for the attribute type
   */
  public void setType(String type) {
    this.type = type;
  }

  /**
   * Sets the measurement unit for the attribute.
   *
   * @param unit the measurement unit for the attribute
   */
  public void setUnit(MeasurementUnit unit) {
    this.unit = unit;
  }

  /**
   * Called by the JAXB runtime when an instance of this class has been completely unmarshalled, but
   * before it is added to its parent.
   *
   * @param unmarshaller the JAXB unmarshaller
   * @param parentObject the parent object
   */
  @SuppressWarnings("unused")
  private void afterUnmarshal(Unmarshaller unmarshaller, Object parentObject) {
    if (parentObject instanceof PartyBase parent) {
      setParty(parent);
    }
  }
}
