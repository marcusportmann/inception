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
 * The <code>ErrorReportSummary</code> class holds the summary information for an error report that
 * was submitted using the messaging infrastructure.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public class ErrorReportSummary
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
   * The Universally Unique Identifier (UUID) used to uniquely identify the device the error report
   * originated from.
   */
  private UUID deviceId;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the error report.
   */
  private UUID id;

  /**
   * The username identifying the user associated with the error report.
   */
  private String who;

  /**
   * Constructs a new <code>ErrorReportSummary</code>.
   *
   * @param id                 the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the error report
   * @param applicationId      the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the application that generated the error report
   * @param applicationVersion the version of the application that generated the error report
   * @param created            the date and time the error report was created
   * @param who                the username identifying the user associated with the error report
   * @param deviceId           the Universally Unique Identifier (UUID) used to uniquely identify
   *                           the device the error report originated from
   */
  ErrorReportSummary(UUID id, UUID applicationId, int applicationVersion, LocalDateTime created,
      String who, UUID deviceId)
  {
    this.id = id;
    this.applicationId = applicationId;
    this.applicationVersion = applicationVersion;
    this.created = created;
    this.who = who;
    this.deviceId = deviceId;
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
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the application that
   * generated the error report.
   *
   * @param applicationId the Universally Unique Identifier (UUID) used to uniquely identify the
   *                      application that generated the error report
   */
  public void setApplicationId(UUID applicationId)
  {
    this.applicationId = applicationId;
  }

  /**
   * Set the version of the application that generated the error report.
   *
   * @param applicationVersion the version of the application that generated the error report
   */
  public void setApplicationVersion(int applicationVersion)
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
   * Set the Universally Unique Identifier (UUID) used to uniquely identify the device the error
   * report originated from.
   *
   * @param deviceId the Universally Unique Identifier (UUID) used to uniquely identify the device
   *                 the error report originated from
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
   * Set the username identifying the user associated with the error report.
   *
   * @param who the username identifying the user associated with the error report
   */
  public void setWho(String who)
  {
    this.who = who;
  }
}
