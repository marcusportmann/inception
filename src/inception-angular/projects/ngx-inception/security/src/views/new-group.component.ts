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

import { Component, inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Group } from '../services/group';
import { SecurityService } from '../services/security.service';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';

/**
 * The NewGroupComponent class implements the new group component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-new-group',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-group.component.html',
  styleUrls: ['new-group.component.css']
})
export class NewGroupComponent extends AdminContainerView implements OnInit {
  readonly descriptionControl: FormControl<string>;

  group: Group | null = null;

  readonly nameControl: FormControl<string>;

  readonly newGroupForm: FormGroup<{
    description: FormControl<string>;
    name: FormControl<string>;
  }>;

  readonly title = $localize`:@@security_new_group_title:New Group`;

  userDirectoryCapabilities: UserDirectoryCapabilities | null = null;

  readonly userDirectoryId: string;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');
    if (!userDirectoryId) {
      throw new globalThis.Error('No userDirectoryId route parameter found');
    }
    this.userDirectoryId = userDirectoryId;

    // Initialize the form controls
    this.descriptionControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.maxLength(100)]
    });

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    // Initialize the form
    this.newGroupForm = new FormGroup({
      description: this.descriptionControl,
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_new_group_back_navigation:Groups`, ['.'], {
      relativeTo: this.activatedRoute.parent,
      state: { userDirectoryId: this.userDirectoryId }
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], {
      relativeTo: this.activatedRoute.parent,
      state: { userDirectoryId: this.userDirectoryId }
    });
  }

  ngOnInit(): void {
    // Retrieve the user directory capabilities and initialize the form fields
    this.spinnerService.showSpinner();

    this.securityService
      .getUserDirectoryCapabilities(this.userDirectoryId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (userDirectoryCapabilities: UserDirectoryCapabilities) => {
          this.userDirectoryCapabilities = userDirectoryCapabilities;
          this.group = new Group(this.userDirectoryId, '', '');
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ok(): void {
    if (!this.group || !this.newGroupForm.valid) {
      return;
    }

    this.group.name = this.nameControl.value;
    this.group.description = this.descriptionControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .createGroup(this.group)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], {
            relativeTo: this.activatedRoute.parent,
            state: { userDirectoryId: this.userDirectoryId }
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
