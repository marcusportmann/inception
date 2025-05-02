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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {Session, SessionService} from 'ngx-inception/core';
import {Tenant} from 'ngx-inception/security';
import {ReplaySubject, Subject} from 'rxjs';
import {debounceTime, first, map, startWith, takeUntil} from 'rxjs/operators';

/**
 * The SelectTenantComponent class implements the select tenant component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'select-tenant.component.html',
  standalone: false
})
export class SelectTenantComponent implements OnInit, OnDestroy {

  filteredTenants$: Subject<Tenant[]> = new ReplaySubject<Tenant[]>(1);

  selectTenantForm: FormGroup;

  tenantControl: FormControl;

  private destroy$ = new Subject<void>();

  /**
   * Constructs a new SelectTenantComponent.
   *
   * @param router          The router.
   * @param activatedRoute  The activated route.
   * @param sessionService  The session service.
   */
  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private sessionService: SessionService) {

    // Initialise the form controls
    this.tenantControl = new FormControl('', Validators.required);

    // Initialise the form
    this.selectTenantForm = new FormGroup({
      tenant: this.tenantControl
    });
  }

  displayTenant(tenant: Tenant): string {
    return tenant?.name || '';
  }

  isTenantSelected(): boolean {
    const selectedTenant = this.tenantControl.value;
    return !!(selectedTenant && typeof selectedTenant === 'object' && selectedTenant.id);
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
    .pipe(first(), map(() => window.history.state))
    .subscribe((state) => {
      if (state.tenants) {
        this.tenantControl.valueChanges
        .pipe(startWith(''), debounceTime(300), takeUntil(this.destroy$))
        .subscribe((value) => {
          this.filteredTenants$.next(this.filterTenants(state.tenants, value));
        });
      } else {
        console.error(
          'No tenants found, invalidating session and redirecting to the application root');

        this.sessionService.logout();

        // noinspection JSIgnoredPromiseFromCall
        this.router.navigate(['/']);
      }
    });
  }

  ok(): void {
    if (this.selectTenantForm.valid) {
      this.sessionService.session$
      .pipe(first())
      .subscribe((session: Session | null) => {
        if (session && typeof this.tenantControl.value === 'object') {
          session.tenantId = this.tenantControl.value.id;

          // noinspection JSIgnoredPromiseFromCall
          this.router.navigate(['/']);
        }
      });
    }
  }

  private filterTenants(tenants: Tenant[], value: string | Tenant): Tenant[] {
    const filterValue = typeof value === 'string' ? value.toLowerCase() : value.name.toLowerCase();
    return tenants.filter((tenant) => tenant.name.toLowerCase().startsWith(filterValue));
  }
}
