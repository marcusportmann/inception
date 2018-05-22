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

import {NavigationItemBadge} from "./navigation-item-badge";

/**
 * The NavigationItem class stores the information for a navigation item.
 *
 * @author Marcus Portmann
 */
export class NavigationItem {

  /**
   * The name of the navigation item.
   */
  name: string;

  /**
   * The URL for the navigation item.
   */
  url: string;

  /**
   * The icon for the navigation item.
   */
  icon: string;

  /**
   * Should the navigation item be displayed in the sidebar navigation?
   */
  showInSidebarNav: boolean;

  /**
   * The route path for the navigation item.
   */
  routePath: string;

  /**
   * The path to the module that the child routes for the navigation item will be loaded from.
   */
  routeLoadChildren: string;

  /**
   * The optional badge associated with the navigation item.
   */
  badge?: NavigationItemBadge;

  /**
   * Constructs a new NavigationItemBadge.
   *
   * @param {string} name               The name of the navigation item.
   * @param {string} url                The URL for the navigation item.
   * @param {string} icon               The icon for the navigation item.
   * @param {boolean} showInSidebarNav  Should the navigation item be displayed in the sidebar
   *                                    navigation?
   * @param {string} routePath          The path to the module that the child routes for the
   *                                    navigation item will be loaded from.
   * @param {string} routeLoadChildren  The path to the module that the child routes for the
   *                                    navigation item will be loaded from.
   * @param {NavigationItemBadge} badge The optional badge associated with the navigation item.
   */
  constructor(name: string, url: string, icon: string, showInSidebarNav: boolean, routePath: string, routeLoadChildren: string, badge?: NavigationItemBadge) {
    this.name = name;
    this.url = url;
    this.icon = icon;
    this.showInSidebarNav = showInSidebarNav;
    this.routePath = routePath;
    this.routeLoadChildren = routeLoadChildren;
    this.badge = badge;
  }
}
