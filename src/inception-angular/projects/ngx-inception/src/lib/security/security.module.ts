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

import {ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {CoreModule} from '../core/core.module';
import {CanActivateFunctionGuard, DisabledFunctionGuard} from './routing';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import {SecurityService} from './services/security.service';
import {SessionInterceptor} from './services/session.interceptor';
import {SessionService} from './services/session.service';
import {HasAuthorityDirective} from './directives';

/**
 * The SecurityModule class implements the Inception Security Module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [

    // Directives
    HasAuthorityDirective
  ],
  imports: [

    // Angular modules
    CommonModule, HttpClientModule,

    // Inception modules
    CoreModule.forRoot()
  ],
  exports: [

    // Angular modules
    CommonModule, HttpClientModule,

    // Inception modules
    CoreModule,

    // Directives
    HasAuthorityDirective
  ]
})
export class SecurityModule {
  constructor() {
    console.log('Initialising the Inception Security Module');
  }

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: SecurityModule,
      providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: SessionInterceptor,
        multi: true
      },

        // Services
        SecurityService, SessionService,

        // Function Guards
        CanActivateFunctionGuard, DisabledFunctionGuard
      ]
    };
  }
}