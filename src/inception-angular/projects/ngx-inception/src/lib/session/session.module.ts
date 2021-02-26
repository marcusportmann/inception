/*
 * Copyright 2021 Marcus Portmann
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

import {CommonModule} from '@angular/common';
import {ModuleWithProviders, NgModule} from '@angular/core';
import {RouterModule} from '@angular/router';
import {CoreModule} from '../core/core.module';
import {HasAuthorityDirective} from "./directives/has-authority.directive";
import {SessionService} from "./services/session.service";
import {HTTP_INTERCEPTORS} from "@angular/common/http";
import {SessionInterceptor} from "./services/session.interceptor";

/**
 * The SessionModule class implements the Inception Session Module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [
    // Components

    // Directives
    HasAuthorityDirective
  ],
  imports: [
    // Angular modules
    CommonModule, RouterModule,

    // Inception modules
    CoreModule
  ],
  exports: [
    // Angular modules
    CommonModule, RouterModule,

    // Inception modules
    CoreModule,

    // Components

    // Directives
    HasAuthorityDirective
  ],
  entryComponents: []
})
export class SessionModule {
  constructor() {
  }

  static forRoot(): ModuleWithProviders<SessionModule> {
    return {
      ngModule: SessionModule,
      providers: [{
        provide: HTTP_INTERCEPTORS,
        useClass: SessionInterceptor,
        multi: true
      },
        SessionService
      ]
    };
  }
}
