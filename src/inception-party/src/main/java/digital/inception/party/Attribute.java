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
 * The <b>Attribute</b> class holds the information for an attribute for an organization or person.
 *
 * @author Marcus Portmann
 */
@Schema(name = "Attribute", description = "An attribute for an organization or person")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "type",
  "booleanValue",
  "dateValue",
  "doubleValue",
  "integerValue",
  "stringValue"
})
@XmlRootElement(name = "Attribute", namespace = "http://inception.digital/party")
@XmlType(
    name = "Attribute",
    namespace = "http://inception.digital/party",
    propOrder = {"type", "booleanValue", "dateValue", "doubleValue", "integerValue", "stringValue"})
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "party", name = "attributes")
@IdClass(AttributeId.class)
public class Attribute implements Serializable {

  /** The attribute type codes reserved for standard attributes. */
  public static final String[] RESERVED_ATTRIBUTE_TYPE_CODES = {
    "attributes",
    "consents",
    "contact_mechanisms",
    "countries_of_citizenship",
    "countries_of_tax_residence",
    "country_of_birth",
    "country_of_residence",
    "date_of_birth",
    "date_of_death",
    "employment_status",
    "employment_type",
    "gender",
    "given_name",
    "id",
    "identity_documents",
    "initials",
    "language",
    "locks",
    "maiden_name",
    "marital_status",
    "marriage_type",
    "middle_names",
    "name",
    "occupation",
    "physical_addresses",
    "preferences",
    "preferred_name",
    "race",
    "residence_permits",
    "residency_status",
    "residential_type",
    "roles",
    "sources_of_funds",
    "sources_of_wealth",
    "statuses",
    "surname",
    "tax_numbers",
    "tenant_id",
    "title"
  };

  private static final long serialVersionUID = 1000000;

  /** The boolean value for the attribute. */
  @Schema(description = "The boolean value for the attribute")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  @Column(name = "boolean_value")
  private Boolean booleanValue;

  /** The date and time the attribute was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The date value for the attribute. */
  @Schema(description = "The date value for the attribute")
  @JsonProperty
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_value")
  private LocalDate dateValue;

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

  /** The party the attribute is associated with. */
  @Schema(hidden = true)
  @JsonBackReference("attributeReference")
  @XmlTransient
  @Id
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "party_id")
  private PartyBase party;

  /** The string value for the attribute. */
  @Schema(description = "The string value for the attribute")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 200)
  @Column(name = "string_value", length = 200)
  private String stringValue;

  /** The code for the attribute type. */
  @Schema(description = "The code for the attribute type", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Type", required = true)
  @NotNull
  @Size(min = 1, max = 30)
  @Id
  @Column(name = "type", length = 30, nullable = false)
  private String type;

  /** The date and time the attribute was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>Attribute</b>. */
  public Attribute() {}

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   */
  public Attribute(String type) {
    this.type = type;
  }

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   * @param stringValue the string value for the attribute
   */
  public Attribute(String type, String stringValue) {
    this.type = type;
    this.stringValue = stringValue;
  }

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   * @param booleanValue the boolean value for the attribute
   */
  public Attribute(String type, boolean booleanValue) {
    this.type = type;
    this.booleanValue = booleanValue;
  }

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   * @param doubleValue the double value for the attribute
   */
  public Attribute(String type, double doubleValue) {
    this.type = type;
    this.doubleValue = doubleValue;
  }

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   * @param dateValue the date value for the attribute
   */
  public Attribute(String type, LocalDate dateValue) {
    this.type = type;
    this.dateValue = dateValue;
  }

  /**
   * Constructs a new <b>Attribute</b>.
   *
   * @param type the attribute type
   * @param integerValue the integer value for the attribute
   */
  public Attribute(String type, Integer integerValue) {
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

    Attribute other = (Attribute) object;

    return Objects.equals(party, other.party) && Objects.equals(type, other.type);
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
   * Returns the date and time the attribute was created.
   *
   * @return the date and time the attribute was created
   */
  public LocalDateTime getCreated() {
    return created;
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
   * Returns the party the attribute is associated with.
   *
   * @return the party the attribute is associated with
   */
  @Schema(hidden = true)
  public PartyBase getParty() {
    return party;
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
   * Returns the date and time the attribute was last updated.
   *
   * @return the date and time the attribute was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
  }

  /**
   * Returns whether the attribute has a valid value.
   *
   * @return <b>true</b> if the attribute has a valid value or <b>false</b> otherwise
   */
  public boolean hasValue() {
    return StringUtils.hasText(stringValue)
        || (booleanValue != null)
        || (dateValue != null)
        || (doubleValue != null)
        || (integerValue != null);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (((party == null) || (party.getId() == null)) ? 0 : party.getId().hashCode())
        + ((type == null) ? 0 : type.hashCode());
  }

  /**
   * Set the boolean value for the attribute.
   *
   * @param booleanValue the boolean value for the attribute
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Set the date value for the attribute.
   *
   * @param dateValue the date value for the attribute
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the double value for the attribute.
   *
   * @param doubleValue the double value for the attribute
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the integer value for the attribute.
   *
   * @param integerValue the integer value for the attribute
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Set the party the attribute is associated with.
   *
   * @param party the party the attribute is associated with
   */
  @Schema(hidden = true)
  public void setParty(PartyBase party) {
    this.party = party;
  }

  /**
   * Set the string value for the attribute.
   *
   * @param stringValue the string value for the attribute
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the code for the attribute type.
   *
   * @param type the code for the attribute type
   */
  public void setType(String type) {
    this.type = type;
  }
}
