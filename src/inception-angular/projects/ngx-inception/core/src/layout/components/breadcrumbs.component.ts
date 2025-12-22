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

import { AsyncPipe, NgClass } from '@angular/common';
import {
  ChangeDetectionStrategy, ChangeDetectorRef, Component, ElementRef, inject, Input, OnDestroy,
  OnInit
} from '@angular/core';
import { RouterLink } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { Replace } from '../../util/replace';
import { Breadcrumb } from '../services/breadcrumb';
import { BreadcrumbsService } from '../services/breadcrumbs.service';

/**
 * The BreadcrumbsComponent class implements the breadcrumbs component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'breadcrumbs',
  standalone: true,
  template: `
    @if (this.visible) {
      <ol class="breadcrumbs">
        @for (breadcrumb of breadcrumbs | async; track breadcrumb; let last = $last) {
          @if (breadcrumb.label) {
            <li class="breadcrumbs-item" [ngClass]="{ active: last }">
              @if (!last && !!breadcrumb.url) {
                <a [routerLink]="breadcrumb.url">{{ breadcrumb.label }}</a>
              }
              @if (last || !breadcrumb.url) {
                <span>{{ breadcrumb.label }}</span>
              }
            </li>
          }
        }
      </ol>
    }
  `,
  imports: [AsyncPipe, NgClass, RouterLink],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class BreadcrumbsComponent implements OnInit, OnDestroy {
  breadcrumbs: Observable<Breadcrumb[]>;

  @Input() fixed = false;

  visible = false;

  private readonly breadcrumbsService = inject(BreadcrumbsService);

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  private readonly elementRef = inject(ElementRef);

  private readonly subscriptions: Subscription = new Subscription();

  /**
   * Constructs a new BreadcrumbsComponent.
   */
  constructor() {
    this.breadcrumbs = this.breadcrumbsService.breadcrumbs$;
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    Replace(this.elementRef);

    this.subscriptions.add(
      this.breadcrumbsService.breadcrumbs$.subscribe((breadcrumbs: Breadcrumb[]) => {
        const bodySelector = document.querySelector('body');

        if (bodySelector) {
          if (this.fixed && breadcrumbs.length > 0) {
            bodySelector.classList.add('breadcrumbs-fixed');
          } else {
            bodySelector.classList.remove('breadcrumbs-fixed');
          }
        }

        this.changeDetectorRef.detectChanges();
      })
    );

    this.subscriptions.add(
      this.breadcrumbsService.breadcrumbsVisible$.subscribe((breadcrumbsVisible: boolean) => {
        this.visible = breadcrumbsVisible;
        this.changeDetectorRef.detectChanges();
      })
    );
  }
}
