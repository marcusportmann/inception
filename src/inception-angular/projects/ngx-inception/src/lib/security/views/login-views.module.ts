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
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {NgModule} from '@angular/core';
// Import Angular classes
import {RouterModule, Routes} from '@angular/router';
// Import Inception modules
import {CoreModule} from '../../core/core.module';
import {LayoutModule} from '../../layout/layout.module';
// Import Inception components
import {LoginComponent} from './login.component';
import {SelectOrganizationComponent} from './select-organization.component';
import {ExpiredPasswordComponent} from './expired-password.component';
import {ForgottenPasswordComponent} from './forgotten-password.component';
import {ResetPasswordComponent} from './reset-password.component';
// Import Inception resolvers
import {ExpiredPasswordTitleResolver} from './expired-password-title-resolver';
import {ForgottenPasswordTitleResolver} from './forgotten-password-title-resolver';
import {LoginTitleResolver} from './login-title-resolver';
import {ResetPasswordTitleResolver} from './reset-password-title-resolver';
import {SelectOrganizationTitleResolver} from './select-organization-title-resolver';
import {DialogModule} from '../../dialog/dialog.module';
import {SecurityModule} from '../security.module';


const routes: Routes = [{
  path: '',
  component: LoginComponent,
  resolve: {
    title: LoginTitleResolver
  }
}, {
  path: 'expired-password',
  component: ExpiredPasswordComponent,
  resolve: {
    title: ExpiredPasswordTitleResolver
  }
}, {
  path: 'forgotten-password',
  component: ForgottenPasswordComponent,
  resolve: {
    title: ForgottenPasswordTitleResolver
  }
}, {
  path: 'reset-password',
  component: ResetPasswordComponent,
  resolve: {
    title: ResetPasswordTitleResolver
  }
}, {
  path: 'select-organization',
  component: SelectOrganizationComponent,
  resolve: {
    title: SelectOrganizationTitleResolver
  }
}
];

@NgModule({
  declarations: [

    // Components
    ExpiredPasswordComponent, ForgottenPasswordComponent, LoginComponent, ResetPasswordComponent,
    SelectOrganizationComponent
  ],
  imports: [

    // Angular modules
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule.forChild(routes),

    // Inception modules
    CoreModule.forRoot(), DialogModule.forRoot(), LayoutModule.forRoot(), SecurityModule.forRoot()
  ],
  // exports: [
  //
  //   // Angular modules
  //   CommonModule, FormsModule, ReactiveFormsModule,
  //
  //   // Inception modules
  //   CoreModule, DialogModule, LayoutModule, SecurityModule
  // ],
  providers: [

    // Resolvers
    ExpiredPasswordTitleResolver, ForgottenPasswordTitleResolver, LoginTitleResolver, ResetPasswordTitleResolver,
    SelectOrganizationTitleResolver
  ]
})
export class LoginViewsModule {
}
