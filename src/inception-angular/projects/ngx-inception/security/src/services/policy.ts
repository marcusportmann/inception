/*
 * Copyright Marcus Portmann
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

import { PolicyType } from './policy-type';

/**
 * The Policy class holds the information for a policy.
 *
 * @author Marcus Portmann
 */
export class Policy {
  /**
   * The policy data.
   */
  data: string;

  /**
   * The ID for the policy.
   */
  id: string;

  /**
   * The name of the policy.
   */
  name: string;

  /**
   * The policy type.
   */
  type: PolicyType;

  /**
   * The version of the policy.
   */
  version: string;

  /**
   * Constructs a new Policy.
   *
   * @param id      The ID for the policy.
   * @param version The version of the policy.
   * @param name    The name of the policy.
   * @param type    The policy type.
   * @param data    The policy data.
   */
  constructor(id: string, version: string, name: string, type: PolicyType, data: string) {
    this.id = id;
    this.version = version;
    this.name = name;
    this.type = type;
    this.data = data;
  }
}
