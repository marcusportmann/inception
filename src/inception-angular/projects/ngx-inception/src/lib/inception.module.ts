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

import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {HttpClientModule} from '@angular/common/http';
import {Injector, ModuleWithProviders, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import '@angular/localize/init'
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS} from '@angular/material/form-field';
import {RouterModule} from '@angular/router';
import {CoreModule} from './core/core.module';
import {DialogModule} from './dialog/dialog.module';
import {INCEPTION_CONFIG, InceptionConfig} from './inception-config';
import {setInceptionInjector} from './inception-injector';
import {LayoutModule} from './layout/layout.module';
import {NgxInceptionComponent} from './ngx-inception.component';
import {CanActivateFunctionGuard} from './session/routing/can-activate-function-guard';
import {DisabledFunctionGuard} from './session/routing/disabled-function-guard';
import {SessionModule} from "./session/session.module";

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
  declarations: [
    // Components
    NgxInceptionComponent
  ],
  imports: [
    // Angular modules
    CommonModule, FormsModule, ReactiveFormsModule, RouterModule,

    // Inception modules
    CoreModule, DialogModule.forRoot(), LayoutModule.forRoot(), SessionModule.forRoot(),
  ],
  exports: [
    // Angular modules
    CommonModule, FormsModule, HttpClientModule, ReactiveFormsModule, RouterModule,

    // Inception modules
    CoreModule, DialogModule, LayoutModule, SessionModule,

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
      },
        // Function Guards
        CanActivateFunctionGuard, DisabledFunctionGuard
      ]
    };
  }
}

