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

import {Directive, HostListener} from '@angular/core';

/**
 * The AppMobileSidebarTogglerDirective class implements the app mobile sidebar toggler directive.
 *
 * @author Marcus Portmann
 */
@Directive({
  selector: '[appMobileSidebarToggler]'
})
export class AppMobileSidebarTogglerDirective {

  /**
   * Constructs a new AppMobileSidebarTogglerDirective.
   */
  constructor() { }

  @HostListener('click', ['$event'])
  toggleOpen($event: any): void {
    $event.preventDefault();
    document.querySelector('body').classList.toggle('sidebar-mobile-show');
  }

  /**
   * Check whether the element has the class with the specified name.
   *
   * @param target    The target element.
   * @param className The class name to check for.
   *
   * @return True if the element has the class with the specified name or false otherwise.
   */
  private hasClass(target: any, className: string): boolean {
    return new RegExp('(\\s|^)' + className + '(\\s|$)').test(target.className);
  }
}
