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

import {DOCUMENT, isPlatformBrowser} from '@angular/common';
import {inject, Injectable, PLATFORM_ID} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {distinctUntilChanged, map} from 'rxjs/operators';
import {SIDEBAR_CSS_CLASSES} from '../components/sidebar-css-classes';

export interface SidebarConfig {
  compact: boolean;
  display?: string;
  fixed: boolean;
  minimized: boolean;
  offCanvas: boolean;
}

/**
 * The Sidebar Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({providedIn: 'root'})
export class SidebarService {
  private readonly _document = inject<Document>(DOCUMENT);

  private readonly _platformId = inject(PLATFORM_ID);

  private readonly _stateSubject = new BehaviorSubject<SidebarConfig>({
    compact: false,
    display: undefined,
    fixed: false,
    minimized: false,
    offCanvas: false
  });

  /** Read-only minimized stream. */
  get sidebarMinimized$(): Observable<boolean> {
    return this.state$.pipe(
      map((s) => s.minimized),
      distinctUntilChanged()
    );
  }

  /** Read-only stream of the whole sidebar config/state. */
  get state$(): Observable<SidebarConfig> {
    return this._stateSubject.asObservable();
  }

  /** Apply the static configuration (display/compact/fixed/offCanvas) and minimized state. */
  configure(config: Partial<SidebarConfig>): void {
    const next = {...this._stateSubject.value, ...config};
    this._stateSubject.next(next);

    // Keep body classes in sync
    this._applyStateToBody(next);
  }

  /** Hide only the mobile "sidebar-show" class. */
  hideMobile(): void {
    if (!this._isBrowser()) return;
    this._document.body.classList.remove(SIDEBAR_CSS_CLASSES[0]);
  }

  /** Remove all sidebar-related classes this service manages. */
  reset(): void {
    this.configure({
      compact: false,
      display: undefined,
      fixed: false,
      minimized: false,
      offCanvas: false
    });

    if (!this._isBrowser()) return;

    const body = this._document.body;
    body.classList.remove('sidebar-compact', 'sidebar-fixed', 'sidebar-off-canvas');
    body.classList.remove('sidebar-minimized', 'admin-brand-minimized');
    SIDEBAR_CSS_CLASSES.forEach((c) => body.classList.remove(c));
  }

  /** Set minimized explicitly. */
  setSidebarMinimized(minimized: boolean): void {
    this.configure({minimized});
  }

  /** Toggle minimized. */
  toggleMinimized(): void {
    this.setSidebarMinimized(!this._stateSubject.value.minimized);
  }

  /** Toggle a sidebar "show" class (optionally for a breakpoint like 'lg'). */
  toggleShow(breakpoint?: string): void {
    if (!this._isBrowser()) return;

    const body = this._document.body;
    const cls = breakpoint?.trim() ? `sidebar-${breakpoint.trim()}-show` : SIDEBAR_CSS_CLASSES[0];

    // If already showing this class, hide; otherwise show it and remove others
    const already = body.classList.contains(cls);

    SIDEBAR_CSS_CLASSES.forEach((c) => body.classList.remove(c));
    if (!already) {
      body.classList.add(cls);
    }
  }

  private _applyStateToBody(state: SidebarConfig): void {
    if (!this._isBrowser()) return;

    const body = this._document.body;

    // Only remove/add show classes if display is configured
    if (state.display) {
      SIDEBAR_CSS_CLASSES.forEach((c) => body.classList.remove(c));
      body.classList.add(`sidebar-${state.display}-show`);
    }

    // Static flags
    body.classList.toggle('sidebar-compact', !!state.compact);
    body.classList.toggle('sidebar-fixed', !!state.fixed);
    body.classList.toggle('sidebar-off-canvas', !!state.offCanvas);

    // Minimized behavior
    body.classList.toggle('sidebar-minimized', !!state.minimized);
    body.classList.toggle('admin-brand-minimized', !!state.minimized);
  }

  private _isBrowser(): boolean {
    return isPlatformBrowser(this._platformId) && !!this._document?.body;
  }
}
