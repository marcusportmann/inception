/*
 * Copyright 2022 Marcus Portmann
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {MatAutocompleteModule} from '@angular/material/autocomplete';
import {MatChipsModule} from '@angular/material/chips';
import {MatInputModule} from '@angular/material/input';
import {ReferenceServicesModule} from '../services/reference-services.module';
import {CountriesChipListComponent} from './countries-chip-list.component';
import {CountryInputComponent} from './country-input.component';
import {LanguageInputComponent} from './language-input.component';
import {RegionInputComponent} from './region-input.component';
import {TimeZoneInputComponent} from './time-zone-input.component';

@NgModule({
  declarations: [
    // Components
    CountriesChipListComponent, CountryInputComponent, LanguageInputComponent, RegionInputComponent,
    TimeZoneInputComponent
  ],
  imports: [
    // Angular modules
    CommonModule,

    // Inception modules
    ReferenceServicesModule,

    // Material modules
    MatAutocompleteModule, MatChipsModule, MatInputModule
  ],
  exports: [
    CountriesChipListComponent, CountryInputComponent, LanguageInputComponent, RegionInputComponent,
    TimeZoneInputComponent
  ]
})
export class ReferenceComponentsModule {
}



