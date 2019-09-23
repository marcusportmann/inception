/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.error;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import digital.inception.core.xml.LocalDateTimeAdapter;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.time.LocalDateTime;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>ErrorReportSummary</code> class holds the summary information for an error report.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ErrorReportSummary")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "applicationId", "applicationVersion", "description", "created", "who",
    "deviceId" })
@XmlRootElement(name = "ErrorReportSummary", namespace = "http://error.inception.digital")
@XmlType(name = "ErrorReport", namespace = "http://error.inception.digital",
    propOrder = { "id", "applicationId", "applicationVersion", "description", "created", "who",
        "deviceId" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "error", name = "error_reports")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ErrorReportSummary
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID used to uniquely identify the application that generated the error report.
   */
  @ApiModelProperty(
      value = "ID used to uniquely identify the application that generated the error report",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ApplicationId", required = true)
  @NotNull
  @Size(min = 1, max = 200)
  @Column(name = "application_id", nullable = false, length = 200)
  private String applicationId;

  /**
   * The version of the application that generated the error report.
   */
  @ApiModelProperty(value = "The version of the application that generated the error report",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ApplicationVersion", required = true)
  @NotNull
  @Size(max = 50)
  @Column(name = "application_version", nullable = false)
  private String applicationVersion;

  /**
   * The date and time the error report was created.
   */
  @ApiModelProperty(value = "The date and time the error report was created", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "When")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  @Column(name = "created", nullable = false)
  private LocalDateTime created;

  /**
   * The description of the error.
   */
  @ApiModelProperty(value = "The description of the error", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 4000)
  @Column(name = "description", nullable = false, length = 4000)
  private String description;

  /**
   * The optional Universally Unique Identifier (UUID) used to uniquely identify the device the
   * error report originated from.
   */
  @ApiModelProperty(
      value = "The optional Universally Unique Identifier (UUID) used to uniquely identify the device the error report originated from")
  @JsonProperty
  @XmlElement(name = "DeviceId")
  @Size(max = 50)
  @Column(name = "device_id")
  private UUID deviceId;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the error report",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /**
   * The optional username identifying the user associated with the error report.
   */
  @ApiModelProperty(
      value = "The optional username identifying the user associated with the error report")
  @JsonProperty
  @XmlElement(name = "Who")
  @Size(max = 100)
  @Column(name = "who", length = 100)
  private String who;

  /**
   * Constructs a new <code>ErrorReportSummary</code>.
   */
  public ErrorReportSummary() {}

  /**
   * Constructs a new <code>ErrorReportSummary</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the ID used to uniquely identify the application that generated the
   *                           error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param created            the date and time the error report was created
   * @param who                the optional username identifying the user associated with the error
   *                           report
   * @param deviceId           the optional Universally Unique Identifier (UUID) used to uniquely
   *                           identify the device the error report originated from
   */
  public ErrorReportSummary(UUID id, String applicationId, String applicationVersion,
      String description, LocalDateTime created, String who, UUID deviceId)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.created = created;
    this.who = who;
    this.deviceId = deviceId;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the object argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object object)
  {
    if (this == object)
    {
      return true;
    }

    if (object == null)
    {
      return false;
    }

    if (getClass() != object.getClass())
    {
      return false;
    }

    ErrorReportSummary other = (ErrorReportSummary) object;

    return (id != null) && id.equals(other.id);
  }

  /**
   * Returns the ID used to uniquely identify the application that generated the error report.
   *
   * @return the ID used to uniquely identify the application that generated the error report
   */
  public String getApplicationId()
  {
    return applicationId;
  }

  /**
   * Returns the version of the application that generated the error report.
   *
   * @return the version of the application that generated the error report
   */
  public String getApplicationVersion()
  {
    return applicationVersion;
  }

  /**
   * Returns the date and time the error report was created.
   *
   * @return the date and time the error report was created
   */
  public LocalDateTime getCreated()
  {
    return created;
  }

  /**
   * Returns the description of the error.
   *
   * @return the description of the error
   */
  public String getDescription()
  {
    return description;
  }

  /**
   * Returns the optional Universally Unique Identifier (UUID) used to uniquely identify the device
   * the error report originated from.
   *
   * @return the optional Universally Unique Identifier (UUID) used to uniquely identify the device
   *         the error report originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the error report.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the error report
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the username identifying the user associated with the error report.
   *
   * @return the username identifying the user associated with the error report
   */
  public String getWho()
  {
    return who;
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return (id == null)
        ? 0
        : id.hashCode();
  }

  /**
   * Set the ID used to uniquely identify the application that generated the error report.
   *
   * @param applicationId the ID used to uniquely identify the application that generated the error
   *                      report
   */
  public void setApplicationId(String applicationId)
  {
    this.applicationId = applicationId;
  }

  /**
   * Set the version of the application that generated the error report.
   *
   * @param applicationVersion the version of the application that generated the error report
   */
  public void setApplicationVersion(String applicationVersion)
  {
    this.applicationVersion = applicationVersion;
  }

  /**
   * Set the date and time the error report was created.
   *
   * @param created the date and time the error report was created
   */
  public void setCreated(LocalDateTime created)
  {
    this.created = created;
  }

  /**
   * Set the description of the error.
   *
   * @param description the description of the error
   */
  public void setDescription(String description)
  {
    this.description = description;
  }

  /**
   * Set the optional Universally Unique Identifier (UUID) used to uniquely identify the device the
   * error report originated from.
   *
   * @param deviceId the optional Universally Unique Identifier (UUID) used to uniquely identify
   *                 the device the error report originated from
   */
  public void setDeviceId(UUID deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the error report.
   *
   * @param id the Universally Unique Identifier (UUID) used to uniquely identify the error report
   */
  public void setId(UUID id)
  {
    this.id = id;
  }

  /**
   * Set the optional username identifying the user associated with the error report.
   *
   * @param who the optional username identifying the user associated with the error report
   */
  public void setWho(String who)
  {
    this.who = who;
  }
}
