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
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SecurityService} from '../../services/security/security.service';
import {combineLatest} from 'rxjs';
import {UserDirectoryType} from '../../services/security/user-directory-type';
import {Group} from "../../services/security/group";
import {GroupMembers} from "../../services/security/group-members";

/**
 * The EditGroupComponent class implements the edit group component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-group.component.html',
  styleUrls: ['edit-group.component.css'],
})
export class EditGroupComponent extends AdminContainerView implements AfterViewInit {

  editGroupForm: FormGroup;

  group?: Group;

  userDirectoryType?: UserDirectoryType;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.editGroupForm = new FormGroup({
      description: new FormControl('', [Validators.maxLength(100)]),
      name: new FormControl({
        value: '',
        disabled: true
      })
    });
  }

  get backNavigation(): BackNavigation {
    const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

    return new BackNavigation(this.i18n({
      id: '@@edit_group_component_back_title',
      value: 'Groups'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@edit_group_component_title',
      value: 'Edit Group'
    })
  }

  ngAfterViewInit(): void {
    const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    const name = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('name')!);

    // Retrieve the existing group and initialise the form fields
    this.spinnerService.showSpinner();

    combineLatest([this.securityService.getUserDirectoryTypeForUserDirectory(userDirectoryId),
      this.securityService.getGroup(userDirectoryId, name)
    ])
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((results: [UserDirectoryType, Group]) => {
        this.userDirectoryType = results[0];

        this.group = results[1];

        this.editGroupForm.get('description')!.setValue(results[1].description);
        this.editGroupForm.get('name')!.setValue(results[1].name);
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
    this.router.navigate(['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId}
    });
  }

  onOK(): void {
    if (this.group && this.editGroupForm.valid) {
      this.group.description = this.editGroupForm.get('description')!.value;

      this.spinnerService.showSpinner();

      this.securityService.updateGroup(this.group)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          const userDirectoryId = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../../..'], {
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
