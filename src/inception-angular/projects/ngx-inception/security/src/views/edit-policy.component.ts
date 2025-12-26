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
import { Policy } from '../services/policy';
import { PolicyType } from '../services/policy-type';
import { SecurityService } from '../services/security.service';

/**
 * The EditPolicyComponent class implements the edit policy component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-edit-policy',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'edit-policy.component.html',
  styleUrls: ['edit-policy.component.css']
})
export class EditPolicyComponent extends AdminContainerView implements OnInit {
  readonly dataControl: FormControl<string>;

  readonly editPolicyForm: FormGroup<{
    data: FormControl<string>;
    id: FormControl<string>;
    name: FormControl<string>;
    type: FormControl<PolicyType>;
    version: FormControl<string>;
  }>;

  readonly idControl: FormControl<string>;

  readonly nameControl: FormControl<string>;

  policy: Policy | null = null;

  readonly policyId: string;

  readonly title = $localize`:@@security_edit_policy_title:Edit Policy`;

  readonly typeControl: FormControl<PolicyType>;

  readonly versionControl: FormControl<string>;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    const policyId = this.activatedRoute.snapshot.paramMap.get('policyId');
    if (!policyId) {
      throw new globalThis.Error('No policyId route parameter found');
    }
    this.policyId = policyId;

    // Initialize the form controls
    this.dataControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required]
    });

    this.idControl = new FormControl<string>(
      {
        value: '',
        disabled: true
      },
      { nonNullable: true, validators: [Validators.required, Validators.maxLength(100)] }
    );

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    this.typeControl = new FormControl<PolicyType>(PolicyType.XACMLPolicy, {
      nonNullable: true,
      validators: Validators.required
    });

    this.versionControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(30)]
    });

    // Initialize the form
    this.editPolicyForm = new FormGroup({
      data: this.dataControl,
      id: this.idControl,
      name: this.nameControl,
      type: this.typeControl,
      version: this.versionControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_edit_policy_back_navigation:Policies`, ['.'], {
      relativeTo: this.activatedRoute.parent?.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent?.parent });
  }

  ngOnInit(): void {
    // Retrieve the existing policy and initialize the form controls
    this.spinnerService.showSpinner();

    this.securityService
      .getPolicy(this.policyId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (policy: Policy) => {
          this.policy = policy;

          this.idControl.setValue(policy.id);
          this.versionControl.setValue(policy.version);
          this.nameControl.setValue(policy.name);
          this.typeControl.setValue(policy.type);
          this.dataControl.setValue(policy.data);
        },
        error: (error: Error) =>
          this.handleError(error, true, ['.'], { relativeTo: this.activatedRoute.parent?.parent })
      });
  }

  ok(): void {
    if (!this.policy || !this.editPolicyForm.valid) {
      return;
    }

    this.policy.version = this.versionControl.value;
    this.policy.name = this.nameControl.value;
    this.policy.type = this.typeControl.value;
    this.policy.data = this.dataControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .updatePolicy(this.policy)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], {
            relativeTo: this.activatedRoute.parent?.parent
          });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
