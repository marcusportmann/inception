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

import {Component, ElementRef, OnInit} from '@angular/core';
import {Replace} from '../../util/replace';

/**
 * The SidebarFormComponent class implements the sidebar form component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar-form',
  template: `
    <form class="sidebar-form">
      <ng-content></ng-content>
    </form>
  `,
  standalone: false
})
export class SidebarFormComponent implements OnInit {

  /**
   * Constructs a new SidebarFormComponent.
   *
   * @param elementRef The element reference.
   */
  constructor(private elementRef: ElementRef) {
  }

  ngOnInit(): void {
    Replace(this.elementRef);
  }
}
