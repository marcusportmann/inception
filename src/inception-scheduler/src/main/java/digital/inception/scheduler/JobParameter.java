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

package digital.inception.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

import java.util.UUID;

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;

/**
 * The <code>JobParameter</code> class holds the information for a job parameter.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "JobParameter")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "jobId", "name", "value" })
@XmlRootElement(name = "JobParameter", namespace = "http://scheduler.inception.digital")
@XmlType(name = "JobParameter", namespace = "http://scheduler.inception.digital",
    propOrder = { "jobId", "name", "value" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "scheduler", name = "job_parameters")
@IdClass(JobParameterId.class)
@SuppressWarnings({ "unused", "WeakerAccess" })
public class JobParameter
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job the job parameter
   * is associated with.
   */
  @ApiModelProperty(
      value = "The Universally Unique Identifier (UUID) used to uniquely identify the job the job parameter is associated with",
      required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "JobId", required = true)
  @NotNull
  @Id
  @Column(name = "job_id", nullable = false)
  private UUID jobId;

  /**
   * The name of the job parameter.
   */
  @ApiModelProperty(value = "The name of the job parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Name", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Id
  @Column(name = "name", nullable = false)
  private String name;

  /**
   * The value of the job parameter.
   */
  @ApiModelProperty(value = "The value of the job parameter", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Value", required = true)
  @NotNull
  @Size(max = 4000)
  @Column(name = "value", nullable = false)
  private String value;

  /**
   * Constructs a new <code>JobParameter</code>.
   */
  public JobParameter() {}

  /**
   * Constructs a new <code>JobParameter</code>.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job the
   *              job parameter is associated with
   * @param name  the name of the job parameter
   * @param value the value of the job parameter
   */
  public JobParameter(UUID jobId, String name, String value)
  {
    this.jobId = jobId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the job the job
   * parameter is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the job the job
   *         parameter is associated with
   */
  public UUID getJobId()
  {
    return jobId;
  }

  /**
   * Returns the name of the job parameter.
   *
   * @return the name of the job parameter
   */
  public String getName()
  {
    return name;
  }

  /**
   * Returns the value of the job parameter.
   *
   * @return the value of the job parameter
   */
  public String getValue()
  {
    return value;
  }

  /**
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the job the job
   * parameter is associated with.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job the
   *              job parameter is associated with
   */
  public void setJobId(UUID jobId)
  {
    this.jobId = jobId;
  }

  /**
   * Set the name of the job parameter.
   *
   * @param name the name of the job parameter
   */
  public void setName(String name)
  {
    this.name = name;
  }

  /**
   * Set the value of the job parameter.
   *
   * @param value the value of the job parameter
   */
  public void setValue(String value)
  {
    this.value = value;
  }
}
