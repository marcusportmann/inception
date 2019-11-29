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

/**
 * The JobStatus enumeration defines the possible statuses for a job.
 *
 * @author Marcus Portmann
 */
export enum JobStatus {

  /**
   * Unscheduled.
   */
  Unscheduled = 0,

  /**
   * Scheduled.
   */
  Scheduled = 1,

  /**
   * Executing.
   */
  Executing = 2,

  /**
   * Executed.
   */
  Executed = 3,

  /**
   * Aborted.
   */
  Aborted = 4,

  /**
   * Failed.
   */
  Failed = 5,

  /**
   * OnceOff.
   */
  OnceOff = 6,

  /**
   * Unknown.
   */
  Unknown = -1
}
