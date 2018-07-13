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

// Basic Buttons component
import {BasicButtonsComponent} from './basic-buttons.component';

// Dropdown Buttons component
//import {BsDropdownModule} from 'ngx-bootstrap/dropdown';
import {DropdownButtonsComponent} from '../buttons/dropdown-buttons.component';

// Loading Buttons component
import {LoadingButtonsComponent} from '../buttons/loading-buttons.component';


const routes: Routes = [
  {
    path: '',
    redirectTo: 'basic-buttons',
    pathMatch: 'full',
  },
  {
    path: 'basic-buttons',
    component: BasicButtonsComponent,
    data: {
      title: 'Basic  Buttons',
    }
  },
  {
    path: 'dropdown-buttons',
    component: DropdownButtonsComponent,
    data: {
      title: 'Dropdown Buttons',
    }
  },
  {
    path: 'loading-buttons',
    component: LoadingButtonsComponent,
    data: {
      title: 'Loading Buttons',
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,

    //BsDropdownModule.forRoot(),

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [BasicButtonsComponent, DropdownButtonsComponent, LoadingButtonsComponent]
})
export class ButtonsModule {
}
