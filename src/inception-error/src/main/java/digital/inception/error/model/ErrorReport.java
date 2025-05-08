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

package digital.inception.error.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import digital.inception.core.xml.OffsetDateTimeAdapter;
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
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * The {@code ErrorReport} class holds the information for an error report.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An error report submitted by a user after experiencing an application error")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "applicationId",
  "applicationVersion",
  "description",
  "detail",
  "created",
  "who",
  "deviceId",
  "feedback",
  "data"
})
@XmlRootElement(name = "ErrorReport", namespace = "https://inception.digital/error")
@XmlType(
    name = "ErrorReport",
    namespace = "https://inception.digital/error",
    propOrder = {
      "id",
      "applicationId",
      "applicationVersion",
      "description",
      "detail",
      "created",
      "who",
      "deviceId",
      "feedback",
      "data"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(name = "error_error_reports")
@SuppressWarnings({"unused", "WeakerAccess"})
public class ErrorReport implements Serializable {

  /** The maximum size of the error report detail. */
  public static final int MAX_DETAIL_SIZE = 1024000;

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID for the application that generated the error report. */
  @Schema(
      description = "ID for the application that generated the error report",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ApplicationId", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "application_id", length = 200, nullable = false)
  private String applicationId;

  /** The version of the application that generated the error report. */
  @Schema(
      description = "The version of the application that generated the error report",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "ApplicationVersion", required = true)
  @NotNull
  @Size(max = 50)
  @Column(name = "application_version", length = 50, nullable = false)
  private String applicationVersion;

  /** The date and time the error report was created. */
  @Schema(
      description = "The date and time the error report was created",
      requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Created")
  @XmlJavaTypeAdapter(OffsetDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private OffsetDateTime created;

  /** The base-64 encoded data associated with the error report. */
  @Schema(description = "The base-64 encoded data associated with the error report")
  @JsonProperty
  @XmlElement(name = "Data")
  @Column(name = "data")
  private String data;

  /** The description of the error. */
  @Schema(description = "The description of the error", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 2000)
  @Column(name = "description", length = 2000, nullable = false)
  private String description;

  /** The error detail. */
  @Schema(description = "The error detail")
  @JsonProperty
  @XmlElement(name = "Detail")
  @Size(max = 1024000)
  @Column(name = "detail")
  private String detail;

  /** The ID for the device the error report originated from. */
  @Schema(description = "The ID for the device the error report originated from")
  @JsonProperty
  @XmlElement(name = "DeviceId")
  @Column(name = "device_id")
  private UUID deviceId;

  /** The feedback provided by the user for the error. */
  @Schema(description = "The feedback provided by the user for the error")
  @JsonProperty
  @XmlElement(name = "Feedback")
  @Size(max = 2000)
  @Column(name = "feedback", length = 2000)
  private String feedback;

  /** The ID for the error report. */
  @Schema(description = "The ID for the error report", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The username for the user associated with the error report. */
  @Schema(description = "The username for the user associated with the error report")
  @JsonProperty
  @XmlElement(name = "Who")
  @Size(max = 100)
  @Column(name = "who", length = 100)
  private String who;

  /** Creates a new {@code ErrorReport} instance. */
  public ErrorReport() {}

  /**
   * Creates a new {@code ErrorReport} instance.
   *
   * @param id the ID for the error report
   * @param applicationId the ID for the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description the description of the error
   * @param detail the error detail
   * @param created the date and time the error report was created
   */
  public ErrorReport(
      UUID id,
      String applicationId,
      String applicationVersion,
      String description,
      String detail,
      OffsetDateTime created) {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.created = created;
  }

  /**
   * Creates a new {@code ErrorReport} instance.
   *
   * @param id the ID for the error report
   * @param applicationId the ID for the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description the description of the error
   * @param detail the error detail
   * @param created the date and time the error report was created
   * @param who the username for the user associated with the error report
   * @param deviceId the ID for the device the error report originated from
   * @param feedback the feedback provided by the user for the error
   * @param data the base-64 encoded data associated with the error report
   */
  public ErrorReport(
      UUID id,
      String applicationId,
      String applicationVersion,
      String description,
      String detail,
      OffsetDateTime created,
      String who,
      UUID deviceId,
      String feedback,
      String data) {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.created = created;
    this.who = who;
    this.deviceId = deviceId;
    this.feedback = feedback;
    this.data = data;
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

    ErrorReport other = (ErrorReport) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the ID for the application that generated the error report.
   *
   * @return the ID for the application that generated the error report
   */
  public String getApplicationId() {
    return applicationId;
  }

  /**
   * Returns the version of the application that generated the error report.
   *
   * @return the version of the application that generated the error report
   */
  public String getApplicationVersion() {
    return applicationVersion;
  }

  /**
   * Returns the date and time the error report was created.
   *
   * @return the date and time the error report was created
   */
  public OffsetDateTime getCreated() {
    return created;
  }

  /**
   * Returns the base-64 encoded data associated with the error report.
   *
   * @return the base-64 encoded data associated with the error report
   */
  public String getData() {
    return data;
  }

  /**
   * Returns the description of the error.
   *
   * @return the description of the error
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the error detail.
   *
   * @return the error detail
   */
  public String getDetail() {
    return detail;
  }

  /**
   * Returns the ID for the device the error report originated from.
   *
   * @return the ID for the device the error report originated from
   */
  public UUID getDeviceId() {
    return deviceId;
  }

  /**
   * Returns the feedback provided by the user for the error.
   *
   * @return the feedback provided by the user for the error
   */
  public String getFeedback() {
    return feedback;
  }

  /**
   * Returns the ID for the error report.
   *
   * @return the ID for the error report
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the username for the user associated with the error report.
   *
   * @return the username for the user associated with the error report
   */
  public String getWho() {
    return who;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Set the ID for the application that generated the error report.
   *
   * @param applicationId the ID for the application that generated the error report
   */
  public void setApplicationId(String applicationId) {
    this.applicationId = applicationId;
  }

  /**
   * Set the version of the application that generated the error report.
   *
   * @param applicationVersion the version of the application that generated the error report
   */
  public void setApplicationVersion(String applicationVersion) {
    this.applicationVersion = applicationVersion;
  }

  /**
   * Set the date and time the error report was created.
   *
   * @param created the date and time the error report was created
   */
  public void setCreated(OffsetDateTime created) {
    this.created = created;
  }

  /**
   * Set the base-64 encoded data associated with the error report.
   *
   * @param data the base-64 encoded data associated with the error report
   */
  public void setData(String data) {
    this.data = data;
  }

  /**
   * Set the description of the error.
   *
   * @param description the description of the error
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set the error detail.
   *
   * @param detail the error detail
   */
  public void setDetail(String detail) {
    this.detail = detail;
  }

  /**
   * Set the ID for the device the error report originated from.
   *
   * @param deviceId the ID for the device the error report originated from
   */
  public void setDeviceId(UUID deviceId) {
    this.deviceId = deviceId;
  }

  /**
   * Set the feedback provided by the user for the error.
   *
   * @param feedback the feedback provided by the user for the error
   */
  public void setFeedback(String feedback) {
    this.feedback = feedback;
  }

  /**
   * Set the ID for the error report.
   *
   * @param id the ID for the error report
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the username for the user associated with the error report.
   *
   * @param who the username for the user associated with the error report
   */
  public void setWho(String who) {
    this.who = who;
  }
}
