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

import {Injector, ModuleWithProviders, NgModule} from '@angular/core';
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {
  DateAdapter,
  MAT_DATE_FORMATS,
  MAT_DATE_LOCALE,
  MAT_FORM_FIELD_DEFAULT_OPTIONS,
  MatCheckboxModule,
  MatFormFieldModule,
  MatRadioModule
} from '@angular/material';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {setInceptionInjector} from './inception-injector';
import {FormsModule} from '@angular/forms';
import {AutofocusDirective, ValidatedFormDirective} from './directives';
import {
  CheckboxFormFieldComponent, FileUploadComponent, RadioGroupFormFieldComponent, TableFilterComponent
} from './components';

// See the Moment.js docs for the meaning of these formats:
// https://momentjs.com/docs/#/displaying/format/
export const INCEPTION_DATE_FORMATS = {
  parse: {
    dateInput: 'LL',
  },
  display: {
    dateInput: 'LL',
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
    CheckboxFormFieldComponent, FileUploadComponent, RadioGroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutofocusDirective, ValidatedFormDirective
  ],
  imports: [

    // Angular modules
    CommonModule, FormsModule,

    // Material modules
    MatCheckboxModule, MatFormFieldModule, MatRadioModule
  ],
  exports: [

    // Angular modules
    CommonModule, FormsModule,

    // Material modules
    MatCheckboxModule, MatFormFieldModule, MatRadioModule,

    // Components
    CheckboxFormFieldComponent, FileUploadComponent, RadioGroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutofocusDirective, ValidatedFormDirective,
  ]
})
export class CoreModule {
  constructor(injector: Injector) {
    console.log('Initialising the Inception Core Module');
    setInceptionInjector(injector);
  }

  static forRoot(): ModuleWithProviders {
    return {
      ngModule: CoreModule,
      providers: [{
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
        provide: MAT_DATE_LOCALE,
        useValue: 'en-GB'
      }, {
        provide: MAT_FORM_FIELD_DEFAULT_OPTIONS,
        useValue: {appearance: 'standard'}
      }
      ]
    };
  }
}
