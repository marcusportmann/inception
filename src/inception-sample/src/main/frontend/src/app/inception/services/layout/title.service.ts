/*
 * Copyright 2019 Marcus Portmann
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
import {BehaviorSubject} from 'rxjs';

/**
 * The Title Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class TitleService {

  title: BehaviorSubject<string> = new BehaviorSubject<string>(null);

  /**
   * Constructs a new TitleService.
   */
  constructor() {
    console.log('Initializing the Title Service');
  }

  /**
   * Clear the current title
   */
  clearTitle(): void {
    this.title.next(null);
  }

  /**
   * Set the current title.
   *
   * @param title the current title
   */
  setTitle(title: string): void {
    this.title.next(title);
  }
}
