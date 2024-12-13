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

package digital.inception.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.validation.constraint.NoScriptOrSQLInjection;
import digital.inception.core.validation.constraint.ValidISOCountryCode;
import digital.inception.core.validation.constraint.ValidISOLanguageCode;
import digital.inception.core.xml.LocalDateAdapter;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSchemaType;
import jakarta.xml.bind.annotation.XmlType;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

/**
 * The <b>ReactiveData</b> class.
 *
 * @author Marcus Portmann
 */
@Schema(description = "ReactiveData")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "stringValue",
  "integerValue",
  "dateValue",
  "timestampValue",
  "country",
  "language"
})
@XmlRootElement(name = "ReactiveData", namespace = "https://inception.digital/demo")
@XmlType(
    name = "ReactiveData",
    namespace = "https://inception.digital/demo",
    propOrder = {
      "id",
      "stringValue",
      "integerValue",
      "dateValue",
      "timestampValue",
      "country",
      "language"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@NoScriptOrSQLInjection
@Table("demo_data")
@SuppressWarnings({"unused", "WeakerAccess"})
public class ReactiveData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ISO 3166-1 country code value for the reactive data. */
  @Schema(
      description = "The ISO 3166-1 country code value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Country")
  @NotNull
  @ValidISOCountryCode
  @Size(min = 2, max = 2)
  @Column("country")
  private String country;

  /** The date value for the reactive data. */
  @Schema(
      description = "The ISO 8601 format date value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @NotNull
  @Column("date_value")
  private LocalDate dateValue;

  /** The ID for the reactive data. */
  @Schema(description = "The ID for the reactive data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column("id")
  private long id;

  /** The integer value for the reactive data. */
  @Schema(
      description = "The integer value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "IntegerValue", required = true)
  @NotNull
  @Column("integer_value")
  private Integer integerValue;

  /** The ISO 639-1 language code value for the reactive data. */
  @Schema(
      description = "The ISO 639-1 language code value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Language")
  @NotNull
  @ValidISOLanguageCode
  @Size(min = 2, max = 2)
  @Column("language")
  private String language;

  /** The string value for the reactive data. */
  @Schema(
      description = "The string value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "StringValue")
  @NotNull
  @Size(min = 1, max = 2000)
  @Column("string_value")
  private String stringValue;

  /** The timestamp value for the reactive data. */
  @Schema(
      description = "The timestamp value for the reactive data",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "TimestampValue", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column("timestamp_value")
  private OffsetDateTime timestampValue;

  /** Constructs a new <b>ReactiveData</b>. */
  public ReactiveData() {}

  /**
   * Constructs a new <b>ReactiveData</b>.
   *
   * @param id the ID for the reactive data
   * @param integerValue the integer value for the reactive data
   * @param stringValue the string value for the reactive data
   * @param dateValue the date value for the reactive data
   * @param timestampValue the timestamp value for the reactive data
   * @param country the ISO 3166-1 country code value for the reactive data
   * @param language the ISO 639-1 language code value for the reactive data
   */
  public ReactiveData(
      long id,
      Integer integerValue,
      String stringValue,
      LocalDate dateValue,
      OffsetDateTime timestampValue,
      String country,
      String language) {
    this.id = id;
    this.integerValue = integerValue;
    this.stringValue = stringValue;
    this.dateValue = dateValue;
    this.timestampValue = timestampValue;
    this.country = country;
    this.language = language;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument, otherwise <b>false</b>
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

    ReactiveData other = (ReactiveData) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ISO 3166-1 country code value for the reactive data.
   *
   * @return the ISO 3166-1 country code value for the reactive data
   */
  public String getCountry() {
    return country;
  }

  /**
   * Returns the date value for the reactive data.
   *
   * @return the date value for the reactive data
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the ID for the reactive data.
   *
   * @return the ID for the reactive data
   */
  public long getId() {
    return id;
  }

  /**
   * Returns the integer value for the reactive data.
   *
   * @return the integer value for the reactive data
   */
  public Integer getIntegerValue() {
    return integerValue;
  }

  /**
   * Returns the ISO 639-1 language code value for the reactive data.
   *
   * @return the ISO 639-1 language code value for the reactive data
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the string value for the reactive data.
   *
   * @return the string value for the reactive data
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Returns the timestamp value for the reactive data.
   *
   * @return the timestamp value for the reactive data
   */
  public OffsetDateTime getTimestampValue() {
    return timestampValue;
  }

  /**
   * Set the ISO 3166-1 country code value for the reactive data.
   *
   * @param country the ISO 3166-1 country code value for the reactive data
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Set the date value for the reactive data.
   *
   * @param dateValue the date value for the reactive data
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the ID for the reactive data.
   *
   * @param id the ID for the reactive data.
   */
  public void setId(@NotNull long id) {
    this.id = id;
  }

  /**
   * Set the integer value for the reactive data.
   *
   * @param integerValue the integer value for the reactive data
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Set the ISO 639-1 language code value for the reactive data.
   *
   * @param language the ISO 639-1 language code value for the reactive data
   */
  public void setLanguage(@NotNull @Size(min = 2, max = 2) String language) {
    this.language = language;
  }

  /**
   * Set the string value for the reactive data.
   *
   * @param stringValue the string value for the reactive data
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the timestamp value for the reactive data.
   *
   * @param timestampValue the timestamp value for the reactive data
   */
  public void setTimestampValue(OffsetDateTime timestampValue) {
    this.timestampValue = timestampValue;
  }

  /**
   * Returns a string representation of the reactive data.
   *
   * @return a string representation of the reactive data
   */
  @Override
  public String toString() {
    return "ReactiveData {id=\""
        + id
        + "\", integerValue=\""
        + integerValue
        + "\", stringValue=\""
        + stringValue
        + "\", dateValue=\""
        + dateValue
        + "\", timestampValue=\""
        + timestampValue
        + "\"}";
  }
}
