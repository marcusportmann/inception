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

import { AsyncPipe } from '@angular/common';
import { Component, ElementRef, inject, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { Replace } from '../../util/replace';
import { TitleBarService } from '../services/title-bar.service';
import { BackNavigation } from './back-navigation';

/**
 * The TitleBarComponent class implements the title bar component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'title-bar',
  standalone: true,
  imports: [AsyncPipe],
  template: `
    @if (title | async; as title) {
      <div class="title-bar">
        @if (backNavigation | async; as backNavigation) {
          <div class="back" (click)="navigateBack(backNavigation)">
            <span class="fa fa-chevron-left"></span> {{ backNavigation.title }}
          </div>
        }
        <div class="title">{{ title }}</div>
      </div>
    } @else {
      <div class="title-bar">
        <div class="title">No Title</div>
      </div>
    }
  `
})
export class TitleBarComponent implements OnInit {
  @Input() fixed = false;

  private readonly elementRef = inject(ElementRef);

  private readonly router = inject(Router);

  private readonly titleBarService = inject(TitleBarService);

  /**
   * The back navigation.
   */
  get backNavigation(): Observable<BackNavigation | null> {
    return this.titleBarService.backNavigation$;
  }

  /**
   * The title.
   */
  get title(): Observable<string | null> {
    return this.titleBarService.title$;
  }

  /**
   * Navigate back.
   */
  navigateBack(backNavigation: BackNavigation): void {
    if (backNavigation) {
      void this.router.navigate(backNavigation.commands, backNavigation.extras).then(() => {
        /* empty */
      });
    }
  }

  ngOnInit(): void {
    Replace(this.elementRef);

    if (this.fixed) {
      const bodySelector = document.querySelector('body');

      if (bodySelector) {
        bodySelector.classList.add('title-bar-fixed');
      }
    }
  }
}
