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
import {UserDirectoryType} from '../../services/security/user-directory-type';
import {Group} from "../../services/security/group";
import {v4 as uuid} from 'uuid';

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

  userDirectoryType?: UserDirectoryType;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.newGroupForm = new FormGroup({
      description: new FormControl('', [Validators.maxLength(100)]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
    });
  }

  get backNavigation(): BackNavigation {
    const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    return new BackNavigation(this.i18n({
      id: '@@new_group_component_back_title',
      value: 'Groups'
    }), ['../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@new_group_component_title',
      value: 'New Group'
    })
  }

  ngAfterViewInit(): void {
    const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectoryType: UserDirectoryType) => {
        this.userDirectoryType = userDirectoryType;

        this.group = new Group(uuid(), userDirectoryId, '', '');
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
    const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId}
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
          const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {
            relativeTo: this.activatedRoute,
            state: {userDirectoryId}
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
