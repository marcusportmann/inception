/*
 * Copyright 2019 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
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
import {CodeCategoryTitleResolver} from './code-category-title-resolver';
import {CodeCategoriesTitleResolver} from './code-categories-title-resolver';
import {CodesTitleResolver} from './codes-title-resolver';
import {NewCodeCategoryTitleResolver} from './new-code-category-title-resolver';
import {EditCodeCategoryTitleResolver} from './edit-code-category-title-resolver';
import {NewCodeTitleResolver} from './new-code-title-resolver';
import {CodeTitleResolver} from "./code-title-resolver";
import {EditCodeTitleResolver} from "./edit-code-title-resolver";


const routes: Routes = [{
  path: '',
  pathMatch: 'full',
  redirectTo: 'code-categories',
}, {
  path: 'code-categories',
  resolve: {
    title: CodeCategoriesTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: CodeCategoriesComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
    }
  }, {
    path: 'new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewCodeCategoryComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
    },
    resolve: {
      title: NewCodeCategoryTitleResolver
    }
  }, {
    path: ':codeCategoryId',
    resolve: {
      title: CodeCategoryTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditCodeCategoryComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
      },
      resolve: {
        title: EditCodeCategoryTitleResolver
      }
    }, {
      path: 'codes',
      resolve: {
        title: CodesTitleResolver
      },
      children: [{
        path: '',
        canActivate: [CanActivateFunctionGuard],
        component: CodesComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
        }
      }, {
        path: 'new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewCodeComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
        },
        resolve: {
          title: NewCodeTitleResolver
        }
      }, {
        path: ':codeId',
        resolve: {
          title: CodeTitleResolver
        },
        children: [{
          path: 'edit',
          canActivate: [CanActivateFunctionGuard],
          component: EditCodeComponent,
          data: {
            authorities: ['ROLE_Administrator', 'FUNCTION_Codes.CodeAdministration']
          },
          resolve: {
            title: EditCodeTitleResolver
          }
        }]
      }]
    }]
  }]
}
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [CodeCategoriesComponent, CodesComponent, EditCodeCategoryComponent,
    EditCodeComponent, NewCodeCategoryComponent, NewCodeComponent
  ],
  providers: [CodeCategoriesTitleResolver, CodeCategoryTitleResolver, CodesTitleResolver, CodeTitleResolver, EditCodeCategoryTitleResolver, EditCodeTitleResolver, NewCodeCategoryTitleResolver, NewCodeTitleResolver]
})
export class CodesModule {
}



