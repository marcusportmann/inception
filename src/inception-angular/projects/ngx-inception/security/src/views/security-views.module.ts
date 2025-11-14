/*
 * Copyright Marcus Portmann
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

import { ClipboardModule } from '@angular/cdk/clipboard';
import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterModule, Routes } from '@angular/router';
import { CanActivateFunctionGuard, CoreModule } from 'ngx-inception/core';
import { EditGroupTitleResolver } from './edit-group-title-resolver';
import { EditGroupComponent } from './edit-group.component';
import { EditPolicyTitleResolver } from './edit-policy-title-resolver';
import { EditPolicyComponent } from './edit-policy.component';
import { EditTenantTitleResolver } from './edit-tenant-title-resolver';
import { EditTenantComponent } from './edit-tenant.component';
import { EditUserDirectoryTitleResolver } from './edit-user-directory-title-resolver';
import { EditUserDirectoryComponent } from './edit-user-directory.component';
import { EditUserTitleResolver } from './edit-user-title-resolver';
import { EditUserComponent } from './edit-user.component';
import { GroupMembersTitleResolver } from './group-members-title-resolver';
import { GroupMembersComponent } from './group-members.component';
import { GroupRolesTitleResolver } from './group-roles-title-resolver';
import { GroupRolesComponent } from './group-roles.component';
import { GroupTitleResolver } from './group-title-resolver';
import { GroupsTitleResolver } from './groups-title-resolver';
import { GroupsComponent } from './groups.component';
import { NewGroupTitleResolver } from './new-group-title-resolver';
import { NewGroupComponent } from './new-group.component';
import { NewPolicyTitleResolver } from './new-policy-title-resolver';
import { NewPolicyComponent } from './new-policy.component';
import { NewTenantTitleResolver } from './new-tenant-title-resolver';
import { NewTenantComponent } from './new-tenant.component';
import { NewTokenTitleResolver } from './new-token-title-resolver';
import { NewTokenComponent } from './new-token.component';
import { NewUserDirectoryTitleResolver } from './new-user-directory-title-resolver';
import { NewUserDirectoryComponent } from './new-user-directory.component';
import { NewUserTitleResolver } from './new-user-title-resolver';
import { NewUserComponent } from './new-user.component';
import { PoliciesTitleResolver } from './policies-title-resolver';
import { PoliciesComponent } from './policies.component';
import { PolicyTitleResolver } from './policy-title-resolver';
import { ResetUserPasswordTitleResolver } from './reset-user-password-title-resolver';
import { ResetUserPasswordComponent } from './reset-user-password.component';
import { SecurityOverviewTitleResolver } from './security-overview-title-resolver';
import { SecurityOverviewComponent } from './security-overview.component';
import { TenantTitleResolver } from './tenant-title-resolver';
import { TenantUserDirectoriesTitleResolver } from './tenant-user-directories-title-resolver';
import { TenantUserDirectoriesComponent } from './tenant-user-directories.component';
import { TenantsTitleResolver } from './tenants-title-resolver';
import { TenantsComponent } from './tenants.component';
import { TokenTitleResolver } from './token-title-resolver';
import { TokensTitleResolver } from './tokens-title-resolver';
import { TokensComponent } from './tokens.component';
import { UserDirectoriesTitleResolver } from './user-directories-title-resolver';
import { UserDirectoriesComponent } from './user-directories.component';
import { UserDirectoryTitleResolver } from './user-directory-title-resolver';
import { UserGroupsTitleResolver } from './user-groups-title-resolver';
import { UserGroupsComponent } from './user-groups.component';
import { UserTitleResolver } from './user-title-resolver';
import { UsersTitleResolver } from './users-title-resolver';
import { UsersComponent } from './users.component';
import { ViewTokenTitleResolver } from './view-token-title-resolver';
import { ViewTokenComponent } from './view-token.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: 'overview'
  },
  {
    path: 'groups',
    pathMatch: 'prefix',
    resolve: {
      title: GroupsTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: GroupsComponent,
        data: {
          authorities: [
            'ROLE_Administrator',
            'FUNCTION_Security.TenantAdministration',
            'FUNCTION_Security.GroupAdministration'
          ]
        }
      },
      {
        path: ':userDirectoryId/new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewGroupComponent,
        data: {
          authorities: [
            'ROLE_Administrator',
            'FUNCTION_Security.TenantAdministration',
            'FUNCTION_Security.GroupAdministration'
          ]
        },
        resolve: {
          title: NewGroupTitleResolver
        }
      },
      {
        path: ':userDirectoryId/:groupName',
        pathMatch: 'full',
        redirectTo: ':userDirectoryId/:groupName/edit'
      },
      {
        path: ':userDirectoryId/:groupName',
        pathMatch: 'prefix',
        resolve: {
          title: GroupTitleResolver
        },
        children: [
          {
            path: 'edit',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: EditGroupComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.GroupAdministration'
              ]
            },
            resolve: {
              title: EditGroupTitleResolver
            }
          },
          {
            path: 'members',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: GroupMembersComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.GroupAdministration'
              ]
            },
            resolve: {
              title: GroupMembersTitleResolver
            }
          },
          {
            path: 'roles',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: GroupRolesComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.GroupAdministration'
              ]
            },
            resolve: {
              title: GroupRolesTitleResolver
            }
          }
        ]
      }
    ]
  },
  {
    path: 'overview',
    pathMatch: 'prefix',
    component: SecurityOverviewComponent,
    data: {
      icon: 'fa fa-shield-alt'
    },
    resolve: {
      title: SecurityOverviewTitleResolver
    }
  },
  {
    path: 'policies',
    pathMatch: 'prefix',
    resolve: {
      title: PoliciesTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: PoliciesComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.PolicyAdministration']
        }
      },
      {
        path: 'new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewPolicyComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.PolicyAdministration']
        },
        resolve: {
          title: NewPolicyTitleResolver
        }
      },
      {
        path: ':policyId',
        pathMatch: 'full',
        redirectTo: ':policyId/edit'
      },
      {
        path: ':policyId',
        pathMatch: 'prefix',
        resolve: {
          title: PolicyTitleResolver
        },
        children: [
          {
            path: 'edit',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: EditPolicyComponent,
            data: {
              authorities: ['ROLE_Administrator', 'FUNCTION_Security.PolicyAdministration']
            },
            resolve: {
              title: EditPolicyTitleResolver
            }
          }
        ]
      }
    ]
  },
  {
    path: 'tenants',
    pathMatch: 'prefix',
    resolve: {
      title: TenantsTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: TenantsComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.TenantAdministration']
        }
      },
      {
        path: 'new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewTenantComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.TenantAdministration']
        },
        resolve: {
          title: NewTenantTitleResolver
        }
      },
      {
        path: ':tenantId',
        pathMatch: 'full',
        redirectTo: ':tenantId/edit'
      },
      {
        path: ':tenantId',
        pathMatch: 'prefix',
        resolve: {
          title: TenantTitleResolver
        },
        children: [
          {
            path: 'edit',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: EditTenantComponent,
            data: {
              authorities: ['ROLE_Administrator', 'FUNCTION_Security.TenantAdministration']
            },
            resolve: {
              title: EditTenantTitleResolver
            }
          },
          {
            path: 'user-directories',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: TenantUserDirectoriesComponent,
            data: {
              authorities: ['ROLE_Administrator', 'FUNCTION_Security.TenantAdministration']
            },
            resolve: {
              title: TenantUserDirectoriesTitleResolver
            }
          }
        ]
      }
    ]
  },
  {
    path: 'tokens',
    pathMatch: 'prefix',
    resolve: {
      title: TokensTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: TokensComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.TokenAdministration']
        }
      },
      {
        path: 'new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewTokenComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.TokenAdministration']
        },
        resolve: {
          title: NewTokenTitleResolver
        }
      },
      {
        path: 'reissue/:existingTokenId',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: NewTokenComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.TokenAdministration']
        },
        resolve: {
          title: NewTokenTitleResolver
        }
      },
      {
        path: ':tokenId',
        pathMatch: 'full',
        redirectTo: ':tokenId/view'
      },
      {
        path: ':tokenId',
        pathMatch: 'prefix',
        resolve: {
          title: TokenTitleResolver
        },
        children: [
          {
            path: 'view',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: ViewTokenComponent,
            data: {
              authorities: ['ROLE_Administrator', 'FUNCTION_Security.TokenAdministration']
            },
            resolve: {
              title: ViewTokenTitleResolver
            }
          }
        ]
      }
    ]
  },
  {
    path: 'user-directories',
    pathMatch: 'prefix',
    resolve: {
      title: UserDirectoriesTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: UserDirectoriesComponent,
        data: {
          authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
        }
      },
      {
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
      },
      {
        path: ':userDirectoryId',
        pathMatch: 'full',
        redirectTo: ':userDirectoryId/edit'
      },
      {
        path: ':userDirectoryId',
        resolve: {
          title: UserDirectoryTitleResolver
        },
        children: [
          {
            path: 'edit',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: EditUserDirectoryComponent,
            data: {
              authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
            },
            resolve: {
              title: EditUserDirectoryTitleResolver
            }
          }
        ]
      }
    ]
  },
  {
    path: 'users',
    pathMatch: 'prefix',
    resolve: {
      title: UsersTitleResolver
    },
    children: [
      {
        path: '',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        component: UsersComponent,
        data: {
          authorities: [
            'ROLE_Administrator',
            'FUNCTION_Security.TenantAdministration',
            'FUNCTION_Security.UserAdministration',
            'FUNCTION_Security.UserGroups',
            'FUNCTION_Security.ResetUserPassword'
          ]
        }
      },
      {
        path: ':userDirectoryId/new',
        pathMatch: 'full',
        canActivate: [CanActivateFunctionGuard],
        component: NewUserComponent,
        data: {
          authorities: [
            'ROLE_Administrator',
            'FUNCTION_Security.TenantAdministration',
            'FUNCTION_Security.UserAdministration'
          ]
        },
        resolve: {
          title: NewUserTitleResolver
        }
      },
      {
        path: ':userDirectoryId/:username',
        pathMatch: 'full',
        redirectTo: ':userDirectoryId/:username/edit'
      },
      {
        path: ':userDirectoryId/:username',
        pathMatch: 'prefix',
        resolve: {
          title: UserTitleResolver
        },
        children: [
          {
            path: 'edit',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: EditUserComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.UserAdministration'
              ]
            },
            resolve: {
              title: EditUserTitleResolver
            }
          },
          {
            path: 'groups',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: UserGroupsComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.UserAdministration'
              ]
            },
            resolve: {
              title: UserGroupsTitleResolver
            }
          },
          {
            path: 'reset-user-password',
            pathMatch: 'prefix',
            canActivate: [CanActivateFunctionGuard],
            component: ResetUserPasswordComponent,
            data: {
              authorities: [
                'ROLE_Administrator',
                'FUNCTION_Security.TenantAdministration',
                'FUNCTION_Security.UserAdministration',
                'FUNCTION_Security.ResetUserPassword'
              ]
            },
            resolve: {
              title: ResetUserPasswordTitleResolver
            }
          }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [
    // Angular modules
    ClipboardModule,
    CommonModule,
    FormsModule,
    MatSnackBarModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),

    // Inception modules
    CoreModule
  ],
  providers: [
    // Resolvers
    EditGroupTitleResolver,
    EditPolicyTitleResolver,
    EditTenantTitleResolver,
    EditUserDirectoryTitleResolver,
    EditUserTitleResolver,
    GroupMembersTitleResolver,
    GroupRolesTitleResolver,
    GroupTitleResolver,
    GroupsTitleResolver,
    NewGroupTitleResolver,
    NewPolicyTitleResolver,
    NewTenantTitleResolver,
    NewTokenTitleResolver,
    NewUserDirectoryTitleResolver,
    NewUserTitleResolver,
    PoliciesTitleResolver,
    PolicyTitleResolver,
    ResetUserPasswordTitleResolver,
    SecurityOverviewTitleResolver,
    TenantsTitleResolver,
    TenantTitleResolver,
    TenantUserDirectoriesTitleResolver,
    TokensTitleResolver,
    TokenTitleResolver,
    UserDirectoriesTitleResolver,
    UserDirectoryTitleResolver,
    UserGroupsTitleResolver,
    UsersTitleResolver,
    UserTitleResolver,
    ViewTokenTitleResolver
  ]
})
export class SecurityViewsModule {}
