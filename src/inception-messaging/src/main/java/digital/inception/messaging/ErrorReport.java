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

package digital.inception.messaging;

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The <code>ErrorReport</code> class holds the information for an error report that was submitted
 * using the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
public class ErrorReport
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   */
  private UUID applicationId;

  /**
   * The version of the application that generated the error report.
   */
  private int applicationVersion;

  /**
   * The date and time the error report was created.
   */
  private LocalDateTime created;

  /**
   * The data associated with the error report e.g. the application XML.
   */
  private byte[] data;

  /**
   * The description of the error.
   */
  private String description;

  /**
   * The error detail e.g. a stack trace.
   */
  private String detail;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the error report
   * originated from.
   */
  private UUID deviceId;

  /**
   * The feedback provided by the user for the error.
   */
  private String feedback;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private UUID id;

  /**
   * The username identifying the user associated with the error report.
   */
  private String who;

  /**
   * Constructs a new <code>ErrorReport</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param description        the description of the error
   * @param detail             the error detail e.g. a stack trace
   * @param feedback           the feedback provided by the user for the error
   * @param created            the date and time the error report was created
   * @param who                the username identifying the user associated with the error report
   * @param deviceId           the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the device the error report originated from
   * @param data               the data associated with the error report e.g. the application XML
   */
  public ErrorReport(UUID id, UUID applicationId, int applicationVersion, String description,
      String detail, String feedback, LocalDateTime created, String who, UUID deviceId, byte[] data)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.description = description;
    this.detail = detail;
    this.feedback = feedback;
    this.created = created;
    this.who = who;
    this.deviceId = deviceId;
    this.data = data;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the application
   * that generated the error report.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the application
   * that generated the error report
   */
  public UUID getApplicationId()
  {
    return applicationId;
  }

  /**
   * Returns the version of the application that generated the error report.
   *
   * @return the version of the application that generated the error report
   */
  public int getApplicationVersion()
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
   * Returns the data associated with the error report e.g. the application XML.
   *
   * @return the data associated with the error report e.g. the application XML
   */
  public byte[] getData()
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
   * Returns the error detail e.g. a stack trace.
   *
   * @return the error detail e.g. a stack trace
   */
  public String getDetail()
  {
    return detail;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from
   */
  public UUID getDeviceId()
  {
    return deviceId;
  }

  /**
   * Returns the feedback provided by the user for the error.
   *
   * @return the feedback provided by the user for the error
   */
  public String getFeedback()
  {
    return feedback;
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
}
