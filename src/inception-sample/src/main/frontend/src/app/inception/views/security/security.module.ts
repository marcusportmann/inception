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
import {InceptionModule} from '../../inception.module';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception components
import {OrganizationsComponent} from './organizations.component';
import {SecurityOverviewComponent} from './security-overview.component';
import {CanActivateFunctionGuard} from '../../routing/can-activate-function-guard';
import {UserDirectoriesComponent} from './user-directories.component'
import {UsersComponent} from './users.component';
import {SecurityOverviewTitleResolver} from './security-overview-title-resolver';
import {OrganizationsTitleResolver} from './organizations-title-resolver';
import {UsersTitleResolver} from './users-title-resolver';
import {UserDirectoriesTitleResolver} from './user-directories-title-resolver';
import {NewUserTitleResolver} from './new-user-title-resolver';
import {EditUserTitleResolver} from './edit-user-title-resolver';
import {UserTitleResolver} from './user-title-resolver';
import {NewUserComponent} from './new-user.component';
import {EditUserComponent} from './edit-user.component';

const routes: Routes = [{
  path: '',
  redirectTo: 'overview'
}, {
  path: 'overview',
  component: SecurityOverviewComponent,
  data: {
    icon: 'fa fa-shield-alt'
  },
  resolve: {
    title: SecurityOverviewTitleResolver
  }
}, {
  path: 'organizations',
  resolve: {
    title: OrganizationsTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: OrganizationsComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
    }
  }]
}, {
  path: 'users',
  resolve: {
    title: UsersTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: UsersComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.ResetUserPassword', 'FUNCTION_Security.UserAdministration', 'FUNCTION_Security.UserGroups']
    }
  }, {
    path: ':userDirectoryId/new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewUserComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.UserAdministration']
    },
    resolve: {
      title: NewUserTitleResolver
    }
  }, {
    path: ':userDirectoryId/:username',
    pathMatch: 'full',
    redirectTo: ':userDirectoryId/:username/edit'
  }, {
    path: ':userDirectoryId/:username',
    resolve: {
      title: UserTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditUserComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.UserAdministration']
      },
      resolve: {
        title: EditUserTitleResolver
      }
    }]
  }]
},
  {
    path: 'user-directories',
    resolve: {
      title: UserDirectoriesTitleResolver
    },
    children: [{
      path: '',
      canActivate: [CanActivateFunctionGuard],
      component: UserDirectoriesComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
      }
    }]
  }
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],

  declarations: [EditUserComponent, NewUserComponent, OrganizationsComponent, SecurityOverviewComponent, UserDirectoriesComponent, UsersComponent],
  providers: [EditUserTitleResolver, NewUserTitleResolver, OrganizationsTitleResolver, SecurityOverviewTitleResolver, UserDirectoriesTitleResolver, UsersTitleResolver, UserTitleResolver]
})
export class SecurityModule {
}
