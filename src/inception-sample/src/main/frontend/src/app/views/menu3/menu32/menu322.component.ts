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

import {Component} from '@angular/core';
import {Observable, of} from 'rxjs';
import {AdminContainerView} from '../../../inception/components/layout/admin-container-view';
import {BackNavigation} from '../../../inception/components/layout/back-navigation';
import {ActivatedRoute} from '@angular/router';
import {I18n} from "@ngx-translate/i18n-polyfill";

/**
 * The Menu322Component class implements the menu 3.2.2 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `Menu 3.2.2`
})
export class Menu322Component extends AdminContainerView {

  /**
   * Constructs a new Menu322Component.
   *
   * @param activatedRoute The activated route.
   * @param i18n           The internationalization service.
   */
  constructor(private activatedRoute: ActivatedRoute, private i18n: I18n) {
    super();
  }

  /**
   * Tne back navigation for admin container view.
   */
  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@menu_322_component_back_title',
      value: 'Menu 3.1.1'
    }), ['../menu321'], {relativeTo: this.activatedRoute});
  }

  /**
   * The title for the admin container view.
   */
  get title(): string | Observable<string> {
    return this.i18n({
      id: '@@menu_322_component_title',
      value: 'Custom Menu 3.2.2 Title'
    });
  }
}
