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
import {FormControl, FormGroup, Validators} from '@angular/forms';
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

  organizationFormControl: FormControl;

  selectOrganizationForm: FormGroup;

  filteredOrganizations$: Subject<Organization[]> = new ReplaySubject<Organization[]>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private sessionService: SessionService) {

    // Initialise the form controls
    this.organizationFormControl = new FormControl('', Validators.required);

    // Initialise the form
    this.selectOrganizationForm = new FormGroup({
      organization: this.organizationFormControl
    });
  }

  displayOrganization(organization: Organization): string {
    return organization.name;
  }

  isOrganizationSelected(): boolean {
    if (this.selectOrganizationForm.valid) {
      if (this.organizationFormControl.value as Organization) {
        if (typeof (this.organizationFormControl.value.id) !== 'undefined' && this.organizationFormControl.value.id !== null) {
          return true;
        }
      }
    }

    return false;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(first(), map(() => window.history.state))
      .subscribe((state) => {
        if (state.organizations) {
          this.subscriptions.add(this.organizationFormControl.valueChanges.pipe(startWith(''), map((value) => {
            this.filteredOrganizations$.next(this.filterOrganizations(state.organizations, value));
          })).subscribe());
        } else {
          console.log('No organizations found, invalidating session and redirecting to the application root');

          // TODO: Invalidate session -- MARCUS

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        }
      });
  }

  ok(): void {
    if (this.selectOrganizationForm.valid) {
      this.sessionService.session$
        .pipe(first())
        .subscribe((session: Session | null) => {
          if (session) {
            if (typeof this.organizationFormControl.value === 'object') {
              session.organization = (this.organizationFormControl.value as Organization);

              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['/']);
            }
          }
        });
    }
  }

  private filterOrganizations(organizations: Organization[], value: string | Organization): Organization[] {
    let filterValue = '';

    if (typeof value === 'string') {
      filterValue = (value as string).toLowerCase();
    } else if (typeof value === 'object') {
      filterValue = (value as Organization).name.toLowerCase();
    }

    return organizations.filter(organization => organization.name.toLowerCase().indexOf(filterValue) === 0);
  }
}
