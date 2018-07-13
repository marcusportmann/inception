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

// Colors component
import {ColorsComponent} from '../theme/colors.component';

// Typography Component
import {TypographyComponent} from '../theme/typography.component';

const routes: Routes = [
  {
    path: '',
    redirectTo: 'colors',
    pathMatch: 'full',
  },
  {
    path: 'colors',
    component: ColorsComponent,
    data: {
      title: 'Colors',
    }
  },
  {
    path: 'typography',
    component: TypographyComponent,
    data: {
      title: 'Typography',
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
  declarations: [ColorsComponent, TypographyComponent]
})
export class ThemeModule {
}