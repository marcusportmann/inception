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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  ContactMechanismRole, ContactMechanismType, EmploymentStatus, EmploymentType, Gender,
  IdentityDocumentType, MaritalStatus, MarriageType, NextOfKinType, Occupation,
  PartyReferenceService, Race, ResidencePermitType, ResidencyStatus, ResidentialType,
  SourceOfFundsType, TaxNumberType, Title
} from "ngx-inception/party";
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

/**
 * The PartyReferenceFormComponent class implements the party reference form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'party-reference-form.component.html'
})
export class PartyReferenceFormComponent implements OnInit, OnDestroy {

  filteredContactMechanismRoles$: Subject<ContactMechanismRole[]> = new ReplaySubject<ContactMechanismRole[]>();
  filteredContactMechanismTypes$: Subject<ContactMechanismType[]> = new ReplaySubject<ContactMechanismType[]>();
  filteredEmploymentStatuses$: Subject<EmploymentStatus[]> = new ReplaySubject<EmploymentStatus[]>();
  filteredEmploymentTypes$: Subject<EmploymentType[]> = new ReplaySubject<EmploymentType[]>();
  filteredGenders$: Subject<Gender[]> = new ReplaySubject<Gender[]>();
  filteredIdentityDocumentTypes$: Subject<IdentityDocumentType[]> = new ReplaySubject<IdentityDocumentType[]>();
  filteredMaritalStatuses$: Subject<MaritalStatus[]> = new ReplaySubject<MaritalStatus[]>();
  filteredMarriageTypes$: Subject<MarriageType[]> = new ReplaySubject<MarriageType[]>();
  filteredNextOfKinTypes$: Subject<NextOfKinType[]> = new ReplaySubject<NextOfKinType[]>();
  filteredOccupations$: Subject<Occupation[]> = new ReplaySubject<Occupation[]>();
  filteredRaces$: Subject<Race[]> = new ReplaySubject<Race[]>();
  filteredResidencePermitTypes$: Subject<ResidencePermitType[]> = new ReplaySubject<ResidencePermitType[]>();
  filteredResidencyStatuses$: Subject<ResidencyStatus[]> = new ReplaySubject<ResidencyStatus[]>();
  filteredResidentialTypes$: Subject<ResidentialType[]> = new ReplaySubject<ResidentialType[]>();
  filteredSourceOfFundsTypes$: Subject<SourceOfFundsType[]> = new ReplaySubject<SourceOfFundsType[]>();
  filteredTaxNumberTypes$: Subject<TaxNumberType[]> = new ReplaySubject<TaxNumberType[]>();
  filteredTitles$: Subject<Title[]> = new ReplaySubject<Title[]>();
  partyReferenceForm: FormGroup;
  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private partyReferenceService: PartyReferenceService) {

    this.partyReferenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // tslint:disable-next-line
      contactMechanismRole: ['', Validators.required],
      contactMechanismType: ['', Validators.required],
      employmentStatus: ['', Validators.required],
      employmentType: ['', Validators.required],
      gender: ['', Validators.required],
      identityDocumentType: ['', Validators.required],
      maritalStatus: ['', Validators.required],
      marriageType: ['', Validators.required],
      minorType: ['', Validators.required],
      nextOfKinType: ['', Validators.required],
      occupation: ['', Validators.required],
      race: ['', Validators.required],
      residencePermitType: ['', Validators.required],
      residencyStatus: ['', Validators.required],
      residentialType: ['', Validators.required],
      sourceOfFundsType: ['', Validators.required],
      taxNumberType: ['', Validators.required],
      title: ['', Validators.required]
    });
  }

  displayContactMechanismRole(contactMechanismRole: ContactMechanismRole): string {
    if (!!contactMechanismRole) {
      return contactMechanismRole.name;
    } else {
      return '';
    }
  }

  displayContactMechanismType(contactMechanismType: ContactMechanismType): string {
    if (!!contactMechanismType) {
      return contactMechanismType.name;
    } else {
      return '';
    }
  }

  displayEmploymentStatus(employmentStatus: EmploymentStatus): string {
    if (!!employmentStatus) {
      return employmentStatus.name;
    } else {
      return '';
    }
  }

  displayEmploymentType(employmentType: EmploymentType): string {
    if (!!employmentType) {
      return employmentType.name;
    } else {
      return '';
    }
  }

  displayGender(gender: Gender): string {
    if (!!gender) {
      return gender.name;
    } else {
      return '';
    }
  }

  displayIdentityDocumentType(identityDocumentType: IdentityDocumentType): string {
    if (!!identityDocumentType) {
      return identityDocumentType.name;
    } else {
      return '';
    }
  }

  displayMaritalStatus(maritalStatus: MaritalStatus): string {
    if (!!maritalStatus) {
      return maritalStatus.name;
    } else {
      return '';
    }
  }

  displayMarriageType(marriageType: MarriageType): string {
    if (!!marriageType) {
      return marriageType.name;
    } else {
      return '';
    }
  }

  displayNextOfKinType(nextOfKinType: NextOfKinType): string {
    if (!!nextOfKinType) {
      return nextOfKinType.name;
    } else {
      return '';
    }
  }

  displayOccupation(occupation: Occupation): string {
    if (!!occupation) {
      return occupation.name;
    } else {
      return '';
    }
  }

  displayRace(race: Race): string {
    if (!!race) {
      return race.name;
    } else {
      return '';
    }
  }

  displayResidencePermitType(residencePermitType: ResidencePermitType): string {
    if (!!residencePermitType) {
      return residencePermitType.name;
    } else {
      return '';
    }
  }

  displayResidencyStatus(residencyStatus: ResidencyStatus): string {
    if (!!residencyStatus) {
      return residencyStatus.name;
    } else {
      return '';
    }
  }

  displayResidentialType(residentialType: ResidentialType): string {
    if (!!residentialType) {
      return residentialType.name;
    } else {
      return '';
    }
  }

  displaySourceOfFundsType(sourceOfFundsType: SourceOfFundsType): string {
    if (!!sourceOfFundsType) {
      return sourceOfFundsType.name;
    } else {
      return '';
    }
  }

  displayTaxNumberType(taxNumberType: TaxNumberType): string {
    if (!!taxNumberType) {
      return taxNumberType.name;
    } else {
      return '';
    }
  }

  displayTitle(title: Title): string {
    if (!!title) {
      return title.name;
    } else {
      return '';
    }
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.partyReferenceService.getContactMechanismRoles().pipe(first()).subscribe((contactMechanismRoles: ContactMechanismRole[]) => {
      const contactMechanismRoleControl = this.partyReferenceForm.get('contactMechanismRole');

      if (contactMechanismRoleControl) {
        this.subscriptions.add(contactMechanismRoleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismRole) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismRoles$.next(contactMechanismRoles.filter(
                contactMechanismRole => contactMechanismRole.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismRoles$.next(contactMechanismRoles.filter(
                contactMechanismRole => contactMechanismRole.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getContactMechanismTypes().pipe(first()).subscribe((contactMechanismTypes: ContactMechanismType[]) => {
      const contactMechanismTypeControl = this.partyReferenceForm.get('contactMechanismType');

      if (contactMechanismTypeControl) {
        this.subscriptions.add(contactMechanismTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismType) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismTypes$.next(contactMechanismTypes.filter(
                contactMechanismType => contactMechanismType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismTypes$.next(contactMechanismTypes.filter(
                contactMechanismType => contactMechanismType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentStatuses().pipe(first()).subscribe((employmentStatuses: EmploymentStatus[]) => {
      const employmentStatusControl = this.partyReferenceForm.get('employmentStatus');

      if (employmentStatusControl) {
        this.subscriptions.add(employmentStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentStatus) => {
            if (typeof (value) === 'string') {
              this.filteredEmploymentStatuses$.next(employmentStatuses.filter(
                employmentStatus => employmentStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredEmploymentStatuses$.next(employmentStatuses.filter(
                employmentStatus => employmentStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getEmploymentTypes().pipe(first()).subscribe((employmentTypes: EmploymentType[]) => {
      const employmentTypeControl = this.partyReferenceForm.get('employmentType');

      if (employmentTypeControl) {
        this.subscriptions.add(employmentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | EmploymentType) => {
            if (typeof (value) === 'string') {
              this.filteredEmploymentTypes$.next(employmentTypes.filter(
                employmentType => employmentType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredEmploymentTypes$.next(employmentTypes.filter(
                employmentType => employmentType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getGenders().pipe(first()).subscribe((genders: Gender[]) => {
      const genderControl = this.partyReferenceForm.get('gender');

      if (genderControl) {
        this.subscriptions.add(genderControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Gender) => {
            if (typeof (value) === 'string') {
              this.filteredGenders$.next(genders.filter(
                gender => gender.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredGenders$.next(genders.filter(
                gender => gender.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getIdentityDocumentTypes().pipe(first()).subscribe((identityDocumentTypes: IdentityDocumentType[]) => {
      const identityDocumentTypeControl = this.partyReferenceForm.get('identityDocumentType');

      if (identityDocumentTypeControl) {
        this.subscriptions.add(identityDocumentTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | IdentityDocumentType) => {
            if (typeof (value) === 'string') {
              this.filteredIdentityDocumentTypes$.next(identityDocumentTypes.filter(
                identityDocumentType => identityDocumentType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredIdentityDocumentTypes$.next(identityDocumentTypes.filter(
                identityDocumentType => identityDocumentType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getMaritalStatuses().pipe(first()).subscribe((maritalStatuses: MaritalStatus[]) => {
      const maritalStatusControl = this.partyReferenceForm.get('maritalStatus');

      if (maritalStatusControl) {
        this.subscriptions.add(maritalStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MaritalStatus) => {
            if (typeof (value) === 'string') {
              this.filteredMaritalStatuses$.next(maritalStatuses.filter(
                maritalStatus => maritalStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredMaritalStatuses$.next(maritalStatuses.filter(
                maritalStatus => maritalStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getMarriageTypes().pipe(first()).subscribe((marriageTypes: MarriageType[]) => {
      const marriageTypeControl = this.partyReferenceForm.get('marriageType');

      if (marriageTypeControl) {
        this.subscriptions.add(marriageTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MarriageType) => {
            if (typeof (value) === 'string') {
              this.filteredMarriageTypes$.next(marriageTypes.filter(
                marriageType => marriageType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredMarriageTypes$.next(marriageTypes.filter(
                marriageType => marriageType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getNextOfKinTypes().pipe(first()).subscribe((nextOfKinTypes: NextOfKinType[]) => {
      const nextOfKinTypeControl = this.partyReferenceForm.get('nextOfKinType');

      if (nextOfKinTypeControl) {
        this.subscriptions.add(nextOfKinTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | NextOfKinType) => {
            if (typeof (value) === 'string') {
              this.filteredNextOfKinTypes$.next(nextOfKinTypes.filter(
                nextOfKinType => nextOfKinType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredNextOfKinTypes$.next(nextOfKinTypes.filter(
                nextOfKinType => nextOfKinType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getOccupations().pipe(first()).subscribe((occupations: Occupation[]) => {
      const occupationControl = this.partyReferenceForm.get('occupation');

      if (occupationControl) {
        this.subscriptions.add(occupationControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Occupation) => {
            if (typeof (value) === 'string') {
              this.filteredOccupations$.next(occupations.filter(
                occupation => occupation.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredOccupations$.next(occupations.filter(
                occupation => occupation.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getRaces().pipe(first()).subscribe((races: Race[]) => {
      const raceControl = this.partyReferenceForm.get('race');

      if (raceControl) {
        this.subscriptions.add(raceControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Race) => {
            if (typeof (value) === 'string') {
              this.filteredRaces$.next(races.filter(
                race => race.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRaces$.next(races.filter(
                race => race.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencePermitTypes().pipe(first()).subscribe((residencePermitTypes: ResidencePermitType[]) => {
      const residencePermitTypeControl = this.partyReferenceForm.get('residencePermitType');

      if (residencePermitTypeControl) {
        this.subscriptions.add(residencePermitTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencePermitType) => {
            if (typeof (value) === 'string') {
              this.filteredResidencePermitTypes$.next(residencePermitTypes.filter(
                permitType => permitType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidencePermitTypes$.next(residencePermitTypes.filter(
                permitType => permitType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidencyStatuses().pipe(first()).subscribe((residencyStatuses: ResidencyStatus[]) => {
      const residencyStatusControl = this.partyReferenceForm.get('residencyStatus');

      if (residencyStatusControl) {
        this.subscriptions.add(residencyStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidencyStatus) => {
            if (typeof (value) === 'string') {
              this.filteredResidencyStatuses$.next(residencyStatuses.filter(
                residentialStatus => residentialStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidencyStatuses$.next(residencyStatuses.filter(
                residentialStatus => residentialStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getResidentialTypes().pipe(first()).subscribe((residentialTypes: ResidentialType[]) => {
      const residentialTypeControl = this.partyReferenceForm.get('residentialType');

      if (residentialTypeControl) {
        this.subscriptions.add(residentialTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ResidentialType) => {
            if (typeof (value) === 'string') {
              this.filteredResidentialTypes$.next(residentialTypes.filter(
                residentialType => residentialType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredResidentialTypes$.next(residentialTypes.filter(
                residentialType => residentialType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getSourceOfFundsTypes().pipe(first()).subscribe((sourceOfFundsTypes: SourceOfFundsType[]) => {
      const sourceOfFundsTypeControl = this.partyReferenceForm.get('sourceOfFundsType');

      if (sourceOfFundsTypeControl) {
        this.subscriptions.add(sourceOfFundsTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfFundsType) => {
            if (typeof (value) === 'string') {
              this.filteredSourceOfFundsTypes$.next(sourceOfFundsTypes.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSourceOfFundsTypes$.next(sourceOfFundsTypes.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getTaxNumberTypes().pipe(first()).subscribe((taxNumberTypes: TaxNumberType[]) => {
      const taxNumberTypeControl = this.partyReferenceForm.get('taxNumberType');

      if (taxNumberTypeControl) {
        this.subscriptions.add(taxNumberTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | TaxNumberType) => {
            if (typeof (value) === 'string') {
              this.filteredTaxNumberTypes$.next(taxNumberTypes.filter(
                taxNumberType => taxNumberType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredTaxNumberTypes$.next(taxNumberTypes.filter(
                taxNumberType => taxNumberType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.partyReferenceService.getTitles().pipe(first()).subscribe((titles: Title[]) => {
      const titleControl = this.partyReferenceForm.get('title');

      if (titleControl) {
        this.subscriptions.add(titleControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Title) => {
            if (typeof (value) === 'string') {
              this.filteredTitles$.next(titles.filter(
                title => title.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredTitles$.next(titles.filter(
                title => title.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });
  }

  ok(): void {
    console.log('Contact Mechanism Sub Type = ', this.partyReferenceForm.get('contactMechanismRole')!.value);
    console.log('Contact Mechanism Type = ', this.partyReferenceForm.get('contactMechanismType')!.value);
    console.log('Employment Status = ', this.partyReferenceForm.get('employmentStatus')!.value);
    console.log('Employment Type = ', this.partyReferenceForm.get('employmentType')!.value);
    console.log('Gender = ', this.partyReferenceForm.get('gender')!.value);
    console.log('Identity Document Type = ', this.partyReferenceForm.get('identityDocumentType')!.value);
    console.log('Marital Status = ', this.partyReferenceForm.get('maritalStatus')!.value);
    console.log('Marriage Type = ', this.partyReferenceForm.get('marriageType')!.value);
    console.log('Next Of Kin Type = ', this.partyReferenceForm.get('nextOfKinType')!.value);
    console.log('Occupation = ', this.partyReferenceForm.get('occupation')!.value);
    console.log('Race = ', this.partyReferenceForm.get('race')!.value);
    console.log('Residence Permit Type = ', this.partyReferenceForm.get('residencePermitType')!.value);
    console.log('Residency Status = ', this.partyReferenceForm.get('residencyStatus')!.value);
    console.log('Residential Type = ', this.partyReferenceForm.get('residentialType')!.value);
    console.log('Source Of Funds = ', this.partyReferenceForm.get('sourceOfFunds')!.value);
    console.log('Tax Number Type = ', this.partyReferenceForm.get('taxNumberType')!.value);
    console.log('Title = ', this.partyReferenceForm.get('title')!.value);
  }

}
