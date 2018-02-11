/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.security;

/**
 * The <code>OrganisationStatus</code> enumeration defines the possible statuses for an
 * organisation.
 *
 * @author Marcus Portmann
 */
public enum OrganisationStatus
{
  INACTIVE(0, "Inactive"), ACTIVE(1, "Active");

  private String description;
  private int code;

  OrganisationStatus(int code, String description)
  {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the organisation status given by the specified numeric code value.
   *
   * @param code the numeric code value identifying the organisation status
   *
   * @return the organisation status given by the specified numeric code value
   */
  public static OrganisationStatus fromCode(int code)
  {
    switch (code)
    {
      case 0:
        return OrganisationStatus.INACTIVE;

      case 1:
        return OrganisationStatus.ACTIVE;

      default:
        return OrganisationStatus.INACTIVE;
    }
  }

  /**
   * Returns the numeric code value for the organisation status.
   *
   * @return the numeric code value for the organisation status
   */
  public int code()
  {
    return code;
  }

  /**
   * Returns the description for the organisation status.
   *
   * @return the description for the organisation status
   */
  public String description()
  {
    return description;
  }
}
