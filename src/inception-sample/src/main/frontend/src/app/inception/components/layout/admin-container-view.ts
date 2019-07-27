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

import {BackNavigation} from './back-navigation';

/**
 * The AdminContainerView class provides the abstract base class that all admin container view
 * components should be derived from.
 *
 * @author Marcus Portmann
 */
export abstract class AdminContainerView {

  /**
   * Tne back navigation for admin container view.
   */
  get backNavigation(): BackNavigation {
    return null;
  }

  /**
   * Does the admin container view have a back navigation option?
   */
  get hasBackNavigation(): boolean {
    return false;
  }

  /**
   * Does the admin container view have a title?
   */
  get hasTitle(): boolean {
    return false;
  }

  /**
   * The title for the admin container view.
   */
  get title(): string {
    return null;
  }
}
