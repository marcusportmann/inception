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

import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Replace} from '../../shared';

/**
 * The AdminFooterComponent class implements the admin footer component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'admin-footer',
  template: `
    <footer class="admin-footer">
      <ng-content></ng-content>
    </footer>
  `
})
export class AdminFooterComponent implements OnInit {

  @Input() fixed?: boolean;

  /**
   * Constructs a new AdminFooterComponent.
   *
   * @param elementRef The element reference.
   */
  constructor(private elementRef: ElementRef) {
  }

  ngOnInit(): void {
    Replace(this.elementRef);

    if (this.fixed) {
      const bodySelector = document.querySelector('body');

      if (bodySelector) {
        bodySelector.classList.add('admin-footer-fixed')
      }
    }
  }
}
