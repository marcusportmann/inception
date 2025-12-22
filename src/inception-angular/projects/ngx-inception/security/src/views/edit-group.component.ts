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

import { AfterViewInit, Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, CoreModule, ValidatedFormDirective
} from 'ngx-inception/core';
import { combineLatest } from 'rxjs';
import { finalize, first } from 'rxjs/operators';
import { Group } from '../services/group';
import { SecurityService } from '../services/security.service';
import { UserDirectoryCapabilities } from '../services/user-directory-capabilities';

/**
 * The EditGroupComponent class implements the edit group component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-edit-group',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'edit-group.component.html',
  styleUrls: ['edit-group.component.css']
})
export class EditGroupComponent extends AdminContainerView implements AfterViewInit {
  descriptionControl: FormControl;

  editGroupForm: FormGroup;

  group: Group | null = null;

  groupName: string;

  nameControl: FormControl;

  readonly title = $localize`:@@security_edit_group_title:Edit Group`;

  userDirectoryCapabilities?: UserDirectoryCapabilities;

  userDirectoryId: string;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    const userDirectoryId = this.activatedRoute.snapshot.paramMap.get('userDirectoryId');

    if (!userDirectoryId) {
      throw new globalThis.Error('No userDirectoryId route parameter found');
    }

    this.userDirectoryId = userDirectoryId;

    const groupName = this.activatedRoute.snapshot.paramMap.get('groupName');

    if (!groupName) {
      throw new globalThis.Error('No groupName route parameter found');
    }

    this.groupName = groupName;

    // Initialize the form controls
    this.descriptionControl = new FormControl('', [Validators.maxLength(100)]);
    this.nameControl = new FormControl({
      value: '',
      disabled: true
    });

    // Initialize the form
    this.editGroupForm = new FormGroup({
      description: this.descriptionControl,
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_edit_group_back_navigation:Groups`, ['.'], {
      relativeTo: this.activatedRoute.parent?.parent,
      state: { userDirectoryId: this.userDirectoryId }
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], {
      relativeTo: this.activatedRoute.parent?.parent,
      state: { userDirectoryId: this.userDirectoryId }
    });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing group and initialize the form fields
    this.spinnerService.showSpinner();

    combineLatest([
      this.securityService.getUserDirectoryCapabilities(this.userDirectoryId),
      this.securityService.getGroup(this.userDirectoryId, this.groupName)
    ])
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: ([userDirectoryCapabilities, group]: [UserDirectoryCapabilities, Group]) => {
          this.userDirectoryCapabilities = userDirectoryCapabilities;
          this.group = group;

          this.descriptionControl.setValue(group.description);
          this.nameControl.setValue(group.name);
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  ok(): void {
    if (!this.group || !this.editGroupForm.valid) {
      return;
    }

    this.group.description = this.descriptionControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .updateGroup(this.group)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], {
            relativeTo: this.activatedRoute.parent?.parent,
            state: { userDirectoryId: this.userDirectoryId }
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
