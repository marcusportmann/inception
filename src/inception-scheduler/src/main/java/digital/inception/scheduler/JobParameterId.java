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
 * The <code>JobParameterId</code> class implements the ID class for the <code>JobParameter</code>
 * class.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused" })
public class JobParameterId
  implements Serializable
{
  /**
   * The Universally Unique Identifier (UUID) used to uniquely identify the job the job parameter is associated with.
   */
  private UUID jobId;

  /**
   * The name of the job parameter.
   */
  private String name;

  /**
   * Constructs a new <code>JobParameterId</code>.
   */
  public JobParameterId() {}

  /**
   * Constructs a new <code>JobParameterId</code>.
   *
   * @param jobId the Universally Unique Identifier (UUID) used to uniquely identify the job the
   *              job parameter is associated with
   * @param name  the name of the job parameter
   */
  public JobParameterId(UUID jobId, String name)
  {
    this.jobId = jobId;
    this.name = name;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param obj the reference object with which to compare
   *
   * @return <code>true</code> if this object is the same as the obj argument otherwise
   *         <code>false</code>
   */
  @Override
  public boolean equals(Object obj)
  {
    if (this == obj)
    {
      return true;
    }

    if (obj == null)
    {
      return false;
    }

    if (getClass() != obj.getClass())
    {
      return false;
    }

    JobParameterId other = (JobParameterId) obj;

    return jobId.equals(other.jobId) && name.equals(other.name);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode()
  {
    return ((jobId == null)
        ? 0
        : jobId.hashCode()) + ((name == null)
        ? 0
        : name.hashCode());
  }
}
