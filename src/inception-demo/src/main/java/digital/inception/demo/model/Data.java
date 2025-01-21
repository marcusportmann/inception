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
import digital.inception.core.xml.LocalTimeAdapter;
import digital.inception.core.xml.OffsetDateTimeAdapter;
import digital.inception.core.xml.OffsetTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Objects;

/**
 * The <b>Data</b> class.
 *
 * @author Marcus Portmann
 */
@Schema(description = "Data")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "booleanValue",
  "dateValue",
  "decimalValue",
  "doubleValue",
  "floatValue",
  "integerValue",
  "stringValue",
  "timeValue",
  "timeWithTimeZoneValue",
  "timestampValue",
  "timestampWithTimeZoneValue",
  "country",
  "language"
})
@XmlRootElement(name = "Data", namespace = "https://inception.digital/demo")
@XmlType(
    name = "Data",
    namespace = "https://inception.digital/demo",
    propOrder = {
      "id",
      "booleanValue",
      "dateValue",
      "decimalValue",
      "doubleValue",
      "floatValue",
      "integerValue",
      "stringValue",
      "timeValue",
      "timeWithTimeZoneValue",
      "timestampValue",
      "timestampWithTimeZoneValue",
      "country",
      "language"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@NoScriptOrSQLInjection
@Entity
@Table(name = "demo_data")
@SuppressWarnings({"unused", "WeakerAccess"})
public class Data implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The boolean value for the data. */
  @Schema(description = "The boolean value for the data")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  @Column(name = "boolean_value")
  private boolean booleanValue;

  /** The ISO 3166-1 country code value for the data. */
  @Schema(description = "The ISO 3166-1 country code value for the data")
  @JsonProperty
  @XmlElement(name = "Country")
  @ValidISOCountryCode
  @Size(min = 2, max = 2)
  @Column(name = "country")
  private String country;

  /** The date value for the data. */
  @Schema(description = "The ISO 8601 format date value for the data")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @Column(name = "date_value")
  private LocalDate dateValue;

  /** The decimal value for the data. */
  @Schema(description = "The decimal value for the data")
  @JsonProperty
  @XmlElement(name = "DecimalValue")
  @Column(name = "decimal_value")
  private BigDecimal decimalValue;

  /** The double value for the data. */
  @Schema(description = "The double value for the data")
  @JsonProperty
  @XmlElement(name = "DoubleValue")
  @Column(name = "double_value")
  private Double doubleValue;

  /** The float value for the data. */
  @Schema(description = "The float value for the data")
  @JsonProperty
  @XmlElement(name = "FloatValue")
  @Column(name = "float_value")
  private Float floatValue;

  /** The ID for the data. */
  @Schema(description = "The ID for the data", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  /** The integer value for the data. */
  @Schema(description = "The integer value for the data")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  @Column(name = "integer_value")
  private Integer integerValue;

  /** The ISO 639-1 language code value for the data. */
  @Schema(description = "The ISO 639-1 language code value for the data")
  @JsonProperty
  @XmlElement(name = "Language")
  @ValidISOLanguageCode
  @Size(min = 2, max = 2)
  @Column(name = "language")
  private String language;

  /** The string value for the data. */
  @Schema(description = "The string value for the data")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 2000)
  @Column(name = "string_value")
  private String stringValue;

  /** The time value for the data. */
  @Schema(description = "The time value for the data")
  @JsonProperty
  @XmlElement(name = "TimeValue")
  @XmlJavaTypeAdapter(LocalTimeAdapter.class)
  @XmlSchemaType(name = "time")
  @Column(name = "time_value")
  private LocalTime timeValue;

  /** The time with time zone value for the data. */
  @Schema(description = "The time with time zone value for the data")
  @JsonProperty
  @XmlElement(name = "TimeWithTimeZoneValue")
  @XmlJavaTypeAdapter(OffsetTimeAdapter.class)
  @XmlSchemaType(name = "time")
  @Column(name = "time_with_time_zone_value")
  private OffsetTime timeWithTimeZoneValue;

  /** The timestamp value for the data. */
  @Schema(description = "The timestamp value for the data")
  @JsonProperty
  @XmlElement(name = "TimestampValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "timestamp_value")
  private LocalDateTime timestampValue;

  /** The timestamp with time zone value for the data. */
  @Schema(description = "The timestamp with time zone value for the data")
  @JsonProperty
  @XmlElement(name = "TimestampWithTimeZoneValue")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "timestamp_with_time_zone_value")
  private OffsetDateTime timestampWithTimeZoneValue;

  /** Constructs a new <b>Data</b>. */
  public Data() {}

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

    Data other = (Data) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the boolean value for the data.
   *
   * @return the boolean value for the data
   */
  public boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * Returns the ISO 3166-1 country code value for the data.
   *
   * @return the ISO 3166-1 country code value for the data
   */
  public String getCountry() {
    return country;
  }

  /**
   * Returns the date value for the data.
   *
   * @return the date value for the data
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the decimal value for the data.
   *
   * @return the decimal value for the data
   */
  public BigDecimal getDecimalValue() {
    return decimalValue;
  }

  /**
   * Returns the double value for the data.
   *
   * @return the double value for the data
   */
  public Double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Returns the float value for the data.
   *
   * @return the float value for the data
   */
  public Float getFloatValue() {
    return floatValue;
  }

  /**
   * Returns the ID for the data.
   *
   * @return the ID for the data
   */
  public long getId() {
    return id;
  }

  /**
   * Returns the integer value for the data.
   *
   * @return the integer value for the data
   */
  public Integer getIntegerValue() {
    return integerValue;
  }

  /**
   * Returns the ISO 639-1 language code value for the data.
   *
   * @return the ISO 639-1 language code value for the data
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Returns the string value for the data.
   *
   * @return the string value for the data
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Returns the time value for the data.
   *
   * @return the time value for the data
   */
  public LocalTime getTimeValue() {
    return timeValue;
  }

  /**
   * Returns the time with time zone value for the data.
   *
   * @return the time with time zone value for the data
   */
  public OffsetTime getTimeWithTimeZoneValue() {
    return timeWithTimeZoneValue;
  }

  /**
   * Returns the timestamp value for the data.
   *
   * @return the timestamp value for the data
   */
  public LocalDateTime getTimestampValue() {
    return timestampValue;
  }

  /**
   * Returns the timestamp with time zone value for the data.
   *
   * @return the timestamp with time zone value for the data
   */
  public OffsetDateTime getTimestampWithTimeZoneValue() {
    return timestampWithTimeZoneValue;
  }

  /**
   * Set the boolean value for the data.
   *
   * @param booleanValue the boolean value for the data
   */
  public void setBooleanValue(boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Set the ISO 3166-1 country code value for the data.
   *
   * @param country the ISO 3166-1 country code value for the data
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Set the date value for the data.
   *
   * @param dateValue the date value for the data
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the decimal value for the data.
   *
   * @param decimalValue the decimal value for the data
   */
  public void setDecimalValue(BigDecimal decimalValue) {
    this.decimalValue = decimalValue;
  }

  /**
   * Set the double value for the data.
   *
   * @param doubleValue the double value for the data
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the float value for the data.
   *
   * @param floatValue the float value for the data
   */
  public void setFloatValue(Float floatValue) {
    this.floatValue = floatValue;
  }

  /**
   * Set the ID for the data.
   *
   * @param id the ID for the data.
   */
  public void setId(@NotNull long id) {
    this.id = id;
  }

  /**
   * Set the integer value for the data.
   *
   * @param integerValue the integer value for the data
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Set the ISO 639-1 language code value for the data.
   *
   * @param language the ISO 639-1 language code value for the data
   */
  public void setLanguage(@NotNull @Size(min = 2, max = 2) String language) {
    this.language = language;
  }

  /**
   * Set the string value for the data.
   *
   * @param stringValue the string value for the data
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }

  /**
   * Set the time value for the data.
   *
   * @param timeValue the time value for the data
   */
  public void setTimeValue(LocalTime timeValue) {
    this.timeValue = timeValue;
  }

  /**
   * Set the time with time zone value for the data.
   *
   * @param timeWithTimeZoneValue the time with time zone value for the data
   */
  public void setTimeWithTimeZoneValue(OffsetTime timeWithTimeZoneValue) {
    this.timeWithTimeZoneValue = timeWithTimeZoneValue;
  }

  /**
   * Set the timestamp value for the data.
   *
   * @param timestampValue the timestamp value for the data
   */
  public void setTimestampValue(LocalDateTime timestampValue) {
    this.timestampValue = timestampValue;
  }

  /**
   * Set the timestamp with time zone value for the data.
   *
   * @param timestampWithTimeZoneValue the timestamp with time zone value for the data
   */
  public void setTimestampWithTimeZoneValue(OffsetDateTime timestampWithTimeZoneValue) {
    this.timestampWithTimeZoneValue = timestampWithTimeZoneValue;
  }

  /**
   * Returns a string representation of the data.
   *
   * @return a string representation of the data
   */
  @Override
  public String toString() {
    return "Data {id=\""
        + id
        + "\", booleanValue=\""
        + booleanValue
        + "\", dateValue=\""
        + dateValue
        + "\", decimalValue=\""
        + decimalValue
        + "\", doubleValue=\""
        + doubleValue
        + "\", floatValue=\""
        + floatValue
        + "\", integerValue=\""
        + integerValue
        + "\", stringValue=\""
        + stringValue
        + "\", timeValue=\""
        + timeValue
        + "\", timeWithTimeZoneValue=\""
        + timeWithTimeZoneValue
        + "\", timestampValue=\""
        + timestampValue
        + "\", timestampWithTimeZoneValue=\""
        + timestampWithTimeZoneValue
        + "\", country=\""
        + country
        + "\", language=\""
        + language
        + "\"}";
  }
}
