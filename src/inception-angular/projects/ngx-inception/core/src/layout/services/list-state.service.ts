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

import { Injectable } from '@angular/core';

/**
 * The persisted state for a pageable, sortable list.
 *
 * This model captures the UI state that should survive navigation, such as the current page,
 * page size, sort field/direction, and an optional text filter. The generic `TExtras` allows each
 * list to store additional, feature-specific state (e.g., selected token status, active tab, etc.).
 */
export interface ListState<TExtras = unknown> {
  extras?: TExtras;
  filter?: string;
  pageIndex: number;
  pageSize: number;
  sortActive: string;
  sortDirection: 'asc' | 'desc' | '';
}

/**
 * The List State Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
@Injectable({ providedIn: 'root' })
export class ListStateService {
  private readonly states = new Map<string, ListState>();

  clear(key: string): void {
    this.states.delete(key);
  }

  get(key: string): ListState | undefined {
    return this.states.get(key);
  }

  set(key: string, state: ListState): void {
    this.states.set(key, state);
  }
}
