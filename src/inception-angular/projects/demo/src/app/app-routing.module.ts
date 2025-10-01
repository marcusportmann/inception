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

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import {
  AdminContainerComponent,
  CanActivateFunctionGuard,
  NotFoundComponent,
  SimpleContainerComponent
} from 'ngx-inception/core';

import { UserProfileComponent } from 'ngx-inception/security';
import { AdministrationTitleResolver } from './views/administration/administration-title-resolver';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'menu1'
  },
  {
    path: '',
    component: AdminContainerComponent,
    children: [
      {
        path: 'profile',
        pathMatch: 'prefix',
        component: UserProfileComponent
      },
      {
        path: 'dashboard',
        pathMatch: 'prefix',
        canActivate: [CanActivateFunctionGuard],
        data: {
          title: 'Dashboard',
          authorities: ['ROLE_Administrator', 'FUNCTION_Dashboard.Dashboard']
        },
        loadChildren: () =>
          import('./views/dashboard/dashboard.module').then(
            (m) => m.DashboardModule
          )
      },
      {
        path: 'inception',
        pathMatch: 'prefix',
        data: {
          title: 'Inception'
        },
        loadChildren: () =>
          import('./views/inception/inception.module').then(
            (m) => m.InceptionModule
          )
      },
      {
        path: 'menu1',
        pathMatch: 'prefix',
        data: {
          title: 'Menu 1'
        },
        loadChildren: () =>
          import('./views/menu1/menu1.module').then((m) => m.Menu1Module)
      },
      {
        path: 'menu2',
        pathMatch: 'prefix',
        data: {
          title: 'Menu 2'
        },
        loadChildren: () =>
          import('./views/menu2/menu2.module').then((m) => m.Menu2Module)
      },
      {
        path: 'menu3',
        pathMatch: 'prefix',
        data: {
          title: 'Menu 3'
        },
        loadChildren: () =>
          import('./views/menu3/menu3.module').then((m) => m.Menu3Module)
      },
      {
        path: 'administration',
        pathMatch: 'prefix',
        resolve: {
          title: AdministrationTitleResolver
        },
        loadChildren: () =>
          import('./views/administration/administration.module').then(
            (m) => m.AdministrationModule
          )
      }
    ]
  },

  // Login route
  {
    path: 'login',
    pathMatch: 'prefix',
    component: SimpleContainerComponent,
    loadChildren: () =>
      import('./views/wrappers/login-views-wrapper.module').then(
        (m) => m.LoginViewsWrapperModule
      )
  },

  // Send Error Report route
  {
    path: 'error',
    pathMatch: 'prefix',
    component: SimpleContainerComponent,
    loadChildren: () =>
      import('./views/wrappers/error-views-wrapper.module').then(
        (m) => m.ErrorViewsWrapperModule
      )
  },

  // Default route for invalid paths
  {
    path: '**',
    pathMatch: 'full',
    component: NotFoundComponent
  }
];

@NgModule({
  // Tracing should only be enabled for DEBUG purposes
  imports: [
    // Angular modules
    RouterModule.forRoot(routes, { enableTracing: false })
  ],
  exports: [RouterModule],
  providers: [AdministrationTitleResolver]
})
export class AppRoutingModule {}
