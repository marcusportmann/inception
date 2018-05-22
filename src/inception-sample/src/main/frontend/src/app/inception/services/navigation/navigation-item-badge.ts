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

/**
 * The NavigationItemBadge class stores the information for a navigation item badge.
 *
 * @author Marcus Portmann
 */
export class NavigationItemBadge {

  /**
   * The type of navigation item badge.
   */
  variant: string;

  /**
   * The text for the navigation item badge.
   */
  text: string;

  /**
   * Constructs a new NavigationItemBadge.
   *
   * @param {string} variant The type of navigation item badge.
   * @param {string} text    The text for the navigation item badge.
   */
  constructor(variant: string, text: string) {
    this.variant = variant;
    this.text = text;
  }
}
