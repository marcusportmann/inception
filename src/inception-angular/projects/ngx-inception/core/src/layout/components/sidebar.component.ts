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
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  Input,
  OnDestroy,
  OnInit
} from '@angular/core';
import { Subscription } from 'rxjs';
import { SidebarService } from '../services/sidebar.service';
import { SIDEBAR_CSS_CLASSES } from './sidebar-css-classes';

/**
 * The SidebarComponent class implements the sidebar component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'sidebar',
  template: ` <ng-content></ng-content>`,
  changeDetection: ChangeDetectionStrategy.OnPush,
  standalone: false
})
export class SidebarComponent implements OnInit, OnDestroy {
  // TODO: Confirm if we can default these properties to false -- MARCUS
  @Input() compact?: boolean;

  @Input() display?: string;

  @Input() fixed?: boolean;

  @Input() minimized?: boolean;

  @Input() offCanvas?: boolean;

  private sidebarMinimizedSubscription?: Subscription;

  /**
   * Constructs a new SidebarComponent.
   *
   * @param sidebarService The sidebar service.
   */
  constructor(private sidebarService: SidebarService) {}

  @HostBinding('class.sidebar') get sidebar() {
    return true;
  }

  ngOnDestroy(): void {
    if (this.sidebarMinimizedSubscription) {
      this.sidebarMinimizedSubscription.unsubscribe();
    }
  }

  ngOnInit(): void {
    const bodySelector = document.querySelector('body');

    this.sidebarMinimizedSubscription =
      this.sidebarService.sidebarMinimized$.subscribe(
        (sidebarMinimized: boolean) => {
          if (sidebarMinimized) {
            if (bodySelector) {
              bodySelector.classList.add('sidebar-minimized');
            }
          } else {
            if (bodySelector) {
              bodySelector.classList.remove('sidebar-minimized');
            }
          }
        }
      );

    if (bodySelector) {
      if (!!this.display) {
        let cssClass;
        this.display
          ? (cssClass = `sidebar-${this.display}-show`)
          : (cssClass = SIDEBAR_CSS_CLASSES[0]);
        bodySelector.classList.add(cssClass);
      }

      if (!!this.compact) {
        bodySelector.classList.add('sidebar-compact');
      }

      if (!!this.fixed) {
        bodySelector.classList.add('sidebar-fixed');
      }

      if (!!this.minimized) {
        bodySelector.classList.add('sidebar-minimized');
      }

      if (!!this.offCanvas) {
        bodySelector.classList.add('sidebar-off-canvas');
      }
    }
  }
}
