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
  booleanAttribute, ChangeDetectionStrategy, Component, inject, Input, OnChanges, OnDestroy, OnInit,
  SimpleChanges
} from '@angular/core';
import {SidebarService} from '../services/sidebar.service';

/**
 * The SidebarComponent class implements the sidebar component.
 *
 * @author Marcus Portmann
 */
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush,
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar',
  standalone: true,
  template: `
    <ng-content></ng-content>`
})
export class SidebarComponent implements OnInit, OnChanges, OnDestroy {
  @Input({transform: booleanAttribute}) compact = false;

  @Input() display?: string;

  @Input({transform: booleanAttribute}) fixed = false;

  @Input({transform: booleanAttribute}) minimized = false;

  @Input({transform: booleanAttribute}) offCanvas = false;

  private readonly _sidebarService = inject(SidebarService);

  ngOnChanges(changes: SimpleChanges): void {
    void changes;
    this._pushConfig();
  }

  ngOnDestroy(): void {
    // Optional. In a single-sidebar app this is fine; if you ever mount/unmount,
    // this prevents body class leaks.
    this._sidebarService.reset();
  }

  ngOnInit(): void {
    this._pushConfig();
  }

  private _pushConfig(): void {
    this._sidebarService.configure({
      compact: this.compact,
      display: this.display,
      fixed: this.fixed,
      offCanvas: this.offCanvas
    });
  }
}
