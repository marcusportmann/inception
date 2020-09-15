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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {debounceTime, first, map, startWith} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {ReferenceService} from "ngx-inception";
import {Country} from "ngx-inception";
import {EmploymentStatus} from "ngx-inception";
import {EmploymentType} from "ngx-inception";
import {IdentityDocumentType} from "ngx-inception";
import {Gender} from "ngx-inception";
import {Language} from "ngx-inception";
import {MaritalStatus} from "ngx-inception";
import {MarriageType} from "ngx-inception";
import {MinorType} from "ngx-inception";
import {NextOfKinType} from "ngx-inception";
import {Occupation} from "ngx-inception";
import {Race} from "ngx-inception";
import {ResidencePermitType} from "ngx-inception";
import {ResidencyStatus} from "ngx-inception";
import {Region} from "ngx-inception";
import {ResidentialType} from "ngx-inception";
import {SourceOfFunds} from "ngx-inception";
import {TaxNumberType} from "ngx-inception";
import {Title} from "ngx-inception";
import {VerificationMethod} from "ngx-inception";
import {VerificationStatus} from "ngx-inception";
import {ContactMechanismSubType} from "ngx-inception";
import {ContactMechanismType} from "ngx-inception";

/**
 * The ReferenceFormComponent class implements the example form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'reference-form.component.html'
})
export class ReferenceFormComponent implements OnInit, OnDestroy {

  referenceForm: FormGroup;

  filteredContactMechanismSubTypes$: Subject<ContactMechanismSubType[]> = new ReplaySubject<ContactMechanismSubType[]>();

  filteredContactMechanismTypes$: Subject<ContactMechanismType[]> = new ReplaySubject<ContactMechanismType[]>();

  filteredCountries$: Subject<Country[]> = new ReplaySubject<Country[]>();

  filteredEmploymentStatuses$: Subject<EmploymentStatus[]> = new ReplaySubject<EmploymentStatus[]>();

  filteredEmploymentTypes$: Subject<EmploymentType[]> = new ReplaySubject<EmploymentType[]>();

  filteredGenders$: Subject<Gender[]> = new ReplaySubject<Gender[]>();

  filteredIdentityDocumentTypes$: Subject<IdentityDocumentType[]> = new ReplaySubject<IdentityDocumentType[]>();

  filteredLanguages$: Subject<Language[]> = new ReplaySubject<Language[]>();

  filteredMaritalStatuses$: Subject<MaritalStatus[]> = new ReplaySubject<MaritalStatus[]>();

  filteredMarriageTypes$: Subject<MarriageType[]> = new ReplaySubject<MarriageType[]>();

  filteredMinorTypes$: Subject<MinorType[]> = new ReplaySubject<MinorType[]>();

  filteredNextOfKinTypes$: Subject<NextOfKinType[]> = new ReplaySubject<NextOfKinType[]>();

  filteredOccupations$: Subject<Occupation[]> = new ReplaySubject<Occupation[]>();

  filteredRaces$: Subject<Race[]> = new ReplaySubject<Race[]>();

  filteredRegions$: Subject<Region[]> = new ReplaySubject<Region[]>();

  filteredResidencePermitTypes$: Subject<ResidencePermitType[]> = new ReplaySubject<ResidencePermitType[]>();

  filteredResidencyStatuses$: Subject<ResidencyStatus[]> = new ReplaySubject<ResidencyStatus[]>();

  filteredResidentialTypes$: Subject<ResidentialType[]> = new ReplaySubject<ResidentialType[]>();

  filteredSourcesOfFunds$: Subject<SourceOfFunds[]> = new ReplaySubject<SourceOfFunds[]>();

  filteredTaxNumberTypes$: Subject<TaxNumberType[]> = new ReplaySubject<TaxNumberType[]>();

  filteredTitles$: Subject<Title[]> = new ReplaySubject<Title[]>();

  filteredVerificationMethods$: Subject<VerificationMethod[]> = new ReplaySubject<VerificationMethod[]>();

  filteredVerificationStatuses$: Subject<VerificationStatus[]> = new ReplaySubject<VerificationStatus[]>();

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private referenceService: ReferenceService) {

    this.referenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // tslint:disable-next-line
      contactMechanismSubType: ['', Validators.required],
      contactMechanismType: ['', Validators.required],
      country: ['', Validators.required],
      employmentStatus: ['', Validators.required],
      employmentType: ['', Validators.required],
      gender: ['', Validators.required],
      identityDocumentType: ['', Validators.required],
      language: ['', Validators.required],
      maritalStatus: ['', Validators.required],
      marriageType: ['', Validators.required],
      minorType: ['', Validators.required],
      nextOfKinType: ['', Validators.required],
      occupation: ['', Validators.required],
      race: ['', Validators.required],
      region: ['', Validators.required],
      residencePermitType: ['', Validators.required],
      residencyStatus: ['', Validators.required],
      residentialType: ['', Validators.required],
      sourceOfFunds: ['', Validators.required],
      taxNumberType: ['', Validators.required],
      title: ['', Validators.required],
      verificationMethod: ['', Validators.required],
      verificationStatus: ['', Validators.required]
    });
  }

  displayContactMechanismSubType(contactMechanismSubType: ContactMechanismSubType): string {
    if (!!contactMechanismSubType) {
      return contactMechanismSubType.name;
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

  displayCountry(country: Country): string {
    if (!!country) {
      return country.name;
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

  displayLanguage(language: Language): string {
    if (!!language) {
      return language.name;
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

  displayMinorType(minorType: MinorType): string {
    if (!!minorType) {
      return minorType.name;
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

  displayRegion(region: Region): string {
    if (!!region) {
      return region.name;
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

  displaySourceOfFunds(sourceOfFunds: SourceOfFunds): string {
    if (!!sourceOfFunds) {
      return sourceOfFunds.name;
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

  displayVerificationMethod(verificationMethod: VerificationMethod): string {
    if (!!verificationMethod) {
      return verificationMethod.name;
    } else {
      return '';
    }
  }

  displayVerificationStatus(verificationStatus: VerificationStatus): string {
    if (!!verificationStatus) {
      return verificationStatus.name;
    } else {
      return '';
    }
  }

  ngOnInit(): void {
    this.referenceService.getContactMechanismSubTypes().pipe(first()).subscribe((contactMechanismSubTypes: ContactMechanismSubType[]) => {
      const contactMechanismSubTypeControl = this.referenceForm.get('contactMechanismSubType');

      if (contactMechanismSubTypeControl) {
        this.subscriptions.add(contactMechanismSubTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | ContactMechanismSubType) => {
            if (typeof (value) === 'string') {
              this.filteredContactMechanismSubTypes$.next(contactMechanismSubTypes.filter(
                contactMechanismSubType => contactMechanismSubType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredContactMechanismSubTypes$.next(contactMechanismSubTypes.filter(
                contactMechanismSubType => contactMechanismSubType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getContactMechanismTypes().pipe(first()).subscribe((contactMechanismTypes: ContactMechanismType[]) => {
      const contactMechanismTypeControl = this.referenceForm.get('contactMechanismType');

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

    this.referenceService.getCountries().pipe(first()).subscribe((countries: Country[]) => {
      const countryControl = this.referenceForm.get('country');

      if (countryControl) {
        this.subscriptions.add(countryControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Country) => {
            if (typeof (value) === 'string') {
              this.filteredCountries$.next(countries.filter(
                country => country.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredCountries$.next(countries.filter(
                country => country.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getEmploymentStatuses().pipe(first()).subscribe((employmentStatuses: EmploymentStatus[]) => {
      const employmentStatusControl = this.referenceForm.get('employmentStatus');

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

    this.referenceService.getEmploymentTypes().pipe(first()).subscribe((employmentTypes: EmploymentType[]) => {
      const employmentTypeControl = this.referenceForm.get('employmentType');

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

    this.referenceService.getGenders().pipe(first()).subscribe((genders: Gender[]) => {
      const genderControl = this.referenceForm.get('gender');

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

    this.referenceService.getIdentityDocumentTypes().pipe(first()).subscribe((identityDocumentTypes: IdentityDocumentType[]) => {
      const identityDocumentTypeControl = this.referenceForm.get('identityDocumentType');

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

    this.referenceService.getLanguages().pipe(first()).subscribe((languages: Language[]) => {
      const languageControl = this.referenceForm.get('language');

      if (languageControl) {
        this.subscriptions.add(languageControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Language) => {
            if (typeof (value) === 'string') {
              this.filteredLanguages$.next(languages.filter(
                language => language.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredLanguages$.next(languages.filter(
                language => language.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });


    this.referenceService.getMaritalStatuses().pipe(first()).subscribe((maritalStatuses: MaritalStatus[]) => {
      const maritalStatusControl = this.referenceForm.get('maritalStatus');

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

    this.referenceService.getMarriageTypes().pipe(first()).subscribe((marriageTypes: MarriageType[]) => {
      const marriageTypeControl = this.referenceForm.get('marriageType');

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

    this.referenceService.getMinorTypes().pipe(first()).subscribe((minorTypes: MinorType[]) => {
      const minorTypeControl = this.referenceForm.get('minorType');

      if (minorTypeControl) {
        this.subscriptions.add(minorTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | MinorType) => {
            if (typeof (value) === 'string') {
              this.filteredMinorTypes$.next(minorTypes.filter(
                minorType => minorType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredMinorTypes$.next(minorTypes.filter(
                minorType => minorType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getNextOfKinTypes().pipe(first()).subscribe((nextOfKinTypes: NextOfKinType[]) => {
      const nextOfKinTypeControl = this.referenceForm.get('nextOfKinType');

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

    this.referenceService.getOccupations().pipe(first()).subscribe((occupations: Occupation[]) => {
      const occupationControl = this.referenceForm.get('occupation');

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

    this.referenceService.getRaces().pipe(first()).subscribe((races: Race[]) => {
      const raceControl = this.referenceForm.get('race');

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

    this.referenceService.getRegions().pipe(first()).subscribe((regions: Region[]) => {
      const regionControl = this.referenceForm.get('region');

      if (regionControl) {
        this.subscriptions.add(regionControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | Region) => {
            if (typeof (value) === 'string') {
              this.filteredRegions$.next(regions.filter(
                region => region.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredRegions$.next(regions.filter(
                region => region.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getResidencePermitTypes().pipe(first()).subscribe((residencePermitTypes: ResidencePermitType[]) => {
      const residencePermitTypeControl = this.referenceForm.get('residencePermitType');

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

    this.referenceService.getResidencyStatuses().pipe(first()).subscribe((residencyStatuses: ResidencyStatus[]) => {
      const residencyStatusControl = this.referenceForm.get('residencyStatus');

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

    this.referenceService.getResidentialTypes().pipe(first()).subscribe((residentialTypes: ResidentialType[]) => {
      const residentialTypeControl = this.referenceForm.get('residentialType');

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

    this.referenceService.getSourcesOfFunds().pipe(first()).subscribe((sourceOfFunds: SourceOfFunds[]) => {
      const sourceOfFundsControl = this.referenceForm.get('sourceOfFunds');

      if (sourceOfFundsControl) {
        this.subscriptions.add(sourceOfFundsControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | SourceOfFunds) => {
            if (typeof (value) === 'string') {
              this.filteredSourcesOfFunds$.next(sourceOfFunds.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredSourcesOfFunds$.next(sourceOfFunds.filter(
                sourceOfFunds => sourceOfFunds.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getTaxNumberTypes().pipe(first()).subscribe((taxNumberTypes: TaxNumberType[]) => {
      const taxNumberTypeControl = this.referenceForm.get('taxNumberType');

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

    this.referenceService.getTitles().pipe(first()).subscribe((titles: Title[]) => {
      const titleControl = this.referenceForm.get('title');

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

    this.referenceService.getVerificationMethods().pipe(first()).subscribe((verificationMethods: VerificationMethod[]) => {
      const verificationMethodControl = this.referenceForm.get('verificationMethod');

      if (verificationMethodControl) {
        this.subscriptions.add(verificationMethodControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | VerificationMethod) => {
            if (typeof (value) === 'string') {
              this.filteredVerificationMethods$.next(verificationMethods.filter(
                verificationMethod => verificationMethod.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredVerificationMethods$.next(verificationMethods.filter(
                verificationMethod => verificationMethod.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });

    this.referenceService.getVerificationStatuses().pipe(first()).subscribe((verificationStatuses: VerificationStatus[]) => {
      const verificationStatusControl = this.referenceForm.get('verificationStatus');

      if (verificationStatusControl) {
        this.subscriptions.add(verificationStatusControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | VerificationStatus) => {
            if (typeof (value) === 'string') {
              this.filteredVerificationStatuses$.next(verificationStatuses.filter(
                verificationStatus => verificationStatus.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              this.filteredVerificationStatuses$.next(verificationStatuses.filter(
                verificationStatus => verificationStatus.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });
  }

  ok(): void {
    console.log('Contact Mechanism Sub Type = ', this.referenceForm.get('contactMechanismSubType')!.value);
    console.log('Contact Mechanism Type = ', this.referenceForm.get('contactMechanismType')!.value);
    console.log('Country = ', this.referenceForm.get('country')!.value);
    console.log('Employment Status = ', this.referenceForm.get('employmentStatus')!.value);
    console.log('Employment Type = ', this.referenceForm.get('employmentType')!.value);
    console.log('Gender = ', this.referenceForm.get('gender')!.value);
    console.log('Identity Document Type = ', this.referenceForm.get('identityDocumentType')!.value);
    console.log('Language = ', this.referenceForm.get('language')!.value);
    console.log('Marital Status = ', this.referenceForm.get('maritalStatus')!.value);
    console.log('Marriage Type = ', this.referenceForm.get('marriageType')!.value);
    console.log('Minor Type = ', this.referenceForm.get('minorType')!.value);
    console.log('Next Of Kin Type = ', this.referenceForm.get('nextOfKinType')!.value);
    console.log('Occupation = ', this.referenceForm.get('occupation')!.value);
    console.log('Race = ', this.referenceForm.get('race')!.value);
    console.log('Region = ', this.referenceForm.get('region')!.value);
    console.log('Residence Permit Type = ', this.referenceForm.get('residencePermitType')!.value);
    console.log('Residency Status = ', this.referenceForm.get('residencyStatus')!.value);
    console.log('Residential Type = ', this.referenceForm.get('residentialType')!.value);
    console.log('Source Of Funds = ', this.referenceForm.get('sourceOfFunds')!.value);
    console.log('Tax Number Type = ', this.referenceForm.get('taxNumberType')!.value);
    console.log('Title = ', this.referenceForm.get('title')!.value);
    console.log('Verification Method = ', this.referenceForm.get('verificationMethod')!.value);
    console.log('Verification Status = ', this.referenceForm.get('verificationStatus')!.value);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

}
