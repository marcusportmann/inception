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

package digital.inception.sms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.f4b6a3.uuid.UuidCreator;
import digital.inception.core.xml.LocalDateTimeAdapter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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

/**
 * The <b>SMS</b> class holds the information for a SMS.
 *
 * @author Marcus Portmann
 */
@Schema(description = "An SMS")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
  "id",
  "mobileNumber",
  "message",
  "status",
  "sendAttempts",
  "lockName",
  "lastProcessed"
})
@XmlRootElement(name = "Job", namespace = "http://sms.inception.digital")
@XmlType(
    name = "Job",
    namespace = "http://sms.inception.digital",
    propOrder = {
      "id",
      "mobileNumber",
      "message",
      "status",
      "sendAttempts",
      "lockName",
      "lastProcessed"
    })
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Table(schema = "sms", name = "sms")
@SuppressWarnings({"unused", "WeakerAccess"})
public class SMS implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The date and time the SMS was created. */
  @JsonIgnore
  @XmlTransient
  @CreationTimestamp
  @Column(name = "created", nullable = false, updatable = false)
  private LocalDateTime created;

  /** The Universally Unique Identifier (UUID) for the SMS. */
  @Schema(description = "The Universally Unique Identifier (UUID) for the SMS", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "id", required = true)
  @NotNull
  @Id
  @Column(name = "id", nullable = false)
  private UUID id;

  /** The date and time the last attempt was made to send the SMS. */
  @Schema(description = "The date and time the last attempt was made to send the SMS")
  @JsonProperty
  @XmlElement(name = "LastProcessed")
  @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
  @XmlSchemaType(name = "dateTime")
  @Column(name = "last_processed")
  private LocalDateTime lastProcessed;

  /** The name of the entity that has locked the SMS for sending. */
  @Schema(description = "The name of the entity that has locked the SMS for sending")
  @XmlElement(name = "LockName")
  @Size(min = 1, max = 100)
  @Column(name = "lock_name", length = 100)
  private String lockName;

  /** The message to send. */
  @Schema(description = "The message to send", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Message", required = true)
  @NotNull
  @Size(min = 1, max = 1000)
  @Column(name = "message", length = 1000, nullable = false)
  private String message;

  /** The mobile number to send the SMS to. */
  @Schema(description = "The mobile number to send the SMS to", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "MobileNumber", required = true)
  @NotNull
  @Size(min = 1, max = 100)
  @Column(name = "mobile_number", length = 100, nullable = false)
  private String mobileNumber;

  /** The number of times that the sending of the SMS was attempted. */
  @Schema(description = "The number of times that the sending of the SMS was attempted")
  @JsonProperty
  @XmlElement(name = "SendAttempts")
  @Column(name = "send_attempts")
  private Integer sendAttempts;

  /** The status of the SMS. */
  @Schema(description = "The status of the SMS", required = true)
  @JsonProperty(required = true)
  @XmlElement(name = "Status", required = true)
  @NotNull
  @Column(name = "status", nullable = false)
  private SMSStatus status;

  /** The date and time the SMS was last updated. */
  @JsonIgnore
  @XmlTransient
  @UpdateTimestamp
  @Column(name = "updated", insertable = false)
  private LocalDateTime updated;

  /** Constructs a new <b>SMS</b>. */
  public SMS() {}

  /**
   * Constructs a new <b>SMS</b>.
   *
   * @param mobileNumber the mobile number to send the SMS to
   * @param message the message to send
   */
  public SMS(String mobileNumber, String message) {
    this.id = UuidCreator.getShortPrefixComb();
    this.mobileNumber = mobileNumber;
    this.message = message;
  }

  /**
   * Constructs a new <b>SMS</b>.
   *
   * @param mobileNumber the mobile number to send the SMS to
   * @param message the message to send
   * @param status the status of the SMS
   */
  SMS(String mobileNumber, String message, SMSStatus status) {
    this.id = UuidCreator.getShortPrefixComb();
    this.mobileNumber = mobileNumber;
    this.message = message;
    this.status = status;
  }

  /**
   * Constructs a new <b>SMS</b>.
   *
   * @param id the Universally Unique Identifier (UUID) for the SMS
   * @param mobileNumber the mobile number to send the SMS to
   * @param message the message to send
   * @param status the status of the SMS
   * @param sendAttempts the number of times that the sending of the SMS was attempted
   * @param lockName the name of the entity that has locked the SMS for sending
   * @param lastProcessed the date and time the last attempt was made to send the SMS
   */
  SMS(
      UUID id,
      String mobileNumber,
      String message,
      SMSStatus status,
      int sendAttempts,
      String lockName,
      LocalDateTime lastProcessed) {
    this.id = id;
    this.mobileNumber = mobileNumber;
    this.message = message;
    this.status = status;
    this.sendAttempts = sendAttempts;
    this.lockName = lockName;
    this.lastProcessed = lastProcessed;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>
   * false</b>
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

    SMS other = (SMS) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns the date and time the SMS was created.
   *
   * @return the date and time the SMS was created
   */
  public LocalDateTime getCreated() {
    return created;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the SMS.
   *
   * @return the Universally Unique Identifier (UUID) for the SMS
   */
  public UUID getId() {
    return id;
  }

  /**
   * Returns the date and time the last attempt was made to send the SMS.
   *
   * @return the date and time the last attempt was made to send the SMS
   */
  public LocalDateTime getLastProcessed() {
    return lastProcessed;
  }

  /**
   * Returns the name of the entity that has locked the SMS for sending.
   *
   * @return the name of the entity that has locked the SMS for sending
   */
  public String getLockName() {
    return lockName;
  }

  /**
   * Returns the message to send.
   *
   * @return the message to send
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the mobile number to send the SMS to.
   *
   * @return the mobile number to send the SMS to
   */
  public String getMobileNumber() {
    return mobileNumber;
  }

  /**
   * Returns the number of times that the sending of the SMS was attempted.
   *
   * @return the number of times that the sending of the SMS was attempted
   */
  public Integer getSendAttempts() {
    return sendAttempts;
  }

  /**
   * Returns the status of the SMS.
   *
   * @return the status of the SMS
   */
  public SMSStatus getStatus() {
    return status;
  }

  /**
   * Returns the date and time the SMS was last updated.
   *
   * @return the date and time the SMS was last updated
   */
  public LocalDateTime getUpdated() {
    return updated;
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

  /** Increment the number of times that the sending of the SMS was attempted. */
  public void incrementSendAttempts() {
    if (sendAttempts == null) {
      sendAttempts = 1;
    } else {
      sendAttempts++;
    }
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the SMS.
   *
   * @param id the Universally Unique Identifier (UUID) for the SMS
   */
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   * Set the date and time the last attempt was made to send the SMS.
   *
   * @param lastProcessed the date and time the last attempt was made to send the SMS
   */
  public void setLastProcessed(LocalDateTime lastProcessed) {
    this.lastProcessed = lastProcessed;
  }

  /**
   * Set the name of the entity that has locked the SMS for sending.
   *
   * @param lockName the name of the entity that has locked the SMS for sending
   */
  public void setLockName(String lockName) {
    this.lockName = lockName;
  }

  /**
   * Set the message to send.
   *
   * @param message the message to send
   */
  public void setMessage(String message) {
    this.message = message;
  }

  /**
   * Set the mobile number to send the SMS to.
   *
   * @param mobileNumber the mobile number to send the SMS to
   */
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  /**
   * Set the number of times that the sending of the SMS was attempted.
   *
   * @param sendAttempts the number of times that the sending of the SMS was attempted
   */
  public void setSendAttempts(Integer sendAttempts) {
    this.sendAttempts = sendAttempts;
  }

  /**
   * Set the status of the SMS.
   *
   * @param status the status of the SMS
   */
  public void setStatus(SMSStatus status) {
    this.status = status;
  }
}
