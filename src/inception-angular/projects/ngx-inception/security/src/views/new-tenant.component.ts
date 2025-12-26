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
  AdminContainerView, BackNavigation, CoreModule, GroupFormFieldComponent, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { v4 as uuid } from 'uuid';
import { SecurityService } from '../services/security.service';
import { Tenant } from '../services/tenant';
import { TenantStatus } from '../services/tenant-status';

/**
 * The NewTenantComponent class implements the new tenant component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-new-tenant',
  imports: [CoreModule, ValidatedFormDirective, GroupFormFieldComponent],
  templateUrl: 'new-tenant.component.html',
  styleUrls: ['new-tenant.component.css']
})
export class NewTenantComponent extends AdminContainerView implements OnInit {
  readonly createUserDirectoryControl: FormControl<boolean>;

  readonly nameControl: FormControl<string>;

  readonly newTenantForm: FormGroup<{
    createUserDirectory: FormControl<boolean>;
    name: FormControl<string>;
  }>;

  tenant: Tenant | null = null;

  readonly title = $localize`:@@security_new_tenant_title:New Tenant`;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Initialize the form controls
    this.createUserDirectoryControl = new FormControl<boolean>(false, { nonNullable: true });

    this.nameControl = new FormControl<string>('', {
      nonNullable: true,
      validators: [Validators.required, Validators.maxLength(100)]
    });

    // Initialize the form
    this.newTenantForm = new FormGroup({
      createUserDirectory: this.createUserDirectoryControl,
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_new_tenant_back_navigation:Tenants`, ['.'], {
      relativeTo: this.activatedRoute.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  ngOnInit(): void {
    this.tenant = new Tenant(uuid(), '', TenantStatus.Active);
  }

  ok(): void {
    if (!this.tenant || !this.newTenantForm.valid) {
      return;
    }

    this.tenant.name = this.nameControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .createTenant(this.tenant, this.createUserDirectoryControl.value)
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
}
