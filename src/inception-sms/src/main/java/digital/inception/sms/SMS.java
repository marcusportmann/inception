/*
 * Copyright 2017 Marcus Portmann
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

package digital.inception.sms;

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

import javax.persistence.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * The <code>SMS</code> class holds the information for a SMS.
 *
 * @author Marcus Portmann
 */
@ApiModel(value = "SMS")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "mobileNumber", "message", "status", "sendAttempts", "lockName",
    "lastProcessed" })
@XmlRootElement(name = "Job", namespace = "http://sms.inception.digital")
@XmlType(name = "Job", namespace = "http://sms.inception.digital",
    propOrder = { "id", "mobileNumber", "message", "status", "sendAttempts", "lockName",
        "lastProcessed" })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "sms", name = "sms")
@SuppressWarnings({ "unused", "WeakerAccess" })
public class SMS
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID used to uniquely identify the SMS.
   */
  @ApiModelProperty(value = "The ID used to uniquely identify the SMS", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "id", required = true)
  @NotNull
  @SequenceGenerator(schema = "sms", name = "sms_id_seq", sequenceName = "sms_id_seq",
      allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sms_id_seq")
  @Id
  @Column(name = "id", nullable = false)
  private long id;

  /**
   * The date and time the last attempt was made to send the SMS.
   */
  @ApiModelProperty(value = "The date and time the last attempt was made to send the SMS")
  @JsonProperty
  @XmlElement(name = "LastProcessed")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_processed")
  private LocalDateTime lastProcessed;

  /**
   * The name of the entity that has locked the SMS for sending.
   */
  @ApiModelProperty(value = "The name of the entity that has locked the SMS for sending")
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name")
  private String lockName;

  /**
   * The message to send.
   */
  @ApiModelProperty(value = "The message to send", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Message", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "message", nullable = false)
  private String message;

  /**
   * The mobile number to send the SMS to.
   */
  @ApiModelProperty(value = "The mobile number to send the SMS to", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MobileNumber", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "mobile_number", nullable = false)
  private String mobileNumber;

  /**
   * The number of times that the sending of the SMS was attempted.
   */
  @ApiModelProperty(value = "The number of times that the sending of the SMS was attempted")
  @JsonProperty
  @XmlElement(name = "SendAttempts")
  @Column(name = "send_attempts")
  private Integer sendAttempts;

  /**
   * The status of the SMS.
   */
  @ApiModelProperty(value = "The status of the SMS", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private SMSStatus status;

  /**
   * Constructs a new <code>SMS</code>.
   */
  public SMS() {}

  /**
   * Constructs a new <code>SMS</code>.
   *
   * @param mobileNumber the mobile number to send the SMS to
   * @param message      the message to send
   */
  public SMS(String mobileNumber, String message)
  {
    this.mobileNumber = mobileNumber;
    this.message = message;
  }

  /**
   * Constructs a new <code>SMS</code>.
   *
   * @param mobileNumber the mobile number to send the SMS to
   * @param message      the message to send
   * @param status       the status of the SMS
   */
  SMS(String mobileNumber, String message, SMSStatus status)
  {
    this.mobileNumber = mobileNumber;
    this.message = message;
    this.status = status;
  }

  /**
   * Constructs a new <code>SMS</code>.
   *
   * @param id            the ID used to uniquely identify the SMS
   * @param mobileNumber  the mobile number to send the SMS to
   * @param message       the message to send
   * @param status        the status of the SMS
   * @param sendAttempts  the number of times that the sending of the SMS was attempted
   * @param lockName      the name of the entity that has locked the SMS for sending
   * @param lastProcessed the date and time the last attempt was made to send the SMS
   */
  SMS(long id, String mobileNumber, String message, SMSStatus status, int sendAttempts,
      String lockName, LocalDateTime lastProcessed)
  {
    this.id = id;
    this.mobileNumber = mobileNumber;
    this.message = message;
    this.status = status;
    this.sendAttempts = sendAttempts;
    this.lockName = lockName;
    this.lastProcessed = lastProcessed;
  }

  /**
   * Returns the ID used to uniquely identify the SMS.
   *
   * @return the ID used to uniquely identify the SMS
   */
  public long getId()
  {
    return id;
  }

  /**
   * Returns the date and time the last attempt was made to send the SMS.
   *
   * @return the date and time the last attempt was made to send the SMS
   */
  public LocalDateTime getLastProcessed()
  {
    return lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked the SMS for sending.
   *
   * @return the name of the entity that has locked the SMS for sending
   */
  public String getLockName()
  {
    return lockName;
  }

  /**
   * Returns the message to send.
   *
   * @return the message to send
   */
  public String getMessage()
  {
    return message;
  }

  /**
   * Returns the mobile number to send the SMS to.
   *
   * @return the mobile number to send the SMS to
   */
  public String getMobileNumber()
  {
    return mobileNumber;
  }

  /**
   * Returns the number of times that the sending of the SMS was attempted.
   *
   * @return the number of times that the sending of the SMS was attempted
   */
  public Integer getSendAttempts()
  {
    return sendAttempts;
  }

  /**
   * Returns the status of the SMS.
   *
   * @return the status of the SMS
   */
  public SMSStatus getStatus()
  {
    return status;
  }

  /**
   * Increment the number of times that the sending of the SMS was attempted.
   */
  public void incrementSendAttempts()
  {
    if (sendAttempts == null)
    {
      sendAttempts = 1;
    }
    else
    {
      sendAttempts++;
    }
  }

  /**
   * Set the ID used to uniquely identify the SMS.
   *
   * @param id the ID used to uniquely identify the SMS
   */
  public void setId(long id)
  {
    this.id = id;
  }

  /**
   * Set the date and time the last attempt was made to send the SMS.
   *
   * @param lastProcessed the date and time the last attempt was made to send the SMS
   */
  public void setLastProcessed(LocalDateTime lastProcessed)
  {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Set the name of the entity that has locked the SMS for sending.
   *
   * @param lockName the name of the entity that has locked the SMS for sending
   */
  public void setLockName(String lockName)
  {
    this.lockName = lockName;
  }

  /**
   * Set the message to send.
   *
   * @param message the message to send
   */
  public void setMessage(String message)
  {
    this.message = message;
  }

  /**
   * Set the mobile number to send the SMS to.
   *
   * @param mobileNumber the mobile number to send the SMS to
   */
  public void setMobileNumber(String mobileNumber)
  {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Set the number of times that the sending of the SMS was attempted.
   *
   * @param sendAttempts the number of times that the sending of the SMS was attempted
   */
  public void setSendAttempts(Integer sendAttempts)
  {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the status of the SMS.
   *
   * @param status the status of the SMS
   */
  public void setStatus(SMSStatus status)
  {
    this.status = status;
  }
}
