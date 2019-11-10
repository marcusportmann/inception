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
import {Group} from "../../services/security/group";
import {UserDirectoryCapabilities} from "../../services/security/user-directory-capabilities";

/**
 * The NewGroupComponent class implements the new group component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-group.component.html',
  styleUrls: ['new-group.component.css'],
})
export class NewGroupComponent extends AdminContainerView implements AfterViewInit {

  newGroupForm: FormGroup;

  group?: Group;

  userDirectoryCapabilities?: UserDirectoryCapabilities;

  userDirectoryId: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // Initialise the form
    this.newGroupForm = new FormGroup({
      description: new FormControl('', [Validators.maxLength(100)]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@security_new_group_component_back_title',
      value: 'Groups'
    }), ['../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@security_new_group_component_title',
      value: 'New Group'
    })
  }

  ngAfterViewInit(): void {
    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getUserDirectoryCapabilities(this.userDirectoryId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectoryCapabilities: UserDirectoryCapabilities) => {
        this.userDirectoryCapabilities = userDirectoryCapabilities;

        this.group = new Group(this.userDirectoryId, '', '');
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
    this.router.navigate(['../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  onOK(): void {
    if (this.group && this.newGroupForm.valid) {

      this.group.name = this.newGroupForm.get('name')!.value;
      this.group.description = this.newGroupForm.get('description')!.value;

      this.spinnerService.showSpinner();

      this.securityService.createGroup(this.group)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {
            relativeTo: this.activatedRoute,
            state: {userDirectoryId: this.userDirectoryId}
          });
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
