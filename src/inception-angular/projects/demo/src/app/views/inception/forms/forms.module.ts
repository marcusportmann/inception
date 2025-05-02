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

import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {ReactiveFormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {CoreModule} from 'ngx-inception/core';
import {ReferenceComponentsModule} from 'ngx-inception/reference';
import {ExampleComponent} from './example.component';
import {ReferenceComponentsComponent} from './reference-components.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: 'example'
  }, {
    path: 'example',
    pathMatch: 'prefix',
    component: ExampleComponent,
    data: {
      title: 'Example',
    }
  }, {
    path: 'reference-components',
    pathMatch: 'prefix',
    component: ReferenceComponentsComponent,
    data: {
      title: 'Reference Components',
    }
  }
];

@NgModule({
  imports: [
    // Angular modules
    CommonModule, ReactiveFormsModule, RouterModule.forChild(routes),

    // Inception modules
    CoreModule, ReferenceComponentsModule,
  ],
  declarations: [ExampleComponent, ReferenceComponentsComponent],
  providers: []
})
export class FormsModule {
}
