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

import { Component, inject } from '@angular/core';
import { CoreModule } from 'ngx-inception/core';
import { first } from 'rxjs/operators';
import { Data } from '../../services/data';
import { DataService } from '../../services/data.service';

/**
 * The Menu1Component class implements the menu 1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'app-menu1',
  standalone: true,
  imports: [CoreModule],
  template: `
    <mat-card class="flex-grow-1">
      <mat-card-content> </mat-card-content>
    </mat-card>
  `
})
export class Menu1Component {
  private dataService = inject(DataService);

  clickMe(): void {
    this.dataService
      .getData()
      .pipe(first())
      .subscribe((data: Data) => {
        console.log('data = ', data);

        this.dataService.validateData(data).pipe(first()).subscribe();
      });
  }
}
