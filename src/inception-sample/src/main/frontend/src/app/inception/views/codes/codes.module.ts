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
import {FormsModule} from "@angular/forms";
import {NgModule} from '@angular/core';

// Import Inception module
import {InceptionModule} from '../../../inception/inception.module';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

// Import Inception components
import {CodeCategoriesComponent} from "./code-categories.component";
import {CodesComponent} from "./codes.component";
import {EditCodeCategoryComponent} from "./edit-code-category.component";
import {NewCodeCategoryComponent} from "./new-code-category.component";
import {NewCodeComponent} from "./new-code.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'code-categories',
    pathMatch: 'full',
    data: {
      title: ''
    }
  },
  {
    path: 'code-categories',
    data: {
      title: 'Code Categories'
    },
    children: [
      {
        path: '',
        pathMatch: 'full',
        component: CodeCategoriesComponent
      },
      {
        path: ':codeCategoryId',
        data: {
          title: '{codeCategoryId}'
        },
        children: [
          {
            path: '',
            pathMatch: 'full',
            component: EditCodeCategoryComponent
          },
          {
            path: 'codes',
            pathMatch: 'full',
            data: {
              title: 'Codes'
            },
            children: [
              {
                path: '',
                pathMatch: 'full',
                component: CodesComponent
              },
            ]
          },
          {
            path: 'new-code',
            pathMatch: 'full',
            component: NewCodeComponent,
            data: {
              title: 'New Code',
            }
          }
        ]
      },
    ]
  },
  {
    path: 'new-code-category',
    pathMatch: 'full',
    component: NewCodeCategoryComponent,
    data: {
      title: 'New Code Category'
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
  declarations: [CodeCategoriesComponent, CodesComponent, EditCodeCategoryComponent, NewCodeCategoryComponent, NewCodeComponent]
})
export class CodesModule {
}
