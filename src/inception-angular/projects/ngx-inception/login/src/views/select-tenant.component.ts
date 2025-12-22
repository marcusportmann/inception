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

import { Component, inject, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AutocompleteSelectionRequiredDirective, CoreModule, Session, SessionService,
  ValidatedFormDirective
} from 'ngx-inception/core';
import { Tenant } from 'ngx-inception/security';
import { BehaviorSubject, Subject } from 'rxjs';
import { debounceTime, first, map, startWith, takeUntil } from 'rxjs/operators';

/**
 * The SelectTenantComponent class implements the select tenant component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-login-select-tenant',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective, AutocompleteSelectionRequiredDirective],
  templateUrl: 'select-tenant.component.html'
})
export class SelectTenantComponent implements OnInit, OnDestroy {
  // Exposed as observable for template binding
  readonly filteredTenants$ = new BehaviorSubject<Tenant[]>([]);

  readonly selectTenantForm: FormGroup<{
    tenant: FormControl<Tenant | string>;
  }>;

  readonly tenantControl: FormControl<Tenant | string>;

  private readonly activatedRoute = inject(ActivatedRoute);

  private readonly destroy$ = new Subject<void>();

  private readonly router = inject(Router);

  private readonly sessionService = inject(SessionService);

  private tenants: Tenant[] = [];

  /**
   * Constructs a new SelectTenantComponent.
   */
  constructor() {
    // Initialize the form controls
    this.tenantControl = new FormControl<Tenant | string>('', {
      nonNullable: true,
      validators: [Validators.required]
    });

    // Initialize the form
    this.selectTenantForm = new FormGroup({
      tenant: this.tenantControl
    });
  }

  displayTenant(tenant: Tenant | string | null): string {
    if (!tenant || typeof tenant === 'string') {
      return typeof tenant === 'string' ? tenant : '';
    }

    return tenant.name ?? '';
  }

  isTenantSelected(): boolean {
    const value = this.tenantControl.value;
    return SelectTenantComponent.isTenant(value);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
    this.filteredTenants$.complete();
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
      .pipe(
        first(),
        map(() => window.history.state)
      )
      .subscribe((state) => {
        const tenants: Tenant[] | undefined = state?.tenants;

        if (!tenants || !Array.isArray(tenants) || tenants.length === 0) {
          console.error(
            'No tenants found, invalidating session and redirecting to the application root'
          );

          this.sessionService.logout();

          void this.router.navigate(['/']);

          return;
        }

        this.tenants = tenants;
        this.filteredTenants$.next(this.tenants);

        this.setupTenantFiltering();
      });
  }

  ok(): void {
    if (this.selectTenantForm.invalid) {
      this.selectTenantForm.markAllAsTouched();
      return;
    }

    const selected = this.tenantControl.value;

    if (!SelectTenantComponent.isTenant(selected)) {
      this.selectTenantForm.markAllAsTouched();
      return;
    }

    this.sessionService.session$.pipe(first()).subscribe((session: Session | null) => {
      if (session) {
        session.tenantId = selected.id;

        void this.router.navigate(['/']);
      } else {
        console.error(
          'No active session found when selecting tenant, redirecting to the application root'
        );

        void this.router.navigate(['/']);
      }
    });
  }

  private static isTenant(value: unknown): value is Tenant {
    return !!value && typeof value === 'object' && 'id' in value && 'name' in value;
  }

  private filterTenants(tenants: Tenant[], value: Tenant | string): Tenant[] {
    if (!value) {
      return tenants;
    }

    const filterValue =
      typeof value === 'string' ? value.toLowerCase() : (value.name?.toLowerCase() ?? '');

    if (!filterValue) {
      return tenants;
    }

    return tenants.filter((tenant) => (tenant.name ?? '').toLowerCase().startsWith(filterValue));
  }

  private setupTenantFiltering(): void {
    this.tenantControl.valueChanges
      .pipe(startWith('' as Tenant | string), debounceTime(200), takeUntil(this.destroy$))
      .subscribe((value) => {
        const filtered = this.filterTenants(this.tenants, value);
        this.filteredTenants$.next(filtered);
      });
  }
}
