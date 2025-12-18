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

import {
  Directive, inject, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef
} from '@angular/core';
import { Subscription } from 'rxjs';
import { Session } from '../services/session';
import { SessionService } from '../services/session.service';

/**
 * Structural directive that conditionally renders its host template when the current user has at
 * least one of the required authorities.
 *
 * Usage:
 * <pre>
 *   <div *hasAuthority="'ADMIN'">...</div>
 *   <div *hasAuthority="['ADMIN', 'USER_MANAGE']">...</div>
 *   <div *hasAuthority="'ADMIN,USER_MANAGE'">...</div>
 * </pre>
 */
@Directive({
  // eslint-disable-next-line @angular-eslint/directive-selector
  selector: '[hasAuthority]',
  standalone: true
})
export class HasAuthorityDirective implements OnInit, OnDestroy {
  private latestSession: Session | null = null;

  private requiredAuthorities: string[] = [];

  private readonly sessionService = inject(SessionService);

  private sessionSub?: Subscription;

  private readonly templateRef = inject<TemplateRef<unknown>>(TemplateRef);

  private readonly viewContainer = inject(ViewContainerRef);

  /**
   * Accepts:
   *   - 'ADMIN'
   *   - ['ADMIN', 'USER_MANAGE']
   *   - 'ADMIN,USER_MANAGE'
   */
  @Input('hasAuthority')
  set hasAuthorityInput(value: string | string[]) {
    this.requiredAuthorities = this.normalizeAuthorities(value);
    this.updateView();
  }

  ngOnDestroy(): void {
    this.sessionSub?.unsubscribe();
  }

  ngOnInit(): void {
    // Subscribe to session changes and update the view whenever the session changes
    this.sessionSub = this.sessionService.session$.subscribe((session) => {
      this.latestSession = session;
      this.updateView();
    });
  }

  private hide(): void {
    this.viewContainer.clear();
  }

  /**
   * Normalise the input into a clean string array of authorities.
   */
  private normalizeAuthorities(value: string | string[] | null | undefined): string[] {
    if (!value) {
      return [];
    }

    if (Array.isArray(value)) {
      return value
        .filter((v) => !!v)
        .map((v) => v.trim())
        .filter((v) => v.length > 0);
    }

    // Support comma-separated strings
    return value
      .split(',')
      .map((v) => v.trim())
      .filter((v) => v.length > 0);
  }

  private show(): void {
    // Avoid creating duplicate views
    if (this.viewContainer.length === 0) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    }
  }

  /**
   * Decide whether to show or hide the view based on the current
   * session and required authorities.
   */
  private updateView(): void {
    const authorities = this.requiredAuthorities;

    // No authorities required → always show
    if (!authorities.length) {
      this.show();
      return;
    }

    const session = this.latestSession;
    const hasAuthority =
      !!session && authorities.some((authority) => session.hasAuthority(authority));

    if (hasAuthority) {
      this.show();
    } else {
      this.hide();
    }
  }
}
