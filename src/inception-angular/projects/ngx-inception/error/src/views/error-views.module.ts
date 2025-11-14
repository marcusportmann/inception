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
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule, Routes } from '@angular/router';
import { CanActivateFunctionGuard, CoreModule } from 'ngx-inception/core';
import { ErrorReportTitleResolver } from './error-report-title-resolver';
import { ErrorReportComponent } from './error-report.component';
import { ErrorReportsTitleResolver } from './error-reports-title-resolver';
import { ErrorReportsComponent } from './error-reports.component';
import { SendErrorReportComponent } from './send-error-report.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: ErrorReportsComponent,
    data: {
      authorities: [
        'ROLE_Administrator',
        'FUNCTION_Error.ErrorReportAdministration',
        'FUNCTION_Error.ViewErrorReport'
      ]
    }
  },
  {
    path: 'send-error-report',
    pathMatch: 'prefix',
    component: SendErrorReportComponent,
    data: {
      title: 'Send Error Report'
    }
  },
  {
    path: ':errorReportId',
    pathMatch: 'full',
    canActivate: [CanActivateFunctionGuard],
    component: ErrorReportComponent,
    data: {
      authorities: [
        'ROLE_Administrator',
        'FUNCTION_Error.ErrorReportAdministration',
        'FUNCTION_Error.ViewErrorReport'
      ]
    },
    resolve: {
      title: ErrorReportTitleResolver
    }
  }
];

@NgModule({
  imports: [
    // Angular modules
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    RouterModule.forChild(routes),

    // Inception modules
    CoreModule
  ],
  providers: [
    // Resolvers
    ErrorReportsTitleResolver,
    ErrorReportTitleResolver
  ]
})
export class ErrorViewsModule {}
