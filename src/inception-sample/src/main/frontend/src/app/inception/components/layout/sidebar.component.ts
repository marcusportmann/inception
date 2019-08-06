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

import {Component, HostBinding, Input, OnInit} from '@angular/core';
import {sidebarCssClasses} from '../../shared';

/**
 * The SidebarComponent class implements the sidebar component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'sidebar',
  template: `
    <ng-content></ng-content>`
})
export class SidebarComponent implements OnInit {

  @Input() compact?: boolean;

  @Input() display?: string;

  @Input() fixed?: boolean;

  @Input() minimized?: boolean;

  @Input() offCanvas?: boolean;

  @HostBinding('class.sidebar') get sidebar() { return true; };

  /**
   * Constructs a new SidebarComponent.
   */
  constructor() {
  }

  ngOnInit(): void {

    const bodySelector = document.querySelector('body');

    if (bodySelector) {

      if (!!this.display) {
        let cssClass;
        this.display ? cssClass = `sidebar-${this.display}-show` : cssClass = sidebarCssClasses[0];
        bodySelector.classList.add(cssClass);
      }

      if (!!this.compact) {
        bodySelector.classList.add('sidebar-compact');
      }

      if (!!this.fixed) {
        bodySelector.classList.add('sidebar-fixed');
      }

      if (!!this.minimized) {
        bodySelector.classList.add('sidebar-minimized');
      }

      if (!!this.offCanvas) {
        bodySelector.classList.add('sidebar-off-canvas');
      }
    }
  }
}
