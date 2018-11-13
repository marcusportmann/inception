/*
 * Copyright 2018 Marcus Portmann
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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {first, flatMap, map, startWith} from "rxjs/operators";
import {Observable} from "../../../../../node_modules/rxjs";
import {Organization} from "../../services/security/organization";
import {SessionService} from "../../services/session/session.service";
import {Router} from "@angular/router";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {Session} from "../../services/session/session";

@Component({
  templateUrl: 'select-organization.component.html'
})
export class SelectOrganizationComponent implements OnInit {

  selectOrganizationForm: FormGroup;

  filteredOrganizations: Observable<Organization[]>;

  constructor(private formBuilder: FormBuilder, private i18n: I18n, private sessionService: SessionService, private router: Router) {

    this.selectOrganizationForm = this.formBuilder.group({
      organization: ['', Validators.required]
    });
  }

  displayOrganization(organization: Organization): string {
    return organization.name;
  }

  isOrganizationSelected(): boolean {
    return this.selectOrganizationForm.valid && (typeof this.selectOrganizationForm.get('organization').value == 'object');
  }

  ngOnInit() {
    this.filteredOrganizations = this.selectOrganizationForm.get('organization').valueChanges.pipe(
      startWith(''),
      flatMap((value) => this._filterOrganizations(value))
    );
  }

  onSubmit() {

    if (this.selectOrganizationForm.valid && (typeof this.selectOrganizationForm.get('organization').value == 'object')) {

      const selectedOrganization: Organization = <Organization>this.selectOrganizationForm.get('organization').value;

      this.sessionService.session.pipe(first()).subscribe((session: Session) => {

        session.organization = selectedOrganization;

        this.router.navigate(['/']);
      });
    }
  }

  private _filterOrganizations(value: string | object): Observable<Organization[]> {
    let filterValue = '';

    if (typeof value === "string") {
      filterValue = (<string>value).toLowerCase();
    }
    else if (typeof value === 'object') {
      filterValue = (<Organization>value).name.toLowerCase();
    }

    return this.sessionService.session.pipe(
      map((session: Session) => {
        if (session) {
          return session.organizations.filter(
            organization => organization.name.toLowerCase().indexOf(filterValue) === 0)
        }
        else {
          this.router.navigate(['/login']);
          return [];
        }
      })
    );
  }
}
