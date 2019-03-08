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
import {NewCodeCategoryComponent} from "./new-code-category.component";

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
        path: ':id/codes',
        pathMatch: 'full',
        component: CodesComponent,
        data: {
          title: ''
        }
      }
    ]
  },



  /*
  {
    path: 'code-categories',
    pathMatch: 'full',
    component: CodeCategoriesComponent,
    data: {
      title: 'Code Categories'
    }
  },
  {
    path: 'code-categories/:id/codes',

    component: CodesComponent,
    data: {
      title: ''
    }
  },
  */



  {
    path: 'new-code-category',
    pathMatch: 'full',
    component: NewCodeCategoryComponent,
    data: {
      title: 'New Code Category',
      sidebarNav: false
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
  declarations: [CodeCategoriesComponent, CodesComponent, NewCodeCategoryComponent]
})
export class CodesModule {
}
