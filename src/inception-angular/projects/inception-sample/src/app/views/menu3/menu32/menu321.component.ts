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

import {AfterViewInit, Component} from '@angular/core';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {TitleBarService} from 'ngx-inception';

/**
 * The Menu321Component class implements the menu 3.2.1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `Menu 3.2.1`
})
export class Menu321Component implements AfterViewInit {

  /**
   * Constructs a new Menu321Component.
   *
   * @param i18n            The internationalization service.
   * @param titleBarService The title bar service.
   */
  constructor(private i18n: I18n, private titleBarService: TitleBarService) {
  }

  ngAfterViewInit(): void {
    this.titleBarService.setTitle(this.i18n({
      id: '@@menu_321_component_title',
      value: 'Custom Menu 3.2.1 Title'
    }));
  }
}
