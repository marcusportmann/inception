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

import {AfterViewInit, Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {SecurityService} from '../../services/security/security.service';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {v4 as uuid} from 'uuid';
import {Organization} from '../../services/security/organization';
import {OrganizationStatus} from '../../services/security/organization-status';
import {User} from '../../services/security/user';

/**
 * The EditOrganizationComponent class implements the edit organization component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-organization.component.html',
  styleUrls: ['edit-organization.component.css'],
})
export class EditOrganizationComponent extends AdminContainerView implements AfterViewInit {

  editOrganizationForm: FormGroup;

  organization?: Organization;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.editOrganizationForm = new FormGroup({
      name: new FormControl('',       [Validators.required, Validators.maxLength(4000)])
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
        id: '@@edit_organization_component_back_title',
        value: 'Organizations'
      }), ['../..'],
      {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@edit_organization_component_title',
      value: 'Edit Organization'
    })
  }

  ngAfterViewInit(): void {
    const organizationId = decodeURIComponent(
      this.activatedRoute.snapshot.paramMap.get('organizationId')!);

    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getOrganization(organizationId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((organization: Organization) => {
        this.organization = organization;

        this.editOrganizationForm.get('name')!.setValue(organization.name);
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'],
      {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.organization && this.editOrganizationForm.valid) {
      this.organization.name = this.editOrganizationForm.get('name')!.value;

      this.spinnerService.showSpinner();

      this.securityService.updateOrganization(this.organization)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'],
            {relativeTo: this.activatedRoute});
        }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
    }
  }
}
