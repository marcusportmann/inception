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

package digital.inception.operations.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.LocalDateAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
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

/**
 * The {@code VariableSearchCriteria} class holds the variable search criteria to apply when
 * searching for workflows.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The variable search criteria to apply when searching for workflows")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "name",
  "booleanValue",
  "dateValue",
  "decimalValue",
  "doubleValue",
  "integerValue",
  "stringValue"
})
@XmlRootElement(name = "VariableSearchCriteria", namespace = "https://inception.digital/operations")
@XmlType(
    name = "VariableSearchCriteria",
    namespace = "https://inception.digital/operations",
    propOrder = {
      "name",
      "booleanValue",
      "dateValue",
      "decimalValue",
      "doubleValue",
      "integerValue",
      "stringValue"
    })
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings({"unused", "WeakerAccess"})
public class VariableSearchCriteria implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The boolean value for the variable. */
  @Schema(description = "The boolean value for the variable")
  @JsonProperty
  @XmlElement(name = "BooleanValue")
  private Boolean booleanValue;

  /** The date value for the variable. */
  @Schema(description = "The ISO 8601 format date value for the variable")
  @JsonProperty
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "DateValue")
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  private LocalDate dateValue;

  /** The decimal value for the variable. */
  @Schema(description = "The decimal value for the variable")
  @JsonProperty
  @XmlElement(name = "DecimalValue")
  private BigDecimal decimalValue;

  /** The double value for the variable. */
  @Schema(description = "The double value for the variable")
  @JsonProperty
  @XmlElement(name = "DoubleValue")
  private Double doubleValue;

  /** The integer value for the variable. */
  @Schema(description = "The integer value for the variable")
  @JsonProperty
  @XmlElement(name = "IntegerValue")
  private Integer integerValue;

  /**
   * The name for the variable.
   *
   * <p>NOTE: The length of this attribute aligns with the length of variable names in Camunda and
   * Flowable.
   */
  @Schema(description = "The name for the variable")
  @JsonProperty
  @XmlElement(name = "Name")
  @Size(min = 1, max = 255)
  private String name;

  /** The string value for the variable. */
  @Schema(description = "The string value for the variable")
  @JsonProperty
  @XmlElement(name = "StringValue")
  @Size(min = 1, max = 4000)
  private String stringValue;

  /** Constructs a new {@code VariableSearchCriteria}. */
  public VariableSearchCriteria() {}

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param booleanValue the boolean value for the variable
   */
  public VariableSearchCriteria(String name, Boolean booleanValue) {
    this.name = name;
    this.booleanValue = booleanValue;
  }

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param dateValue the date value for the variable
   */
  public VariableSearchCriteria(String name, LocalDate dateValue) {
    this.name = name;
    this.dateValue = dateValue;
  }

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param decimalValue the decimal value for the variable
   */
  public VariableSearchCriteria(String name, BigDecimal decimalValue) {
    this.name = name;
    this.decimalValue = decimalValue;
  }

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param doubleValue the double value for the variable
   */
  public VariableSearchCriteria(String name, Double doubleValue) {
    this.name = name;
    this.doubleValue = doubleValue;
  }

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param integerValue the integer value for the variable
   */
  public VariableSearchCriteria(String name, Integer integerValue) {
    this.name = name;
    this.integerValue = integerValue;
  }

  /**
   * Constructs a new {@code VariableSearchCriteria}.
   *
   * @param name the name for the variable
   * @param stringValue the string value for the variable
   */
  public VariableSearchCriteria(String name, String stringValue) {
    this.name = name;
    this.stringValue = stringValue;
  }

  /**
   * Returns the boolean value for the variable.
   *
   * @return the boolean value for the variable
   */
  public Boolean getBooleanValue() {
    return booleanValue;
  }

  /**
   * Returns the date value for the variable.
   *
   * @return the date value for the variable
   */
  public LocalDate getDateValue() {
    return dateValue;
  }

  /**
   * Returns the decimal value for the variable.
   *
   * @return the decimal value for the variable
   */
  public BigDecimal getDecimalValue() {
    return decimalValue;
  }

  /**
   * Returns the double value for the variable.
   *
   * @return the double value for the variable
   */
  public Double getDoubleValue() {
    return doubleValue;
  }

  /**
   * Returns the integer value for the variable.
   *
   * @return the integer value for the variable
   */
  public Integer getIntegerValue() {
    return integerValue;
  }

  /**
   * Returns the name for the variable.
   *
   * @return the name for the variable
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the string value for the variable.
   *
   * @return the string value for the variable
   */
  public String getStringValue() {
    return stringValue;
  }

  /**
   * Set the boolean value for the variable.
   *
   * @param booleanValue the boolean value for the variable
   */
  public void setBooleanValue(Boolean booleanValue) {
    this.booleanValue = booleanValue;
  }

  /**
   * Set the date value for the variable.
   *
   * @param dateValue the date value for the variable
   */
  public void setDateValue(LocalDate dateValue) {
    this.dateValue = dateValue;
  }

  /**
   * Set the decimal value for the variable.
   *
   * @param decimalValue the decimal value for the variable
   */
  public void setDecimalValue(BigDecimal decimalValue) {
    this.decimalValue = decimalValue;
  }

  /**
   * Set the double value for the variable.
   *
   * @param doubleValue the double value for the variable
   */
  public void setDoubleValue(Double doubleValue) {
    this.doubleValue = doubleValue;
  }

  /**
   * Set the integer value for the variable.
   *
   * @param integerValue the integer value for the variable
   */
  public void setIntegerValue(Integer integerValue) {
    this.integerValue = integerValue;
  }

  /**
   * Set the name for the variable
   *
   * @param name the name for the variable
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the string value for the variable.
   *
   * @param stringValue the string value for the variable
   */
  public void setStringValue(String stringValue) {
    this.stringValue = stringValue;
  }
}
