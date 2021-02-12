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

import {Injector, ModuleWithProviders, NgModule} from '@angular/core';
import {NgxInceptionComponent} from './ngx-inception.component';
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {RouterModule} from '@angular/router';
import {HttpClientModule} from '@angular/common/http';
import {INCEPTION_CONFIG, InceptionConfig} from './inception-config';
import {CoreModule} from './core/core.module';
import {LayoutModule} from './layout/layout.module';
import {DialogModule} from "./dialog/dialog.module";
import {SecurityDirectivesModule} from "./security/directives/security-directives.module";
import {SecurityRoutingModule} from "./security/routing/security-routing.module";
import {SecurityServicesModule} from "./security/services/security-services.module";
import {CodesServicesModule} from "./codes/services/codes-services.module";
import {ConfigServicesModule} from "./config/services/config-services.module";
import {ErrorServicesModule} from "./error/services/error-services.module";
import {MailServicesModule} from "./mail/services/mail-services.module";
import {ReportingServicesModule} from "./reporting/services/reporting-services.module";
import {SchedulerServicesModule} from "./scheduler/services/scheduler-services.module";
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from "@angular/material/core";
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from "@angular/material-moment-adapter";
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from "@angular/material/form-field";
import {setInceptionInjector} from "./inception-injector";
import '@angular/localize/init'
import {ReferenceServicesModule} from "./reference/services/reference-services.module";
import {PartyServicesModule} from "./party/services/party-services.module";
import {ReferenceService} from "./reference/services/reference.service";

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const INCEPTION_DATE_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'YYYY-MM-DD',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};

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
    CoreModule, DialogModule.forRoot(), LayoutModule.forRoot(),
    SecurityDirectivesModule, SecurityRoutingModule.forRoot(),

    /*
     * The Inception services modules MUST be imported here and ONLY here to ensure that the
     * services are singletons. These modules should NEVER be imported by any other Inception
     * framework module or application module.
     */
    CodesServicesModule.forRoot(), ConfigServicesModule.forRoot(),
    ErrorServicesModule.forRoot(), MailServicesModule.forRoot(), PartyServicesModule.forRoot(),
    ReferenceServicesModule.forRoot(), ReportingServicesModule.forRoot(),
    SchedulerServicesModule.forRoot(), SecurityServicesModule.forRoot()
  ],
  exports: [
    // Angular modules
    CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule,

    // Inception modules
    CoreModule, DialogModule, LayoutModule, SecurityDirectivesModule, SecurityRoutingModule,

    // Inception services modules
    CodesServicesModule, ConfigServicesModule, ErrorServicesModule, MailServicesModule,
    PartyServicesModule, ReferenceServicesModule, ReportingServicesModule, SchedulerServicesModule,
    SecurityServicesModule,

    // Components
    NgxInceptionComponent
  ]
})
export class InceptionModule {
  constructor(injector: Injector) {
    setInceptionInjector(injector);
  }

  // TODO: MOVE THE PROVISION OF THE INCEPTION CONFIGURATION TO THE APPLICATION MODULE -- MARCUS
  static forRoot(config: InceptionConfig): ModuleWithProviders<InceptionModule> {
    return {
      ngModule: InceptionModule,
      providers: [{
        provide: INCEPTION_CONFIG,
        useValue: config
      }, {
        provide: LocationStrategy,
        useClass: HashLocationStrategy,
      }, {
        provide: DateAdapter,
        useClass: MomentDateAdapter,
        deps: [MAT_DATE_LOCALE, MAT_MOMENT_DATE_ADAPTER_OPTIONS]
      }, {
        provide: MAT_DATE_FORMATS,
        useValue: INCEPTION_DATE_FORMATS
      }, {
        provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
        useValue: {appearance: 'standard'}
      }
      ]
    };
  }
}
