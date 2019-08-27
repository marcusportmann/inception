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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>ErrorReport</code> class holds the information for an error report.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "ErrorReport")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "applicationId", "applicationVersion", "description", "detail",
    "created", "who", "deviceId", "feedback", "data" })
@XmlRootElement(name = "ErrorReport", namespace = "http://error.inception.digital")
@XmlType(name = "ErrorReport", namespace = "http://error.inception.digital",
    propOrder = { "id", "applicationId", "applicationVersion", "description", "detail", "created",
        "who", "deviceId", "feedback", "data" })
@XmlAccessorType(XmlAccessType.FIELD)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class ErrorReport
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The maximum size of the error report detail.
   */
  public static final int MAX_DETAIL_SIZE = 102400;

  /**
   * The ID used to uniquely identify the application that generated the error report.
   */
  @ApiModelProperty(
      value = "ID used to uniquely identify the application that generated the error report",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "ApplicationId", required = true)
  @NotNull
  @Size(min = 1, max = 100)
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
  private String applicationVersion;

  /**
   * The date and time the error report was created.
   */
  @ApiModelProperty(value = "The date and time the error report was created", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Created")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @NotNull
  private LocalDateTime created;

  /**
   * The base-64 encoded data associated with the error report.
   */
  @ApiModelProperty(value = "The base-64 encoded data associated with the error report")
  @JsonProperty
  @XmlElement(name = "Data")
  private String data;

  /**
   * The description of the error.
   */
  @ApiModelProperty(value = "The description of the error", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Description", required = true)
  @NotNull
  @Size(max = 4000)
  private String description;

  /**
   * The error detail.
   */
  @ApiModelProperty(value = "The error detail")
  @JsonProperty
  @XmlElement(name = "Detail")
  @Size(max = 102400)
  private String detail;

  /**
   * The optional ID used to uniquely identify the device the error report originated from.
   */
  @ApiModelProperty(
      value = "The optional ID used to uniquely identify the device the error report originated from")
  @JsonProperty
  @XmlElement(name = "DeviceId")
  @Size(max = 50)
  private String deviceId;

  /**
   * The optional feedback provided by the user for the error.
   */
  @ApiModelProperty(value = "The optional feedback provided by the user for the error")
  @JsonProperty
  @XmlElement(name = "Detail")
  @Size(max = 4000)
  private String feedback;

  /**
   * The ID used to uniquely identify the error report.
   */
  @ApiModelProperty(value = "The ID used to uniquely identify the error report", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Id", required = true)
  @NotNull
  private String id;

  /**
   * The optional username identifying the user associated with the error report.
   */
  @ApiModelProperty(
      value = "The optional username identifying the user associated with the error report")
  @JsonProperty
  @XmlElement(name = "Who")
  @Size(max = 1000)
  private String who;

  /**
   * Constructs a new <code>ErrorReport</code>.
   */
  public ErrorReport() {}

  /**
   * Constructs a new <code>ErrorReport</code>.
   *
   * @param id                 the ID used to uniquely identify the error report
   * @param applicationId      the ID used to uniquely identify the application that generated the
   *                           error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param detail             the error detail
   * @param created            the date and time the error report was created
   */
  public ErrorReport(String id, String applicationId, String applicationVersion,
      String description, String detail, LocalDateTime created)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.created = created;
  }

  /**
   * Constructs a new <code>ErrorReport</code>.
   *
   * @param id                 the ID used to uniquely identify the error report
   * @param applicationId      the ID used to uniquely identify the application that generated the
   *                           error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param detail             the error detail
   * @param created            the date and time the error report was created
   * @param who                the optional username identifying the user associated with the error
   *                           report
   * @param deviceId           the optional ID used to uniquely identify the device the error report
   *                           originated from
   * @param feedback           the optional feedback provided by the user for the error
   * @param data               the optional base-64 encoded data associated with the error report
   */
  public ErrorReport(String id, String applicationId, String applicationVersion,
      String description, String detail, LocalDateTime created, String who, String deviceId,
      String feedback, String data)
  {
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
   * Returns the optional base-64 encoded data associated with the error report.
   *
   * @return the optional base-64 encoded data associated with the error report
   */
  public String getData()
  {
    return data;
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
   * Returns the error detail.
   *
   * @return the error detail
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the optional ID used to uniquely identify the device the error report originated from.
   *
   * @return the optional ID used to uniquely identify the device the error report originated from
   */
  public String getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the optional feedback provided by the user for the error.
   *
   * @return the optional feedback provided by the user for the error
   */
  public String getFeedback()
  {
    return feedback;
  }

  /**
   * Returns the ID used to uniquely identify the error report.
   *
   * @return the ID used to uniquely identify the error report
   */
  public String getId()
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
   * Set the optional base-64 encoded data associated with the error report.
   *
   * @param data the optional base-64 encoded data associated with the error report
   */
  public void setData(String data)
  {
    this.data = data;
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
   * Set the error detail.
   *
   * @param detail the error detail
   */
  public void setDetail(String detail)
  {
    this.detail = detail;
  }

  /**
   * Set the optional ID used to uniquely identify the device the error report originated from.
   *
   * @param deviceId the optional ID used to uniquely identify the device the error report
   *                 originated from
   */
  public void setDeviceId(String deviceId)
  {
    this.deviceId = deviceId;
  }

  /**
   * Set the optional feedback provided by the user for the error.
   *
   * @param feedback the optional feedback provided by the user for the error
   */
  public void setFeedback(String feedback)
  {
    this.feedback = feedback;
  }

  /**
   * Set the ID used to uniquely identify the error report.
   *
   * @param id the ID used to uniquely identify the error report
   */
  public void setId(String id)
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
