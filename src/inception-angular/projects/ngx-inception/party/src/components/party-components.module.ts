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
import {FieldOfStudyInputComponent} from './field-of-study-input.component';
import {GenderInputComponent} from './gender-input.component';
import {IdentityDocumentTypeInputComponent} from './identity-document-type-input.component';
import {
  IndustryClassificationCategoryInputComponent
} from './industry-classification-category-input.component';
import {IndustryClassificationInputComponent} from './industry-classification-input.component';
import {
  IndustryClassificationSystemInputComponent
} from './industry-classification-system-input.component';
import {LockTypeCategoryInputComponent} from './lock-type-category-input.component';
import {LockTypeInputComponent} from './lock-type-input.component';
import {MaritalStatusInputComponent} from './marital-status-input.component';
import {MarriageTypeInputComponent} from './marriage-type-input.component';
import {NextOfKinTypeInputComponent} from './next-of-kin-type-input.component';
import {OccupationInputComponent} from './occupation-input.component';
import {PhysicalAddressPurposeInputComponent} from './physical-address-purpose-input.component';
import {PhysicalAddressRoleInputComponent} from './physical-address-role-input.component';
import {PhysicalAddressTypeInputComponent} from './physical-address-type-input.component';
import {PreferenceTypeCategoryInputComponent} from './preference-type-category-input.component';
import {PreferenceTypeInputComponent} from './preference-type-input.component';
import {QualificationTypeInputComponent} from './qualification-type-input.component';
import {RaceInputComponent} from './race-input.component';
import {ResidencePermitTypeInputComponent} from './residence-permit-type-input.component';
import {ResidencyStatusInputComponent} from './residency-status-input.component';
import {ResidentialTypeInputComponent} from './residential-type-input.component';
import {RolePurposeInputComponent} from './role-purpose-input.component';
import {RoleTypeInputComponent} from './role-type-input.component';
import {SegmentInputComponent} from './segment-input.component';
import {SegmentationTypeInputComponent} from './segmentation-type-input.component';
import {SkillTypeInputComponent} from './skill-type-input.component';
import {SourceOfFundsTypeInputComponent} from './source-of-funds-type-input.component';
import {SourceOfWealthTypeInputComponent} from './source-of-wealth-type-input.component';
import {StatusTypeCategoryInputComponent} from './status-type-category-input.component';
import {StatusTypeInputComponent} from './status-type-input.component';
import {TaxNumberTypeInputComponent} from './tax-number-type-input.component';
import {TimeToContactInputComponent} from './time-to-contact-input.component';
import {TitleInputComponent} from './title-input.component';

@NgModule({
  declarations: [
    // Components
    AssociationPropertyTypeInputComponent, AssociationTypeInputComponent,
    AttributeTypeCategoryInputComponent, AttributeTypeInputComponent, ConsentTypeInputComponent,
    ContactMechanismPurposeInputComponent, ContactMechanismRoleInputComponent,
    ContactMechanismTypeInputComponent, EmploymentStatusInputComponent,
    EmploymentTypeInputComponent, ExternalReferenceTypeInputComponent,
    FieldOfStudyInputComponent, GenderInputComponent, IdentityDocumentTypeInputComponent,
    IndustryClassificationCategoryInputComponent, IndustryClassificationInputComponent,
    IndustryClassificationSystemInputComponent, LockTypeCategoryInputComponent,
    LockTypeInputComponent, MaritalStatusInputComponent, MarriageTypeInputComponent,
    NextOfKinTypeInputComponent, OccupationInputComponent, PhysicalAddressPurposeInputComponent,
    PhysicalAddressRoleInputComponent, PhysicalAddressTypeInputComponent,
    PreferenceTypeCategoryInputComponent, PreferenceTypeInputComponent,
    QualificationTypeInputComponent,
    RaceInputComponent, ResidencePermitTypeInputComponent, ResidencyStatusInputComponent,
    ResidentialTypeInputComponent, RolePurposeInputComponent,
    RoleTypeInputComponent, SegmentInputComponent, SegmentationTypeInputComponent,
    SkillTypeInputComponent, SourceOfFundsTypeInputComponent, SourceOfWealthTypeInputComponent,
    StatusTypeCategoryInputComponent, StatusTypeInputComponent, TaxNumberTypeInputComponent,
    TimeToContactInputComponent, TitleInputComponent
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
    FieldOfStudyInputComponent, GenderInputComponent, IdentityDocumentTypeInputComponent,
    IndustryClassificationCategoryInputComponent, IndustryClassificationInputComponent,
    IndustryClassificationSystemInputComponent, LockTypeCategoryInputComponent,
    LockTypeInputComponent, MaritalStatusInputComponent, MarriageTypeInputComponent,
    NextOfKinTypeInputComponent, OccupationInputComponent, PhysicalAddressPurposeInputComponent,
    PhysicalAddressRoleInputComponent, PhysicalAddressTypeInputComponent,
    PreferenceTypeCategoryInputComponent, PreferenceTypeInputComponent,
    QualificationTypeInputComponent,
    RaceInputComponent, ResidencePermitTypeInputComponent, ResidencyStatusInputComponent,
    ResidentialTypeInputComponent, RolePurposeInputComponent,
    RoleTypeInputComponent, SegmentInputComponent, SegmentationTypeInputComponent,
    SkillTypeInputComponent, SourceOfFundsTypeInputComponent, SourceOfWealthTypeInputComponent,
    StatusTypeCategoryInputComponent, StatusTypeInputComponent, TaxNumberTypeInputComponent,
    TimeToContactInputComponent, TitleInputComponent
  ]
})
export class PartyComponentsModule {
}



