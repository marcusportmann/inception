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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {AbstractControl, FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first, map, startWith} from 'rxjs/operators';
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {Organization} from '../../services/security/organization';
import {SessionService} from '../../services/session/session.service';
import {ActivatedRoute, Router} from '@angular/router';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Session} from '../../services/session/session';

/**
 * The SelectOrganizationComponent class implements the select organization component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'select-organization.component.html'
})
export class SelectOrganizationComponent implements OnInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  selectOrganizationForm: FormGroup;

  filteredOrganizations: Subject<Organization[]> = new ReplaySubject<Organization[]>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private sessionService: SessionService) {
    this.selectOrganizationForm = this.formBuilder.group({
      organization: [{value: ''}, Validators.required]
    });
  }

  get organizationFormControl(): AbstractControl {
    return this.selectOrganizationForm.get('organization');
  }

  displayOrganization(organization: Organization): string {
    return organization.name;
  }

  isOrganizationSelected(): boolean {
    return this.selectOrganizationForm.valid &&
      (typeof this.organizationFormControl.value === 'object') &&
      (!!this.organizationFormControl.value.id);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(first(), map((state: any) => window.history.state))
      .subscribe((state: any) => {
        if (state.organizations) {
          this.subscriptions.add(this.organizationFormControl.valueChanges.pipe(startWith(''),
            map((value:any, index: number) => {
              this.filteredOrganizations.next(this.filterOrganizations(state.organizations, value));
            }
            )).subscribe());
        } else {
          console.log('No organizations found, invalidating session and redirecting to the application root');

          // TODO: Invalidate session -- MARCUS

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        }
      });
  }

  onOk(): void {
    if (this.selectOrganizationForm.valid &&
      (typeof this.organizationFormControl.value === 'object')) {
      const selectedOrganization: Organization = <Organization>this.organizationFormControl.value;

      this.sessionService.session
        .pipe(first())
        .subscribe((session: Session) => {
          session.organization = selectedOrganization;

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        });
    }
  }

  private filterOrganizations(organizations: Organization[], value: string | object): Organization[] {
    let filterValue = '';

    if (typeof value === 'string') {
      filterValue = (<string>value).toLowerCase();
    } else if (typeof value === 'object') {
      filterValue = (<Organization>value).name.toLowerCase();
    }

    return organizations.filter(
      organization => organization.name.toLowerCase().indexOf(filterValue) === 0);
  }
}
