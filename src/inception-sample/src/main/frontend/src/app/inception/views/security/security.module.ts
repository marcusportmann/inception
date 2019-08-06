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

// // Import Angular modules
// import {CommonModule} from '@angular/common';
// import {FormsModule} from '@angular/forms';
// import {NgModule} from '@angular/core';
// // Import Inception module
// import {InceptionModule} from '../../inception.module';
// // Import Angular classes
// import {RouterModule, Routes} from '@angular/router';
// // Import Inception components
// import {OrganizationsComponent} from './organizations.component';
// import {OverviewComponent} from './overview.component';
// import {CanActivateFunctionGuard} from '../../routing/can-activate-function-guard';
// import {UserDirectoriesComponent} from './user-directories.component'
// import {UsersComponent} from './users.component';
//
//
// const routes: Routes = [{
//   path: '',
//   redirectTo: 'overview'
// }, {
//   path: 'overview',
//   component: OverviewComponent,
//   data: {
//     title: 'Overview',
//     icon: 'fa fa-shield-alt'
//   }
// }, {
//   path: 'organizations',
//   data: {
//     title: 'Organizations'
//   },
//   children: [{
//     path: '',
//     canActivate: [CanActivateFunctionGuard],
//     component: OrganizationsComponent,
//     data: {
//       authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
//     }
//   },
//
//     /*
//     {
//       path: 'new-organization',
//       canActivate: [
//         CanActivateFunctionGuard
//       ],
//       component: NewOrganizationComponent,
//       data: {
//         title: 'New Organization',
//         authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
//       }
//     },
//     {
//       path: ':organizationId',
//       canActivate: [
//         CanActivateFunctionGuard
//       ],
//       component: EditOrganizationComponent,
//       data: {
//         title: '{organizationId}',
//         authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration']
//       }
//     }
//     */
//   ]
// }, {
//   path: 'users',
//   data: {
//     title: 'Users'
//   },
//   children: [{
//     path: '',
//     canActivate: [CanActivateFunctionGuard],
//     component: UsersComponent,
//     data: {
//       authorities: ['ROLE_Administrator', 'FUNCTION_Security.OrganizationAdministration', 'FUNCTION_Security.ResetUserPassword', 'FUNCTION_Security.UserAdministration', 'FUNCTION_Security.UserGroups']
//     }
//   },
//
//     /*
//     {
//       path: 'new-user',
//       canActivate: [
//         CanActivateFunctionGuard
//       ],
//       component: NewUserComponent,
//       data: {
//         title: 'New User',
//         authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserAdministration']
//       }
//     },
//     {
//       path: ':username',
//       canActivate: [
//         CanActivateFunctionGuard
//       ],
//       component: EditUserComponent,
//       data: {
//         title: '{username}',
//         authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserAdministration']
//       }
//     }
//     */
//   ]
// },
//   {
//     path: 'user-directories',
//     data: {
//       title: 'User Directories'
//     },
//     children: [{
//       path: '',
//       canActivate: [CanActivateFunctionGuard],
//       component: UserDirectoriesComponent,
//       data: {
//         authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
//       }
//     },
//
//       /*
//       {
//         path: 'new-user-directory',
//         canActivate: [
//           CanActivateFunctionGuard
//         ],
//         component: NewUserDirectoryComponent,
//         data: {
//           title: 'New User Directory',
//           authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
//         }
//       },
//       {
//         path: ':userDirectoryId',
//         canActivate: [
//           CanActivateFunctionGuard
//         ],
//         component: EditUserDirectoryComponent,
//         data: {
//           title: '{userDirectoryId}',
//           authorities: ['ROLE_Administrator', 'FUNCTION_Security.UserDirectoryAdministration']
//         }
//       }
//       */
//     ]
//   }
// ];
//
// @NgModule({
//   imports: [CommonModule, FormsModule, InceptionModule,
//
//     RouterModule.forChild(routes)
//   ],
//
//   declarations: [OrganizationsComponent, OverviewComponent, UserDirectoriesComponent, UsersComponent]
// })
// export class SecurityModule {
// }
//
//



// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
// Import Inception module
import {InceptionModule} from '../../inception.module';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';


const routes: Routes = [{}];

@NgModule({
  imports: [CommonModule, FormsModule, InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: []
})
export class SecurityModule {
}
