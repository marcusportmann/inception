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
import {ReplaySubject, Subject, Subscription} from 'rxjs';
import {debounceTime, first, map, startWith} from 'rxjs/operators';

/**
 * The SelectTenantComponent class implements the select tenant component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'select-tenant.component.html'
})
export class SelectTenantComponent implements OnInit, OnDestroy {

  filteredTenants$: Subject<Tenant[]> = new ReplaySubject<Tenant[]>();

  selectTenantForm: FormGroup;

  tenantControl: FormControl;

  private subscriptions: Subscription = new Subscription();

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
    return tenant.name;
  }

  isTenantSelected(): boolean {
    if (this.selectTenantForm.valid) {
      if (this.tenantControl.value as Tenant) {
        if (typeof (this.tenantControl.value.id) !== 'undefined' && this.tenantControl.value.id !==
          null) {
          return true;
        }
      }
    }

    return false;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    this.activatedRoute.paramMap
    .pipe(first(), map(() => window.history.state))
    .subscribe((state) => {
      if (state.tenants) {
        this.subscriptions.add(this.tenantControl.valueChanges.pipe(startWith(''),
          debounceTime(500)).subscribe((value) => {
          this.filteredTenants$.next(this.filterTenants(state.tenants, value));
        }));
      } else {
        console.log(
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
        if (session) {
          if (typeof this.tenantControl.value === 'object') {
            session.tenantId = (this.tenantControl.value as Tenant).id;

            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['/']);
          }
        }
      });
    }
  }

  private filterTenants(tenants: Tenant[], value: string | Tenant): Tenant[] {
    let filterValue = '';

    if (typeof value === 'string') {
      filterValue = (value as string).toLowerCase();
    } else if (typeof value === 'object') {
      filterValue = (value as Tenant).name.toLowerCase();
    }

    return tenants.filter(tenant => tenant.name.toLowerCase().indexOf(filterValue) === 0);
  }
}
