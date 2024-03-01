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

/**
 * The Breadcrumb class.
 *
 * @author Marcus Portmann
 */

export class Breadcrumb {

  /**
   * The label for the breadcrumb.
   */
  label: string;

  /**
   * The url for the breadcrumb.
   */
  url: string;

  /**
   * Constructs a new Breadcrumb.
   *
   * @param label The label for the breadcrumb.
   * @param url   The url for the breadcrumb.
   */
  constructor(label: string, url: string) {
    this.label = label;
    this.url = url;
  }
}
