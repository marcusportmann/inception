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

import {Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminContainerView} from 'ngx-inception/core';

/**
 * The SecurityOverviewComponent class implements the security overview component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `Security Overview`,
  standalone: false
})
export class SecurityOverviewComponent extends AdminContainerView {

  /**
   * Constructs a new SecurityOverviewComponent.
   */
  constructor(private router: Router, private activatedRoute: ActivatedRoute) {
    super();
  }

  get title(): string {
    return $localize`:@@security_security_overview_title:Security Overview`
  }
}
