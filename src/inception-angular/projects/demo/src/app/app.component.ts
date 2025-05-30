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
import {NavigationEnd, Router} from '@angular/router';
import {Subject} from 'rxjs';
import {takeUntil} from 'rxjs/operators';

@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'body',
  template: `
    <router-outlet></router-outlet>`,
  standalone: false
})
export class AppComponent implements OnInit, OnDestroy {

  // eslint-disable-next-line
  private unsubscribe$: Subject<any> = new Subject();

  constructor(private router: Router) {
  }

  ngOnDestroy(): void {
    this.unsubscribe$.complete();
  }

  ngOnInit(): void {
    this.router.events.pipe(takeUntil(this.unsubscribe$)).subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
  }
}
