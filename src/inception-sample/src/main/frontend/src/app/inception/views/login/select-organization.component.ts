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

import {Component, OnInit, ViewContainerRef} from '@angular/core';
import {FormGroup, Validators, FormBuilder} from '@angular/forms';

import {InceptionModule} from '../../inception.module';


import {patternValidator} from "../../validators/pattern-validator";
import {SecurityService} from '../../services/security/security.service';


import {HttpClient, HttpErrorResponse} from "@angular/common/http";

import {ErrorReportingService} from "../../services/error-reporting/error-reporting.service";
import {catchError, flatMap, map, startWith} from "rxjs/operators";
import {Observable, of} from "../../../../../node_modules/rxjs";

import {Organization} from "../../services/security/organization";
import {SessionService} from "../../services/session/session.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Error} from "../../errors/error";
import {SpinnerService} from "../../services/layout/spinner.service";
import {LoginError} from "../../services/session/session.service.errors";
import {DialogService} from "../../services/dialog/dialog.service";
import {CommunicationError} from "../../errors/communication-error";



import {I18n} from "@ngx-translate/i18n-polyfill";
import {Session} from "../../services/session/session";




@Component({
  templateUrl: 'select-organization.component.html'
})
export class SelectOrganizationComponent implements OnInit{

  selectOrganizationForm: FormGroup;

  filteredOrganizations: Observable<Organization[]>;

  constructor(private formBuilder: FormBuilder, private i18n: I18n, private sessionService: SessionService, private router: Router) {

    this.selectOrganizationForm = this.formBuilder.group({
      organization: ['', Validators.required]
    });
  }

  displayOrganization(organizationId: string): string {
    return organizationId ? organizationId: undefined;
  }

  ngOnInit() {

    this.filteredOrganizations = this.selectOrganizationForm.get('organization').valueChanges.pipe(
      startWith(''),
      flatMap((value) => this._filterOrganizations(value))
    );
  }

  public onSubmit() {

    if (this.selectOrganizationForm.valid) {

      console.log('this.selectOrganizationForm.get(\'organization\').value = ', this.selectOrganizationForm.get('organization').value);

      // TODO: Save the selected organization in the session.

      this.router.navigate(['/']);
    }
  }

  private _filterOrganizations(value: string): Observable<Organization[]> {
    const filterValue = value.toLowerCase();

    return this.sessionService.getSession().pipe(
      map((session: Session) => {
        return session.organizations.filter(
          organization => organization.name.toLowerCase().indexOf(filterValue) === 0);
      })
    )
  }
}
