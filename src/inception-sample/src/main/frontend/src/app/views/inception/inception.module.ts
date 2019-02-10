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

// Import Angular modules
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'buttons',
    pathMatch: 'full',
  },
  {
    path: 'components',
    loadChildren: './components/components.module#ComponentsModule',
    data: {
      title: 'Components',
    }
  },
  {
    path: 'form',
    loadChildren: './form/form.module#FormModule',
    data: {
      title: 'Form',
    }
  },
  {
    path: 'tables',
    loadChildren: './tables/tables.module#TablesModule',
    data: {
      title: 'Tables',
    }
  },
  {
    path: 'theme',
    loadChildren: './theme/theme.module#ThemeModule',
    data: {
      title: 'Theme',
    }
  }
];

/**
 * The InceptionModule class implements the module that contains the sub-modules that illustrate the
 * use of the Inception framework.
 *
 * @author Marcus Portmann
 */
@NgModule({
  imports: [
    CommonModule,

    RouterModule.forChild(routes)
  ],
  declarations: []
})
export class InceptionModule {
}
