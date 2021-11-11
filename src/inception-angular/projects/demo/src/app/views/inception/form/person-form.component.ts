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
import {FormBuilder, FormGroup} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  EntityType, PartyReferenceService, PartyService, Person, Snapshots
} from 'ngx-inception/party';
import {forkJoin, Subscription} from "rxjs";
import {first} from 'rxjs/operators';

/**
 * The PersonFormComponent class implements the person form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'person-form.component.html'
})
export class PersonFormComponent implements OnInit, OnDestroy {

  personForm: FormGroup;

  person: Person | null = null;

  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private partyReferenceService: PartyReferenceService,
              private partyService: PartyService) {

    this.personForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // tslint:disable-next-line
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.partyService.getPerson('21166574-6564-468a-b845-8a5c127a4345').pipe(first()).subscribe((person: Person) => {
      this.person = person;

      console.log('person = ', person);
    });
  }

  ok(): void {
    /*

    .pipe(first()).subscribe((organization: Organization) => {
      console.log('organization = ', organization);
    });


    .pipe(first()).subscribe((organizations: Organizations) => {
      console.log('organizations = ', organizations);
    });

    .pipe(first()).subscribe((persons: Persons) => {
      console.log('persons = ', persons);
    });

    .pipe(first()).subscribe((party: Party) => {
      console.log('party = ', party);
    });

    .pipe(first()).subscribe((parties: Parties) => {
      console.log('parties = ', parties);
    });

    .pipe(first()).subscribe((associations: Associations) => {
      console.log('associations = ', associations);
    });

    */





    forkJoin({
      party: this.partyService.getParty('21166574-6564-468a-b845-8a5c127a4345'),
      parties: this.partyService.getParties(),

      organization: this.partyService.getOrganization('0ca47707-1e7e-49d5-87e2-665a047a0980'),
      organizations: this.partyService.getOrganizations(),

      person: this.partyService.getPerson('21166574-6564-468a-b845-8a5c127a4345'),
      persons: this.partyService.getPersons(),

      association: this.partyService.getAssociation('4b3fb77a-201b-48e1-b9da-00e94c15e742'),
      associations: this.partyService.getAssociationsForParty('21166574-6564-468a-b845-8a5c127a4345')



    }).pipe(first()).subscribe((value => {
      console.log(value);
    }));



    this.partyService.getPerson('21166574-6564-468a-b845-8a5c127a4345').pipe(first()).subscribe((person: Person) => {

      person.surname = person.surname + ' Updated';

      this.partyService.updatePerson(person).pipe(first()).subscribe((result: boolean) => {
        if (result) {
          this.partyService.getPerson('21166574-6564-468a-b845-8a5c127a4345').pipe(first()).subscribe((updatedPerson: Person) => {
            console.log('Updated person: ', updatedPerson);
          });

          this.partyService.getSnapshots(EntityType.Person, '21166574-6564-468a-b845-8a5c127a4345', new Date('2016-07-17'), new Date('2025-09-23')).pipe(first()).subscribe((snapshots: Snapshots) => {
            console.log('Snapshots: ', snapshots);
          });
        }
      });
    });


  }
}
