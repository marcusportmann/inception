/*
 * Copyright 2020 Marcus Portmann
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
 * The Configuration class stores the key, value and description for the configuration.
 *
 * @author Marcus Portmann
 */
export class Configuration {

  /**
   * The description for the configuration.
   */
  description: string;

  /**
   * The key for the configuration.
   */
  key: string;

  /**
   * The value for the configuration.
   */
  value: string;

  /**
   * Constructs a new Configuration.
   *
   * @param key         The key for the configuration.
   * @param value       The value for the configuration.
   * @param description The description for the configuration.
   */
  constructor(key: string, value: string, description: string) {

    this.key = key;
    this.value = value;
    this.description = description;
  }
}
