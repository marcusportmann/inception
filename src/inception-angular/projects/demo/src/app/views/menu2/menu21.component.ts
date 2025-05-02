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

/**
 * The Menu21Component class implements the menu 2.1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `
      <mat-card class="flex-grow-1">
          <mat-card-content>
              Menu 2.1
          </mat-card-content>
      </mat-card>
  `,
  standalone: false
})
export class Menu21Component {

  constructor() {
  }
}
