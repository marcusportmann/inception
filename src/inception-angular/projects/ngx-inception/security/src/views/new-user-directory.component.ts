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

import { ChangeDetectorRef, Component, inject, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, ValidatedFormDirective
} from 'ngx-inception/core';
import { Subscription } from 'rxjs';
import { debounceTime, finalize, first, pairwise, startWith } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';
import { SecurityService } from '../services/security.service';
import { UserDirectory } from '../services/user-directory';
import { UserDirectoryParameter } from '../services/user-directory-parameter';
import { UserDirectoryType } from '../services/user-directory-type';
import { InternalUserDirectoryComponent } from './internal-user-directory.component';
import { LdapUserDirectoryComponent } from './ldap-user-directory.component';

/**
 * The NewUserDirectoryComponent class implements the new user directory component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-new-user-directory',
  standalone: true,
  imports: [
    CoreModule,
    ValidatedFormDirective,
    InternalUserDirectoryComponent,
    LdapUserDirectoryComponent
  ],
  templateUrl: 'new-user-directory.component.html',
  styleUrls: ['new-user-directory.component.css']
})
export class NewUserDirectoryComponent extends AdminContainerView implements OnInit, OnDestroy {
  @ViewChild(InternalUserDirectoryComponent)
  internalUserDirectoryComponent?: InternalUserDirectoryComponent;

  @ViewChild(LdapUserDirectoryComponent)
  ldapUserDirectoryComponent?: LdapUserDirectoryComponent;

  readonly nameControl: FormControl<string>;

  readonly newUserDirectoryForm: FormGroup<{
    name: FormControl<string>;
    userDirectoryType: FormControl<string | null>;
    internalUserDirectory?: FormControl<UserDirectoryParameter[] | null>;
    ldapUserDirectory?: FormControl<UserDirectoryParameter[] | null>;
  }>;

  readonly title = $localize`:@@security_new_user_directory_title:New User Directory`;

  userDirectory: UserDirectory | null = null;

  readonly userDirectoryTypeControl: FormControl<string | null>;

  userDirectoryTypes: UserDirectoryType[] = [];

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private readonly securityService = inject(SecurityService);

  private readonly subscriptions: Subscription = new Subscription();

  constructor() {
    super();

    // Initialize the form controls
    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.userDirectoryTypeControl = new FormControl<string | null>(null, [Validators.required]);

    // Initialize the form
    this.newUserDirectoryForm = new FormGroup({
      name: this.nameControl,
      userDirectoryType: this.userDirectoryTypeControl
    });

    this.subscriptions.add(
      this.userDirectoryTypeControl.valueChanges
        .pipe(startWith(null), debounceTime(500), pairwise())
        .subscribe(
          ([previousUserDirectoryType, currentUserDirectoryType]: [
            string | null,
            string | null
          ]) => {
            this.userDirectoryTypeSelected(previousUserDirectoryType, currentUserDirectoryType);
          }
        )
    );
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_new_user_directory_back_navigation:User Directories`,
      ['.'],
      { relativeTo: this.activatedRoute.parent }
    );
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.spinnerService.showSpinner();

    this.securityService
      .getUserDirectoryTypes()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (userDirectoryTypes: UserDirectoryType[]) => {
          this.userDirectoryTypes = userDirectoryTypes;
          this.userDirectory = new UserDirectory(uuid(), '', '', []);
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ok(): void {
    if (!this.userDirectory || !this.newUserDirectoryForm.valid) {
      return;
    }

    this.userDirectory.name = this.nameControl.value;
    this.userDirectory.type = this.userDirectoryTypeControl.value || '';

    if (this.internalUserDirectoryComponent) {
      this.userDirectory.parameters = this.internalUserDirectoryComponent.getParameters();
    } else if (this.ldapUserDirectoryComponent) {
      this.userDirectory.parameters = this.ldapUserDirectoryComponent.getParameters();
    }

    this.spinnerService.showSpinner();

    this.securityService
      .createUserDirectory(this.userDirectory)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  userDirectoryTypeSelected(
    previousUserDirectoryType: string | null,
    currentUserDirectoryType: string | null
  ): void {
    // Clear the user directory parameters if required
    if (!!previousUserDirectoryType && this.userDirectory) {
      this.userDirectory.parameters = [];
    }

    // Remove the controls for the user directory types
    this.newUserDirectoryForm.removeControl('internalUserDirectory');
    this.newUserDirectoryForm.removeControl('ldapUserDirectory');

    // Add the appropriate control for the user directory type that was selected
    if (currentUserDirectoryType === 'InternalUserDirectory') {
      this.newUserDirectoryForm.addControl(
        'internalUserDirectory',
        new FormControl<UserDirectoryParameter[] | null>(null, { nonNullable: false })
      );
    } else if (currentUserDirectoryType === 'LDAPUserDirectory') {
      this.newUserDirectoryForm.addControl(
        'ldapUserDirectory',
        new FormControl<UserDirectoryParameter[] | null>(null, { nonNullable: false })
      );
    }

    this.changeDetectorRef.detectChanges();

    // Populate the nested InternalUserDirectoryComponent or LdapUserDirectoryComponent
    if (this.userDirectory) {
      if (this.internalUserDirectoryComponent) {
        this.internalUserDirectoryComponent.setParameters(this.userDirectory.parameters);
      } else if (this.ldapUserDirectoryComponent) {
        this.ldapUserDirectoryComponent.setParameters(this.userDirectory.parameters);
      }
    }
  }
}
