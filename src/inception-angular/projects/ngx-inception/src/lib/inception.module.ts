/*
 * Copyright 2020 Marcus Portmann
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
import {NgxInceptionComponent} from './ngx-inception.component';
import {CommonModule} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {INCEPTION_CONFIG, InceptionConfig} from './inception-config';
import {CoreModule} from './core/core.module';
import {LayoutModule} from './layout/layout.module';
import {CodesModule} from './codes/codes.module';
import {ConfigurationModule} from './configuration/configuration.module';
import {ErrorModule} from './error/error.module';
import {MailModule} from './mail/mail.module';
import {ReportingModule} from './reporting/reporting.module';
import {SchedulerModule} from './scheduler/scheduler.module';
import {SecurityModule} from './security/security.module';

/**
 * The InceptionModule class implements the Inception framework module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [NgxInceptionComponent],
  imports: [

    // Angular modules
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule,

    // Inception modules
    CoreModule.forRoot(), LayoutModule.forRoot(),

    // TODO: DECIDE IF THE APPLICATION MODULE SHOULD IMPORT THESE -- MARCUS
    CodesModule.forRoot(), ConfigurationModule.forRoot(), ErrorModule.forRoot(), MailModule.forRoot(),
    ReportingModule.forRoot(), SchedulerModule.forRoot(), SecurityModule.forRoot()
  ],
  exports: [

    // Angular modules
    CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule,

    // Inception modules
    CoreModule, LayoutModule,

    // TODO: DECIDE IF THE APPLICATION MODULE SHOULD IMPORT THESE -- MARCUS
    CodesModule, ConfigurationModule, ErrorModule, MailModule, ReportingModule, SchedulerModule, SecurityModule,

    // Components
    NgxInceptionComponent
  ]
})
export class InceptionModule {
  constructor() {
  }

  // TODO: MOVE THE PROVISION OF THE INCEPTION CONFIGURATION TO THE APPLICATION MODULE -- MARCUS
  static forRoot(config: InceptionConfig): ModuleWithProviders<InceptionModule> {
    return {
      ngModule: InceptionModule,
      providers: [{
        provide: INCEPTION_CONFIG,
        useValue: config
      }
      ]
    };
  }
}
