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

import { Directive, HostListener, Input } from '@angular/core';
import { toggleClasses } from '../../util/toggle-classes';
import { SIDEBAR_CSS_CLASSES } from '../components/sidebar-css-classes';

/**
 * Toggles the sidebar visibility based on an optional breakpoint.
 */
@Directive({
  // eslint-disable-next-line @angular-eslint/directive-selector
  selector: '[sidebarToggler]',
  standalone: true
})
export class SidebarTogglerDirective {
  /**
   * Optional breakpoint to control which sidebar variant to toggle.
   * Example: "sm", "md", "lg" → "sidebar-sm-show", etc.
   */
  @Input() sidebarToggler?: string;

  @HostListener('click', ['$event'])
  onClick(event: MouseEvent): void {
    event.preventDefault();

    const breakpoint = this.sidebarToggler?.trim();
    const cssClass = breakpoint ? `sidebar-${breakpoint}-show` : SIDEBAR_CSS_CLASSES[0];

    toggleClasses(cssClass, SIDEBAR_CSS_CLASSES);
  }
}
