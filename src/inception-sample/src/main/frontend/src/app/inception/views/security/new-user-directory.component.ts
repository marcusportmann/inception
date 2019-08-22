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
import {UserDirectory} from '../../services/security/user-directory';

/**
 * The NewUserDirectoryComponent class implements the new user directory component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-user-directory.component.html',
  styleUrls: ['new-user-directory.component.css'],
})
export class NewUserDirectoryComponent extends AdminContainerView implements AfterViewInit {

  newUserDirectoryForm: FormGroup;

  userDirectory?: UserDirectory;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise form
    this.newUserDirectoryForm = new FormGroup({
      name: new FormControl('', [Validators.maxLength(4000)])
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
        id: '@@new_user_directory_component_back_title',
        value: 'User Directories'
      }), ['..'],
      {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@new_user_directory_component_title',
      value: 'New User Directory'
    })
  }

  ngAfterViewInit(): void {
    this.userDirectory = new UserDirectory(uuid(), '', '');
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'],
      {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.userDirectory && this.newUserDirectoryForm.valid) {
      this.userDirectory.name = this.newUserDirectoryForm.get('name')!.value;

      this.spinnerService.showSpinner();

      this.securityService.createUserDirectory(this.userDirectory)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'],
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
