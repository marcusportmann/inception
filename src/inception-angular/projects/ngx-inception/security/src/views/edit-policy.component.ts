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

import { AfterViewInit, Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError,
  AdminContainerView,
  BackNavigation,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Policy } from '../services/policy';
import { SecurityService } from '../services/security.service';

/**
 * The EditPolicyComponent class implements the edit policy component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-policy.component.html',
  styleUrls: ['edit-policy.component.css'],
  standalone: false
})
export class EditPolicyComponent
  extends AdminContainerView
  implements AfterViewInit
{
  dataControl: FormControl;

  editPolicyForm: FormGroup;

  idControl: FormControl;

  nameControl: FormControl;

  policy: Policy | null = null;

  policyId: string;

  typeControl: FormControl;

  versionControl: FormControl;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private securityService: SecurityService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Retrieve the route parameters
    const policyId = this.activatedRoute.snapshot.paramMap.get('policyId');

    if (!policyId) {
      throw new Error('No policyId route parameter found');
    }

    this.policyId = decodeURIComponent(policyId);

    // Initialise the form controls
    this.dataControl = new FormControl('', [Validators.required]);
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.nameControl = new FormControl('', [
      Validators.required,
      Validators.maxLength(100)
    ]);
    this.typeControl = new FormControl([], Validators.required);
    this.versionControl = new FormControl('', [
      Validators.required,
      Validators.maxLength(30)
    ]);

    // Initialise the form
    this.editPolicyForm = new FormGroup({
      data: this.dataControl,
      id: this.idControl,
      name: this.nameControl,
      type: this.typeControl,
      version: this.versionControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_edit_policy_back_navigation:Policies`,
      ['../..'],
      { relativeTo: this.activatedRoute }
    );
  }

  get title(): string {
    return $localize`:@@security_edit_policy_title:Edit Policy`;
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing code category and initialise the form controls
    this.spinnerService.showSpinner();

    this.securityService
      .getPolicy(this.policyId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe(
        (policy: Policy) => {
          this.policy = policy;
          this.idControl.setValue(policy.id);
          this.versionControl.setValue(policy.version);
          this.nameControl.setValue(policy.name);
          this.typeControl.setValue(policy.type);
          this.dataControl.setValue(policy.data);
        },
        (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if (
            error instanceof AccessDeniedError ||
            error instanceof InvalidArgumentError ||
            error instanceof ServiceUnavailableError
          ) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {
              state: { error }
            });
          } else {
            this.dialogService
              .showErrorDialog(error)
              .afterClosed()
              .pipe(first())
              .subscribe(() => {
                this.router.navigate(['../..'], {
                  relativeTo: this.activatedRoute
                });
              });
          }
        }
      );
  }

  ok(): void {
    if (this.policy && this.editPolicyForm.valid) {
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
        .subscribe(
          () => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            });
          },
          (error: Error) => {
            // noinspection SuspiciousTypeOfGuard
            if (
              error instanceof AccessDeniedError ||
              error instanceof InvalidArgumentError ||
              error instanceof ServiceUnavailableError
            ) {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigateByUrl('/error/send-error-report', {
                state: { error }
              });
            } else {
              this.dialogService.showErrorDialog(error);
            }
          }
        );
    }
  }
}
