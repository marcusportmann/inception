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
import { SecurityService } from '../services/security.service';
import { Tenant } from '../services/tenant';

/**
 * The EditTenantComponent class implements the edit tenant component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-edit-tenant',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'edit-tenant.component.html',
  styleUrls: ['edit-tenant.component.css']
})
export class EditTenantComponent extends AdminContainerView implements AfterViewInit {
  editTenantForm: FormGroup;

  nameControl: FormControl;

  tenant: Tenant | null = null;

  tenantId: string;

  readonly title = $localize`:@@security_edit_tenant_title:Edit Tenant`;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    const tenantId = this.activatedRoute.snapshot.paramMap.get('tenantId');

    if (!tenantId) {
      throw new globalThis.Error('No tenantId route parameter found');
    }

    this.tenantId = tenantId;

    // Initialize the form controls
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);

    // Initialize the form
    this.editTenantForm = new FormGroup({
      name: this.nameControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@security_edit_tenant_back_navigation:Tenants`,
      ['.'],
      { relativeTo: this.activatedRoute.parent?.parent }
    );
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent?.parent });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing tenant and initialize the form fields
    this.spinnerService.showSpinner();

    this.securityService
      .getTenant(this.tenantId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (tenant: Tenant) => {
          this.tenant = tenant;
          this.nameControl.setValue(tenant.name);
        },
        error: (error: Error) => this.handleError(error, true, ['.'], { relativeTo: this.activatedRoute.parent?.parent })
      });
  }

  ok(): void {
    if (!this.tenant || !this.editTenantForm.valid) {
      return;
    }

    this.tenant.name = this.nameControl.value;

    this.spinnerService.showSpinner();

    this.securityService
      .updateTenant(this.tenant)
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
