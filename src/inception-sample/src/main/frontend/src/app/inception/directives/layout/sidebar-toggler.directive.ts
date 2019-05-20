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

import {Directive, HostListener, Input, OnInit} from '@angular/core';
import {sidebarCssClasses} from '../../shared';
import {ToggleClasses} from '../../shared/toggle-classes';

/**
 * The SidebarTogglerDirective class implements the sidebar toggler directive.
 *
 * @author Marcus Portmann
 */
@Directive({
  // tslint:disable-next-line
  selector: '[sidebarToggler]'
})
export class SidebarTogglerDirective implements OnInit {

  @Input('sidebarToggler') breakpoint: string;

  bp;

  /**
   * Constructs a new SidebarTogglerDirective.
   */
  constructor() {
  }

  ngOnInit(): void {
    this.bp = this.breakpoint;
  }

  @HostListener('click', ['$event']) toggleOpen($event: any): void {
    $event.preventDefault();
    let cssClass;
    this.bp ? cssClass = `sidebar-${this.bp}-show` : cssClass = sidebarCssClasses[0];
    ToggleClasses(cssClass, sidebarCssClasses);
  }
}
