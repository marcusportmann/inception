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

import { Component, Input, HostBinding, OnInit } from '@angular/core';
import { sidebarCssClasses } from '../../shared/index';

@Component({
  selector: 'sidebar',
  template: `<ng-content></ng-content>`
})
export class SidebarComponent implements OnInit {
  @Input() compact: boolean;
  @Input() display: any;
  @Input() fixed: boolean;
  @Input() minimized: boolean;
  @Input() offCanvas: boolean;

  @HostBinding('class.sidebar') true;

  constructor() {}

  ngOnInit() {
    this.displayBreakpoint(this.display);
    this.isCompact(this.compact);
    this.isFixed(this.fixed);
    this.isMinimized(this.minimized);
    this.isOffCanvas(this.offCanvas);
  }

  isCompact(compact: boolean): void {
    if (this.compact) { document.querySelector('body').classList.add('sidebar-compact'); }
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('sidebar-fixed'); }
  }

  isMinimized(minimized: boolean): void {
    if (this.minimized) { document.querySelector('body').classList.add('sidebar-minimized'); }
  }

  isOffCanvas(offCanvas: boolean): void {
    if (this.offCanvas) { document.querySelector('body').classList.add('sidebar-off-canvas'); }
  }

  fixedPosition(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('sidebar-fixed'); }
  }

  displayBreakpoint(display: any): void {
    if (this.display !== false ) {
      let cssClass;
      this.display ? cssClass = `sidebar-${this.display}-show` : cssClass = sidebarCssClasses[0];
      document.querySelector('body').classList.add(cssClass);
    }
  }
}
