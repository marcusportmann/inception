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
import {ObserversModule} from '@angular/cdk/observers';
import {CommonModule, HashLocationStrategy, LocationStrategy} from '@angular/common';
import {MatButtonModule} from '@angular/material/button';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MAT_FORM_FIELD_DEFAULT_OPTIONS, MatFormFieldModule} from '@angular/material/form-field';
import {MatRadioModule} from '@angular/material/radio';
import {MAT_MOMENT_DATE_ADAPTER_OPTIONS, MomentDateAdapter} from '@angular/material-moment-adapter';
import {setInceptionInjector} from './inception-injector';
import {FormsModule} from '@angular/forms';
import {FileUploadComponent} from './components/file-upload.component';
import {TableFilterComponent} from './components/table-filter.component';
import {AutofocusDirective} from './directives/autofocus.directive';
import {ValidatedFormDirective} from './directives/validated-form.directive';
import {GroupFormFieldComponent} from "./components/group-form-field.component";

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
    FileUploadComponent, GroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutofocusDirective, ValidatedFormDirective
  ],
  imports: [

    // Angular modules
    CommonModule, FormsModule,

    // CDK modules
    ObserversModule,

    // Material modules
    MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatRadioModule
  ],
  exports: [

    // Angular modules
    CommonModule, FormsModule,

    // Material modules
    MatButtonModule, MatCheckboxModule, MatFormFieldModule, MatRadioModule,

    // Components
    FileUploadComponent, GroupFormFieldComponent, TableFilterComponent,

    // Directives
    AutofocusDirective, ValidatedFormDirective,
  ]
})
export class CoreModule {
  constructor(injector: Injector) {
    setInceptionInjector(injector);
  }

  static forRoot(): ModuleWithProviders<CoreModule> {
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
