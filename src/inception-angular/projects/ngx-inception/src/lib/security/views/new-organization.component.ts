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
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize, first} from 'rxjs/operators';
import {Error} from '../../core/errors/error';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {SecurityService} from '../services/security.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {BackNavigation} from '../../layout/components/back-navigation';
import {SecurityServiceError} from '../services/security.service.errors';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {SystemUnavailableError} from '../../core/errors/system-unavailable-error';
import {Organization} from '../services/organization';
import {v4 as uuid} from 'uuid';
import {OrganizationStatus} from '../services/organization-status';

/**
 * The NewOrganizationComponent class implements the new organization component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-organization.component.html',
  styleUrls: ['new-organization.component.css'],
})
export class NewOrganizationComponent extends AdminContainerView implements AfterViewInit {

  createUserDirectoryFormControl: FormControl;

  nameFormControl: FormControl;

  newOrganizationForm: FormGroup;

  organization?: Organization;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form controls
    this.createUserDirectoryFormControl = new FormControl(false);
    this.nameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);

    // Initialise the form
    this.newOrganizationForm = new FormGroup({
      createUserDirectory: this.createUserDirectoryFormControl,
      name: this.nameFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation('Organizations', ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return 'New Organization';
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.organization = new Organization(uuid(), '', OrganizationStatus.Active);
  }

  ok(): void {
    if (this.organization && this.newOrganizationForm.valid) {
      this.organization.name = this.nameFormControl.value;

      this.spinnerService.showSpinner();

      this.securityService.createOrganization(this.organization, this.createUserDirectoryFormControl.value)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], {relativeTo: this.activatedRoute});
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
