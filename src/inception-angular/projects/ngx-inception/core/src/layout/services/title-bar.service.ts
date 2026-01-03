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

import {Injectable} from '@angular/core';
import {Observable, ReplaySubject} from 'rxjs';
import {BackNavigation} from '../components/back-navigation';

/**
 * The Title Bar Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class TitleBarService {
  private readonly _backNavigationSubject = new ReplaySubject<BackNavigation | null>(1);

  private readonly _titleSubject = new ReplaySubject<string | null>(1);

  /**
   * Observable that emits the current back navigation configuration.
   * Exposed as read-only to prevent external callers from emitting values.
   */
  get backNavigation$(): Observable<BackNavigation | null> {
    return this._backNavigationSubject.asObservable();
  }

  /**
   * Observable that emits the current title.
   * Exposed as read-only to prevent external callers from emitting values.
   */
  get title$(): Observable<string | null> {
    return this._titleSubject.asObservable();
  }

  /**
   * Set the back navigation.
   *
   * @param backNavigation the back navigation
   */
  setBackNavigation(backNavigation: BackNavigation | null): void {
    this._backNavigationSubject.next(backNavigation);
  }

  /**
   * Set the title.
   *
   * @param title the title
   */
  setTitle(title: string | null): void {
    this._titleSubject.next(title);
  }
}
