/*
 * Copyright Marcus Portmann
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

import { AfterViewInit, ChangeDetectorRef, Component, OnDestroy, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, Error, ValidatedFormDirective
} from 'ngx-inception/core';
import { UserDirectoryParameter } from 'ngx-inception/security';
import { combineLatest, Subscription } from 'rxjs';
import { finalize, first, pairwise, startWith } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { UserDirectory } from '../services/user-directory';
import { UserDirectoryType } from '../services/user-directory-type';
import { InternalUserDirectoryComponent } from './internal-user-directory.component';
import { LdapUserDirectoryComponent } from './ldap-user-directory.component';

/**
 * The EditUserDirectoryComponent class implements the edit user directory component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-edit-user-directory',
  standalone: true,
  imports: [
    CoreModule,
    ValidatedFormDirective,
    InternalUserDirectoryComponent,
    LdapUserDirectoryComponent
  ],
  templateUrl: 'edit-user-directory.component.html',
  styleUrls: ['edit-user-directory.component.css']
})
export class EditUserDirectoryComponent
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  editUserDirectoryForm: FormGroup;

  @ViewChild(InternalUserDirectoryComponent)
  internalUserDirectoryComponent?: InternalUserDirectoryComponent;

  @ViewChild(LdapUserDirectoryComponent)
  ldapUserDirectoryComponent?: LdapUserDirectoryComponent;

  nameControl: FormControl;

  readonly title = $localize`:@@security_edit_user_directory_title:Edit User Directory`;

  userDirectory: UserDirectory | null = null;

  userDirectoryId: string;

  userDirectoryTypeControl: FormControl;

  userDirectoryTypes: UserDirectoryType[] = [];

  private subscriptions: Subscription = new Subscription();

  constructor(
    private changeDetectorRef: ChangeDetectorRef,
    private securityService: SecurityService
  ) {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw new Error('No userDirectoryId route parameter found');
    }

    this.userDirectoryId = decodeURIComponent(userDirectoryId);

    // Initialize the form controls
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.userDirectoryTypeControl = new FormControl('', [Validators.required]);

    // Initialize the form
    this.editUserDirectoryForm = new FormGroup({
      name: this.nameControl,
      userDirectoryType: this.userDirectoryTypeControl
    });

    this.subscriptions.add(
      this.userDirectoryTypeControl.valueChanges
        .pipe(startWith(null), pairwise())
        .subscribe(([previousUserDirectoryType, currentUserDirectoryType]: [string, string]) => {
          this.userDirectoryTypeSelected(previousUserDirectoryType, currentUserDirectoryType);
        })
    );
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_edit_user_directory_back_navigation:User Directories`,
      ['../..'],
      { relativeTo: this.activatedRoute }
    );
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Retrieve the user directory types and the existing user directory
    this.spinnerService.showSpinner();

    combineLatest([
      this.securityService.getUserDirectoryTypes(),
      this.securityService.getUserDirectory(this.userDirectoryId)
    ])
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: ([userDirectoryTypes, userDirectory]: [UserDirectoryType[], UserDirectory]) => {
          this.userDirectoryTypes = userDirectoryTypes;
          this.userDirectory = userDirectory;

          this.nameControl.setValue(userDirectory.name);
          this.userDirectoryTypeControl.setValue(userDirectory.type);
        },
        error: (error: Error) => this.handleError(error, true, '../..')
      });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ok(): void {
    if (!this.userDirectory || !this.editUserDirectoryForm.valid) {
      return;
    }

    this.userDirectory.name = this.nameControl.value;
    this.userDirectory.type = this.userDirectoryTypeControl.value;

    if (this.internalUserDirectoryComponent) {
      this.userDirectory.parameters = this.internalUserDirectoryComponent.getParameters();
    } else if (this.ldapUserDirectoryComponent) {
      this.userDirectory.parameters = this.ldapUserDirectoryComponent.getParameters();
    }

    this.spinnerService.showSpinner();

    this.securityService
      .updateUserDirectory(this.userDirectory)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['../..'], {
            relativeTo: this.activatedRoute
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  userDirectoryTypeSelected(
    previousUserDirectoryType: string,
    currentUserDirectoryType: string
  ): void {
    // If the user actually changes the type in the UI (after initial load),
    // you may want to clear the parameters.
    if (
      previousUserDirectoryType &&
      currentUserDirectoryType &&
      previousUserDirectoryType !== currentUserDirectoryType &&
      this.userDirectory
    ) {
      this.userDirectory.parameters = [];
    }

    // Remove any existing nested controls
    this.editUserDirectoryForm.removeControl('internalUserDirectory');
    this.editUserDirectoryForm.removeControl('ldapUserDirectory');

    if (!currentUserDirectoryType) {
      this.changeDetectorRef.detectChanges();
      return;
    }

    // Determine initial parameters for this type (edit scenario)
    let initialParams: UserDirectoryParameter[] | null = null;

    if (
      this.userDirectory &&
      this.userDirectory.type === currentUserDirectoryType &&
      this.userDirectory.parameters
    ) {
      initialParams = this.userDirectory.parameters;
    }

    // Add the appropriate control for the selected type
    if (currentUserDirectoryType === 'InternalUserDirectory') {
      this.editUserDirectoryForm.addControl(
        'internalUserDirectory',
        new FormControl<UserDirectoryParameter[] | null>(initialParams, {
          nonNullable: false
        })
      );
    } else if (currentUserDirectoryType === 'LDAPUserDirectory') {
      this.editUserDirectoryForm.addControl(
        'ldapUserDirectory',
        new FormControl<UserDirectoryParameter[] | null>(initialParams, {
          nonNullable: false
        })
      );
    }

    // This causes Angular to instantiate the nested component (via *ngIf)
    // and wire up the value accessor; it will then call writeValue(initialParams)
    // on the CVA, which in turn calls setParameters(...) internally.
    this.changeDetectorRef.detectChanges();
  }
}
