/*
 * Copyright 2021 Marcus Portmann
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
import {PartyService} from 'ngx-inception/party';

/**
 * The Menu22Component class implements the menu 2.2 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `
    <mat-card class="flex-grow-1">
      <mat-card-content>
        <div class="row">
          <div class="col m-1">
            <button mat-flat-button color="primary" (click)="doIt()">Do It</button>
          </div>
        </div>
      </mat-card-content>
    </mat-card>
  `
})
export class Menu22Component {

  constructor(private partyService: PartyService) {
  }

  doIt(): void {
    this.partyService.doIt();
  }

}
