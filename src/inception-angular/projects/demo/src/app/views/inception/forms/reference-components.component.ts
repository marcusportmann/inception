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

import { ChangeDetectionStrategy, Component, inject } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { CoreModule, ValidatedFormDirective } from 'ngx-inception/core';
import {
  CountriesChipGridComponent, CountryInputComponent, LanguageInputComponent, RegionInputComponent,
  TimeZoneInputComponent
} from 'ngx-inception/reference';

/**
 * The ReferenceComponentsComponent class.
 *
 * @author Marcus Portmann
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [
    CoreModule,
    CountriesChipGridComponent,
    CountryInputComponent,
    LanguageInputComponent,
    RegionInputComponent,
    TimeZoneInputComponent,
    ValidatedFormDirective
  ],
  selector: 'app-reference-components',
  standalone: true,
  templateUrl: 'reference-components.component.html'
})
export class ReferenceComponentsComponent {
  countriesControl: FormControl = new FormControl([], Validators.required);

  countryControl: FormControl = new FormControl('', Validators.required);

  languageControl: FormControl = new FormControl('EN', Validators.required);

  referenceForm: FormGroup;

  regionControl: FormControl = new FormControl('', Validators.required);

  timeZoneControl: FormControl = new FormControl('', Validators.required);

  private readonly formBuilder = inject(FormBuilder);

  constructor() {
    this.referenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      countries: this.countriesControl,
      country: this.countryControl,
      language: this.languageControl,
      region: this.regionControl,
      timeZone: this.timeZoneControl
    });
  }

  ok(): void {
    console.log('Countries = ', this.referenceForm.controls['countries'].value);
    console.log('Country = ', this.referenceForm.controls['country'].value);
    console.log('Language = ', this.referenceForm.controls['language'].value);
    console.log('Region = ', this.referenceForm.controls['region'].value);
    console.log('Time Zone = ', this.referenceForm.controls['timeZone'].value);
  }
}
