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

import {NavigationItem} from "./services/navigation/navigation-item";
import {NavigationService} from "./services/navigation/navigation.service";

/**
 * The InceptionAppModule class provides the base class that all application module classes that
 * make use of the Inception framework.
 *
 * @author Marcus Portmann
 */
export abstract class InceptionAppModule {

  /**
   * Constructs a new InceptionAppModule.
   */
  constructor(navigationService: NavigationService) {
    navigationService.setNavigation(this.initNavigation());
  }

  /**
   * Initialise the navigation for the application.
   *
   * @returns {NavigationItem[]}
   */
  abstract initNavigation(): NavigationItem[];
}
