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
import { finalize, first } from 'rxjs/operators';
import { Policy } from '../services/policy';
import { PolicyType } from '../services/policy-type';
import { SecurityService } from '../services/security.service';

/**
 * The NewPolicyComponent class implements the new policy component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-new-policy',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-policy.component.html',
  styleUrls: ['new-policy.component.css']
})
export class NewPolicyComponent extends AdminContainerView implements AfterViewInit {
  dataControl: FormControl;

  idControl: FormControl;

  nameControl: FormControl;

  newPolicyForm: FormGroup;

  policy: Policy | null = null;

  readonly title = $localize`:@@security_new_policy_title:New Policy`;

  typeControl: FormControl;

  versionControl: FormControl;

  private securityService = inject(SecurityService);

  constructor() {
    super();

    // Initialize the form controls
    this.dataControl = new FormControl('', [Validators.required]);
    this.idControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.typeControl = new FormControl('', [Validators.required]);
    this.versionControl = new FormControl('', [Validators.required, Validators.maxLength(30)]);

    // Initialize the form
    this.newPolicyForm = new FormGroup({
      data: this.dataControl,
      id: this.idControl,
      name: this.nameControl,
      type: this.typeControl,
      version: this.versionControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_new_policy_back_navigation:Policies`, ['..'], {
      relativeTo: this.activatedRoute
    });
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.policy = new Policy('', '', '', PolicyType.XACMLPolicy, '');
  }

  ok(): void {
    if (!this.policy || !this.newPolicyForm.valid) {
      return;
    }

    this.policy.id = this.idControl.value;
    this.policy.version = this.versionControl.value;
    this.policy.name = this.nameControl.value;
    this.policy.data = this.dataControl.value;
    this.policy.type = this.typeControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .createPolicy(this.policy)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['..'], { relativeTo: this.activatedRoute });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
