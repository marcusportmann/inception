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
import {NewOrganizationComponent} from './new-organization.component';
import {NewOrganizationTitleResolver} from './new-organization-title-resolver';
import {EditOrganizationComponent} from './edit-organization.component';
import {EditOrganizationTitleResolver} from './edit-organization-title-resolver';
import {OrganizationTitleResolver} from './organization-title-resolver';
import {EditUserDirectoryComponent} from './edit-user-directory.component';
import {NewUserDirectoryTitleResolver} from './new-user-directory-title-resolver';
import {EditUserDirectoryTitleResolver} from './edit-user-directory-title-resolver';
import {UserDirectoryTitleResolver} from './user-directory-title-resolver';
import {NewUserDirectoryComponent} from './new-user-directory.component';
import {InternalUserDirectoryComponent} from './internal-user-directory.component';
import {LdapUserDirectoryComponent} from './ldap-user-directory.component';
import {GroupsComponent} from './groups.component';
import {GroupsTitleResolver} from './groups-title-resolver';
import {EditGroupTitleResolver} from "./edit-group-title-resolver";
import {GroupTitleResolver} from "./group-title-resolver";
import {NewGroupTitleResolver} from "./new-group-title-resolver";
import {NewGroupComponent} from './new-group.component';
import {EditGroupComponent} from "./edit-group.component";

const routes: Routes = [{
  path: '',
  redirectTo: 'overview'
}, {
  path: 'groups',
  resolve: {
    title: GroupsTitleResolver
  },
  children: [{
    path: '',
    canActivate: [CanActivateFunctionGuard],
    component: GroupsComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.GroupAdministration']
    }
  }, {
    path: ':userDirectoryId/new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewGroupComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.GroupAdministration']
    },
    resolve: {
      title: NewGroupTitleResolver
    }
  }, {
    path: ':userDirectoryId/:name',
    pathMatch: 'full',
    redirectTo: ':userDirectoryId/:name/edit'
  }, {
    path: ':userDirectoryId/:name',
    resolve: {
      title: GroupTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditGroupComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.GroupAdministration']
      },
      resolve: {
        title: EditGroupTitleResolver
      }
    }]
  }]
},
  {
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
  }, {
    path: 'new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewOrganizationComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
    },
    resolve: {
      title: NewOrganizationTitleResolver
    }
  }, {
    path: ':organizationId',
    pathMatch: 'full',
    redirectTo: ':organizationId/edit'
  }, {
    path: ':organizationId',
    resolve: {
      title: OrganizationTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditOrganizationComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
      },
      resolve: {
        title: EditOrganizationTitleResolver
      }
    }]
  }]
}, {
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
  }, {
    path: 'new',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: NewUserDirectoryComponent,
    data: {
      authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
    },
    resolve: {
      title: NewUserDirectoryTitleResolver
    }
  }, {
    path: ':userDirectoryId',
    pathMatch: 'full',
    redirectTo: ':userDirectoryId/edit'
  }, {
    path: ':userDirectoryId',
    resolve: {
      title: UserDirectoryTitleResolver
    },
    children: [{
      path: 'edit',
      canActivate: [CanActivateFunctionGuard],
      component: EditUserDirectoryComponent,
      data: {
        authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
      },
      resolve: {
        title: EditUserDirectoryTitleResolver
      }
    }]
  }]
},
  {
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
  }
];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],

  declarations: [EditGroupComponent, EditOrganizationComponent, EditUserDirectoryComponent, EditUserComponent, GroupsComponent, InternalUserDirectoryComponent, LdapUserDirectoryComponent, NewGroupComponent, NewOrganizationComponent, NewUserComponent, NewUserDirectoryComponent, OrganizationsComponent, SecurityOverviewComponent, UserDirectoriesComponent, UsersComponent],
  providers: [EditGroupTitleResolver, EditOrganizationTitleResolver, EditUserDirectoryTitleResolver, EditUserTitleResolver, GroupTitleResolver, GroupsTitleResolver, NewGroupTitleResolver, NewOrganizationTitleResolver, NewUserDirectoryTitleResolver, NewUserTitleResolver, OrganizationsTitleResolver, OrganizationTitleResolver, SecurityOverviewTitleResolver, UserDirectoriesTitleResolver, UserDirectoryTitleResolver, UsersTitleResolver, UserTitleResolver]
})
export class SecurityModule {
}
