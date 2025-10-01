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

import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CodeCategoriesTitleResolver } from 'ngx-inception/codes';
import { ConfigsTitleResolver } from 'ngx-inception/config';
import { ErrorReportsTitleResolver } from 'ngx-inception/error';
import { MailTitleResolver } from 'ngx-inception/mail';
import { ReportingTitleResolver } from 'ngx-inception/reporting';
import { SchedulerTitleResolver } from 'ngx-inception/scheduler';
import { SecurityTitleResolver } from 'ngx-inception/security';
import { AdministrationTitleResolver } from './administration-title-resolver';
import { AdministrationComponent } from './administration.component';
import { SystemTitleResolver } from './system-title-resolver';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    component: AdministrationComponent,
    resolve: {
      title: AdministrationTitleResolver
    }
  },
  {
    path: 'security',
    pathMatch: 'prefix',
    resolve: {
      title: SecurityTitleResolver
    },
    loadChildren: () =>
      import('../wrappers/security-views-wrapper.module').then(
        (m) => m.SecurityViewsWrapperModule
      )
  },
  {
    path: 'system',
    pathMatch: 'prefix',
    resolve: {
      title: SystemTitleResolver
    },
    children: [
      {
        path: 'code-categories',
        pathMatch: 'prefix',
        resolve: {
          title: CodeCategoriesTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/codes-views-wrapper.module').then(
            (m) => m.CodesViewsWrapperModule
          )
      },
      {
        path: 'config',
        pathMatch: 'prefix',
        resolve: {
          title: ConfigsTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/config-views-wrapper.module').then(
            (m) => m.ConfigViewsWrapperModule
          )
      },
      {
        path: 'error-reports',
        pathMatch: 'prefix',
        resolve: {
          title: ErrorReportsTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/error-views-wrapper.module').then(
            (m) => m.ErrorViewsWrapperModule
          )
      },
      {
        path: 'mail',
        pathMatch: 'prefix',
        resolve: {
          title: MailTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/mail-views-wrapper.module').then(
            (m) => m.MailViewsWrapperModule
          )
      },
      {
        path: 'scheduler',
        pathMatch: 'prefix',
        resolve: {
          title: SchedulerTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/scheduler-views-wrapper.module').then(
            (m) => m.SchedulerViewsWrapperModule
          )
      },
      {
        path: 'reporting',
        pathMatch: 'prefix',
        resolve: {
          title: ReportingTitleResolver
        },
        loadChildren: () =>
          import('../wrappers/reporting-views-wrapper.module').then(
            (m) => m.ReportingViewsWrapperModule
          )
      }
    ]
  }
];

@NgModule({
  imports: [
    // Angular modules
    CommonModule,
    RouterModule.forChild(routes)
  ],
  declarations: [AdministrationComponent],
  providers: [
    // Resolvers
    CodeCategoriesTitleResolver,
    ConfigsTitleResolver,
    ErrorReportsTitleResolver,
    MailTitleResolver,
    ReportingTitleResolver,
    SchedulerTitleResolver,
    SecurityTitleResolver,
    SystemTitleResolver
  ]
})
export class AdministrationModule {}
