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
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
// Import Inception module
import {InceptionModule} from '../../../inception/inception.module';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception components
import {CodeCategoriesComponent} from './code-categories.component';
import {CodesComponent} from './codes.component';
import {EditCodeCategoryComponent} from './edit-code-category.component';
import {EditCodeComponent} from './edit-code.component';
import {NewCodeCategoryComponent} from './new-code-category.component';
import {NewCodeComponent} from './new-code.component';
import {CanActivateFunctionGuard} from '../../routing/can-activate-function-guard';

const routes: Routes = [{
  path: '',
  canActivate: [CanActivateFunctionGuard],
  component: CodeCategoriesComponent,
  data: {
    title: 'Code Categories',
    functionCodes: ['Codes.CodeAdministration']
  }
}, {
  path: 'new-code-category',
  canActivate: [CanActivateFunctionGuard],
  component: NewCodeCategoryComponent,
  data: {
    title: 'New Code Category',
    functionCodes: ['Codes.CodeAdministration']
  }
}, {
  path: ':codeCategoryId',
  data: {
    title: '{codeCategoryId}'
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: EditCodeCategoryComponent,
    data: {
      functionCodes: ['Codes.CodeAdministration']
    }
  }, {
    path: 'codes',
    data: {
      title: 'Codes'
    },
    children: [{
      path: '',
      canActivate: [CanActivateFunctionGuard],
      component: CodesComponent,
      data: {
        functionCodes: ['Codes.CodeAdministration']
      }
    }, {
      path: 'new-code',
      canActivate: [CanActivateFunctionGuard],
      component: NewCodeComponent,
      data: {
        title: 'New Code',
        functionCodes: ['Codes.CodeAdministration']
      }
    }, {
      path: ':codeId',
      canActivate: [CanActivateFunctionGuard],
      component: EditCodeComponent,
      data: {
        title: '{codeId}',
        functionCodes: ['Codes.CodeAdministration']
      }
    }
    ]
  }
  ]
}
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [CodeCategoriesComponent, CodesComponent, EditCodeCategoryComponent,
    EditCodeComponent, NewCodeCategoryComponent, NewCodeComponent
  ]
})
export class CodesModule {
}
