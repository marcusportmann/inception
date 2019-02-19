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

//~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;
import java.util.UUID;

/**
 * The <code>JobParameter</code> class holds the information for a job parameter.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class JobParameter
  implements Serializable
{
  private static final long serialVersionUID = 1000000;

  /**
   * The ID uniquely identifying the job parameter.
   */
  private UUID id;

  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job the job parameter
   * is associated with.
   */
  private UUID jobId;

  /**
   * The name of the job parameter.
   */
  private String name;

  /**
   * The value of the job parameter.
   */
  private String value;

  /**
   * Constructs a new <code>JobParameter</code>.
   *
   * @param id    the ID uniquely identifying the job parameter
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job the
   *              job parameter is associated with
   * @param name  the name of the job parameter
   * @param value the value of the job parameter
   */
  public JobParameter(UUID id, UUID jobId, String name, String value)
  {
    this.id = id;
    this.jobId = jobId;
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the ID uniquely identifying the job parameter.
   *
   * @return the ID uniquely identifying the job parameter
   */
  public UUID getId()
  {
    return id;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) used to uniquely identify the job the job
   * parameter is associated with.
   *
   * @return the Universally Unique Identifier (UUID) used to uniquely identify the job the job
   * parameter is associated with
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
   * Set the ID uniquely identifying the job parameter.
   *
   * @param id the ID uniquely identifying the job parameter
   */
  public void setId(UUID id)
  {
    this.id = id;
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
