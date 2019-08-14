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

import {Component} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {ConfigurationService} from '../../services/configuration/configuration.service';
import {Configuration} from '../../services/configuration/configuration';
import {ConfigurationServiceError} from '../../services/configuration/configuration.service.errors';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';

/**
 * The EditUserComponent class implements the edit user component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-user.component.html',
  styleUrls: ['edit-user.component.css'],
})
export class EditUserComponent extends AdminContainerView {

  // xxxFormControl: FormControl;

  editUserForm: FormGroup;

  userDirectoryId: string;

  username: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private configurationService: ConfigurationService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!;
    this.username = this.activatedRoute.snapshot.paramMap.get('username')!;

    // Initialise form controls
    // this.xxxFormControl = new FormControl('',
    //   [Validators.required, Validators.maxLength(4000)]);

    // Initialise form
    this.editUserForm = new FormGroup({
//      xxx: this.xxxFormControl,
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@edit_user_component_back_title',
      value: 'Users'
    }), ['../../..'], {relativeTo: this.activatedRoute, state: {userDirectoryId: this.userDirectoryId}});
  }

  get title(): string {
    return this.i18n({
      id: '@@edit_user_component_title',
      value: 'Edit User'
    })
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../../..'], {relativeTo: this.activatedRoute, state: {userDirectoryId: this.userDirectoryId}});
  }

  onOK(): void {
    if (this.editUserForm.valid) {

      // const configuration: Configuration = new Configuration(this.keyFormControl.value,
      //   this.valueFormControl.value, this.descriptionFormControl.value);

      // this.spinnerService.showSpinner();

      // this.configurationService.saveConfiguration(configuration)
      //   .pipe(first())
      //   .subscribe(() => {
      //     this.spinnerService.hideSpinner();
      //
      //     // noinspection JSIgnoredPromiseFromCall
      //     this.router.navigate(['..'], {relativeTo: this.activatedRoute});
      //   }, (error: Error) => {
      //     this.spinnerService.hideSpinner();
      //     // noinspection SuspiciousTypeOfGuard
      //     if ((error instanceof ConfigurationServiceError) || (error instanceof AccessDeniedError) ||
      //       (error instanceof SystemUnavailableError)) {
      //       // noinspection JSIgnoredPromiseFromCall
      //       this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      //     } else {
      //       this.dialogService.showErrorDialog(error);
      //     }
      //   });
    }
  }
}
