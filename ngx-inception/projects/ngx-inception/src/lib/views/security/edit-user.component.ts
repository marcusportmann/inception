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
import {AbstractControl, FormControl, FormGroup, Validators} from '@angular/forms';
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
import {User} from '../../services/security/user';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SecurityService} from '../../services/security/security.service';
import {combineLatest} from 'rxjs';
import {UserDirectoryCapabilities} from '../../services/security/user-directory-capabilities';

/**
 * The EditUserComponent class implements the edit user component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-user.component.html',
  styleUrls: ['edit-user.component.css'],
})
export class EditUserComponent extends AdminContainerView implements AfterViewInit {

  editUserForm: FormGroup;

  emailFormControl: FormControl;

  firstNameFormControl: FormControl;

  lastNameFormControl: FormControl;

  mobileNumberFormControl: FormControl;

  phoneNumberFormControl: FormControl;

  user?: User;

  userDirectoryCapabilities?: UserDirectoryCapabilities;

  userDirectoryId: string;

  username: string;

  usernameFormControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw(new Error('No userDirectoryId route parameter found'));
    }

    this.userDirectoryId = decodeURIComponent(userDirectoryId);

    const username = this.activatedRoute.snapshot.paramMap.get('username');

    if (!username) {
      throw(new Error('No username route parameter found'));
    }

    this.username = decodeURIComponent(username);

    // Initialise the form controls
    this.emailFormControl = new FormControl('', [Validators.maxLength(100), Validators.pattern(// tslint:disable-next-line:max-line-length
      '(?:[a-z0-9!#$%&\'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&\'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\\])')
    ]);
    this.firstNameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.lastNameFormControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.mobileNumberFormControl =
      new FormControl('', [Validators.maxLength(100), Validators.pattern(// tslint:disable-next-line:max-line-length
        '(\\+|00)(297|93|244|1264|358|355|376|971|54|374|1684|1268|61|43|994|257|32|229|226|880|359|973|1242|387|590|375|501|1441|591|55|1246|673|975|267|236|1|61|41|56|86|225|237|243|242|682|57|269|238|506|53|5999|61|1345|357|420|49|253|1767|45|1809|1829|1849|213|593|20|291|212|34|372|251|358|679|500|33|298|691|241|44|995|44|233|350|224|590|220|245|240|30|1473|299|502|594|1671|592|852|504|385|509|36|62|44|91|246|353|98|964|354|972|39|1876|44|962|81|76|77|254|996|855|686|1869|82|383|965|856|961|231|218|1758|423|94|266|370|352|371|853|590|212|377|373|261|960|52|692|389|223|356|95|382|976|1670|258|222|1664|596|230|265|60|262|264|687|227|672|234|505|683|31|47|977|674|64|968|92|507|64|51|63|680|675|48|1787|1939|850|351|595|970|689|974|262|40|7|250|966|249|221|65|500|4779|677|232|503|378|252|508|381|211|239|597|421|386|46|268|1721|248|963|1649|235|228|66|992|690|993|670|676|1868|216|90|688|886|255|256|380|598|1|998|3906698|379|1784|58|1284|1340|84|678|681|685|967|27|260|263)(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{4,20}$')
      ]);
    this.phoneNumberFormControl = new FormControl('', [Validators.maxLength(100)]);
    this.usernameFormControl = new FormControl({
      value: '',
      disabled: true
    });

    // Initialise the form
    this.editUserForm = new FormGroup({
      email: this.emailFormControl,
      firstName: this.firstNameFormControl,
      lastName: this.lastNameFormControl,
      mobileNumber: this.mobileNumberFormControl,
      phoneNumber: this.phoneNumberFormControl,
      username: this.usernameFormControl
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@security_edit_user_component_back_title',
      value: 'Users'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get title(): string {
    return this.i18n({
      id: '@@security_edit_user_component_title',
      value: 'Edit User'
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    combineLatest([this.securityService.getUserDirectoryCapabilities(this.userDirectoryId),
      this.securityService.getUser(this.userDirectoryId, this.username)
    ])
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((results: [UserDirectoryCapabilities, User]) => {
        this.userDirectoryCapabilities = results[0];

        this.user = results[1];

        this.emailFormControl.setValue(results[1].email);
        this.firstNameFormControl.setValue(results[1].firstName);
        this.lastNameFormControl.setValue(results[1].lastName);
        this.mobileNumberFormControl.setValue(results[1].mobileNumber);
        this.phoneNumberFormControl.setValue(results[1].phoneNumber);
        this.usernameFormControl.setValue(results[1].username);

        if (results[0].supportsPasswordExpiry) {
          this.editUserForm.addControl('expirePassword', new FormControl(false));
        }

        if (results[0].supportsUserLocks) {
          this.editUserForm.addControl('lockUser', new FormControl(false));
        }
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  ok(): void {
    if (this.user && this.editUserForm.valid) {
      this.user.firstName = this.firstNameFormControl.value;
      this.user.lastName = this.lastNameFormControl.value;
      this.user.mobileNumber = this.mobileNumberFormControl.value;
      this.user.phoneNumber = this.phoneNumberFormControl.value;
      this.user.email = this.emailFormControl.value;

      this.spinnerService.showSpinner();

      const expirePasswordFormControl: AbstractControl | null = this.editUserForm.get('expirePassword');
      const lockUserFormControl: AbstractControl | null = this.editUserForm.get('lockUser');

      this.securityService.updateUser(this.user, expirePasswordFormControl ? expirePasswordFormControl.value : false,
        lockUserFormControl ? lockUserFormControl.value : false)
        .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
        .subscribe(() => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../../..'], {
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
