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

import {AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first, pairwise, startWith} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {SecurityService} from '../../services/security/security.service';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {UserDirectory} from '../../services/security/user-directory';
import {UserDirectoryType} from '../../services/security/user-directory-type';
import {Subscription} from 'rxjs';
import {InternalUserDirectoryComponent} from './internal-user-directory.component';
import {LdapUserDirectoryComponent} from './ldap-user-directory.component';
import {v4 as uuid} from 'uuid';

/**
 * The NewUserDirectoryComponent class implements the new user directory component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-user-directory.component.html',
  styleUrls: ['new-user-directory.component.css'],
})
export class NewUserDirectoryComponent extends AdminContainerView implements AfterViewInit,
  OnDestroy {

  private subscriptions: Subscription = new Subscription();

  newUserDirectoryForm: FormGroup;

  @ViewChild(InternalUserDirectoryComponent,
    {static: false}) internalUserDirectory?: InternalUserDirectoryComponent;

  @ViewChild(LdapUserDirectoryComponent,
    {static: false}) ldapUserDirectory?: LdapUserDirectoryComponent;

  userDirectory?: UserDirectory;

  userDirectoryTypes: UserDirectoryType[] = [];

  constructor(private changeDetectorRef: ChangeDetectorRef, private router: Router,
              private activatedRoute: ActivatedRoute,
              private i18n: I18n, private securityService: SecurityService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.newUserDirectoryForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      userDirectoryType: new FormControl('', [Validators.required])
    });

    this.subscriptions.add(this.newUserDirectoryForm.get('userDirectoryType')!.valueChanges
      .pipe(startWith(null), pairwise())
      .subscribe(([previousUserDirectoryType, currentUserDirectoryType]: [string, string]) => {
        this.userDirectoryTypeSelected(previousUserDirectoryType, currentUserDirectoryType);
      }));
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@security_new_user_directory_component_back_title',
      value: 'User Directories'
    }), ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@security_new_user_directory_component_title',
      value: 'New User Directory'
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    this.securityService.getUserDirectoryTypes()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((userDirectoryTypes: UserDirectoryType[]) => {
        this.userDirectoryTypes = userDirectoryTypes;
        this.userDirectory = new UserDirectory(uuid(), '', '', []);
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

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ok(): void {
    if (this.userDirectory && this.newUserDirectoryForm.valid) {
      this.userDirectory.name = this.newUserDirectoryForm.get('name')!.value;
      this.userDirectory.type = this.newUserDirectoryForm.get('userDirectoryType')!.value;

      if (this.internalUserDirectory) {
        this.userDirectory.parameters = this.internalUserDirectory.getParameters();
      }

      this.spinnerService.showSpinner();

      this.securityService.createUserDirectory(this.userDirectory)
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

  userDirectoryTypeSelected(previousUserDirectoryType: string,
                              currentUserDirectoryType: string): void {
    // Clear the user directory parameters if required
    if (!!previousUserDirectoryType) {
      this.userDirectory!.parameters = [];
    }

    // Remove the controls for the user directory types
    this.newUserDirectoryForm.removeControl('internalUserDirectory');
    this.newUserDirectoryForm.removeControl('ldapUserDirectory');

    // Add the appropriate control for the user directory type that was selected
    if (currentUserDirectoryType === 'InternalUserDirectory') {
      this.newUserDirectoryForm.addControl('internalUserDirectory', new FormControl(''));
    } else if (currentUserDirectoryType === 'LDAPUserDirectory') {
      this.newUserDirectoryForm.addControl('ldapUserDirectory', new FormControl(''));
    }

    this.changeDetectorRef.detectChanges();

    // Populate the nested InternalUserDirectoryComponent or LdapUserDirectoryComponent
    if (this.internalUserDirectory) {
      this.internalUserDirectory.setParameters(this.userDirectory!.parameters);
    } else if (this.ldapUserDirectory) {
      this.ldapUserDirectory.setParameters(this.userDirectory!.parameters);
    }
  }
}
