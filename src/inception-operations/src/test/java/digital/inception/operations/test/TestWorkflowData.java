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

package digital.inception.operations.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * The {@code TestWorkflow} class holds the data for a test workflow.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The test workflow data")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "name", "date", "number", "timestamp"})
@XmlRootElement(name = "TestWorkflow", namespace = "https://inception.digital/operations")
@XmlType(
    name = "TestWorkflow",
    namespace = "https://inception.digital/operations",
    propOrder = {"id", "name", "date", "number", "timestamp"})
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({"unused", "WeakerAccess"})
public class TestWorkflowData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The date. */
  @Schema(description = "The ISO 8601 format date", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
  @XmlElement(name = "Date", required = true)
  @XmlJavaTypeAdapter(LocalDateAdapter.class)
  @XmlSchemaType(name = "date")
  @NotNull
  private LocalDate date;

  /** The ID. */
  @Schema(description = "The ID", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private UUID id;

  /** The name. */
  @Schema(description = "The name", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  private String name;

  /** The number. */
  @Schema(description = "The number", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Number", required = true)
  @NotNull
  private BigDecimal number;

  /** The timestamp. */
  @Schema(description = "The timestamp", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Timestamp", required = true)
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private OffsetDateTime timestamp;

  /** Creates a new {@code TestWorkflow} instance. */
  public TestWorkflowData() {}

  /**
   * Creates a new {@code TestWorkflow} instance.
   *
   * @param id the ID
   * @param name the name
   * @param date the date
   * @param number the number
   * @param timestamp the timestamp
   */
  public TestWorkflowData(
      UUID id, String name, LocalDate date, BigDecimal number, OffsetDateTime timestamp) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.number = number;
    this.timestamp = timestamp;
  }

  /**
   * Returns the date.
   *
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Returns the ID.
   *
   * @return the ID
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the number.
   *
   * @return the number
   */
  public BigDecimal getNumber() {
    return number;
  }

  /**
   * Returns the timestamp.
   *
   * @return the timestamp
   */
  public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  /**
   * Set the date.
   *
   * @param date the date
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Set the ID.
   *
   * @param id the ID
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the name.
   *
   * @param name the name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the number.
   *
   * @param number the number
   */
  public void setNumber(BigDecimal number) {
    this.number = number;
  }

  /**
   * Set the timestamp.
   *
   * @param timestamp the timestamp
   */
  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }
}
