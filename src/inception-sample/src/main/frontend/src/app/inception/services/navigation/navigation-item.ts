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

import {NavigationBadge} from "./navigation-badge";

/**
 * The NavigationItem class holds the information for a navigation item.
 *
 * @author Marcus Portmann
 */
export class NavigationItem {

  /**
   * The optional icon associated with the navigation item.
   */
  icon?: string;

  /**
   * The optional name of navigation item.
   */
  name?: string;

  /**
   * The optional url associated with the navigation item.
   */
  url?: string;

  /**
   * The optional function codes that are used to restrict access to the navigation item.
   */
  functionCodes?: string[];

  /**
   * The optional child navigation items.
   */
  children?: NavigationItem[];

  /**
   * The optional navigation badge associated with the navigation item.
   */
  badge?: NavigationBadge;

  /**
   * The optional divider indicator.
   */
  divider?: boolean;

  /**
   * Constructs a new NavigationItem.
   *
   * @param {string} icon               The icon associated with the navigation item.
   * @param {string} name               The name of navigation item.
   * @param {string} url                The url associated with the navigation item.
   * @param {string[]} functionCodes    The function codes that are used to restrict access to the
   *                                    navigation item.
   * @param {NavigationItem[]} children The optional child navigation items.
   * @param {NavigationBadge} badge     The optional navigation badge associated with the navigation
   *                                    item.
   * @param {boolean} divider           The optional divider indicator.
   */
  constructor(icon: string, name: string, url: string, functionCodes: string[], children?: NavigationItem[], badge?: NavigationBadge, divider?: boolean) {
    this.icon = icon;
    this.name = name;
    this.url = url;
    this.functionCodes = functionCodes;
    this.children = children;
    this.badge = badge;
    this.divider = divider;
  }
}
