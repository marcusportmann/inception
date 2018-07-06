/*
 * Copyright 2018 Marcus Portmann
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

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {NgModule} from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

// Import Inception module
import {InceptionModule} from '../../../inception/inception.module';

// Basic Forms Component
import {BasicFormsComponent} from "./basic-forms.component";

// Advanced Form component
import {AdvancedFormComponent} from "./advanced-form.component";


const routes: Routes = [
  {
    path: '',
    redirectTo: 'basic-forms',
    pathMatch: 'full',
  },
  {
    path: 'basic-forms',
    component: BasicFormsComponent,
    data: {
      title: 'Basic Forms',
    }
  },
  {
    path: 'advanced-form',
    component: AdvancedFormComponent,
    data: {
      title: 'Advanced Form',
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [BasicFormsComponent, AdvancedFormComponent]
})
export class FormModule {
}
