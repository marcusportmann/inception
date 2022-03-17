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
import {MatInputModule} from '@angular/material/input';
import {PartyServicesModule} from '../services/party-services.module';
import {AssociationPropertyTypeInputComponent} from './association-property-type-input.component';
import {AssociationTypeInputComponent} from './association-type-input.component';
import {AttributeTypeCategoryInputComponent} from './attribute-type-category-input.component';
import {AttributeTypeInputComponent} from './attribute-type-input.component';
import {ConsentTypeInputComponent} from './consent-type-input.component';
import {ContactMechanismPurposeInputComponent} from './contact-mechanism-purpose-input.component';
import {ContactMechanismRoleInputComponent} from './contact-mechanism-role-input.component';
import {ContactMechanismTypeInputComponent} from './contact-mechanism-type-input.component';
import {EmploymentStatusInputComponent} from './employment-status-input.component';
import {EmploymentTypeInputComponent} from './employment-type-input.component';
import {ExternalReferenceTypeInputComponent} from './external-reference-type-input.component';
import {TitleInputComponent} from './title-input.component';

@NgModule({
  declarations: [
    // Components
    AssociationPropertyTypeInputComponent, AssociationTypeInputComponent,
    AttributeTypeCategoryInputComponent, AttributeTypeInputComponent, ConsentTypeInputComponent,
    ContactMechanismPurposeInputComponent, ContactMechanismRoleInputComponent,
    ContactMechanismTypeInputComponent, EmploymentStatusInputComponent,
    EmploymentTypeInputComponent, ExternalReferenceTypeInputComponent,
    TitleInputComponent
  ],
  imports: [
    // Angular modules
    CommonModule,

    // Inception modules
    PartyServicesModule,

    // Material modules
    MatAutocompleteModule, MatInputModule
  ],
  exports: [
    AssociationPropertyTypeInputComponent, AssociationTypeInputComponent,
    AttributeTypeCategoryInputComponent, AttributeTypeInputComponent, ConsentTypeInputComponent,
    ContactMechanismPurposeInputComponent, ContactMechanismRoleInputComponent,
    ContactMechanismTypeInputComponent, EmploymentStatusInputComponent,
    EmploymentTypeInputComponent, ExternalReferenceTypeInputComponent,
    TitleInputComponent
  ]
})
export class PartyComponentsModule {
}



