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

import { Injectable, inject } from '@angular/core';
import { SessionService } from '../../session/services/session.service';

/**
 * The Cache Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class CacheService {
  private sessionService = inject(SessionService);

  private readonly cache = new Map<string, unknown>();

  /**
   * Constructs a new CacheService.
   *
   * @param sessionService The session service.
   */
  constructor() {
    console.log('Initializing the Cache Service');

    // Clear the cache whenever the session changes
    this.sessionService.session$.subscribe(() => {
      this.clear();
    });
  }

  /**
   * Clear the cache managed by the Cache Service.
   */
  clear(): void {
    console.log(`Clearing ${this.cache.size} items from the cache managed by the Cache Service`);
    this.cache.clear();
  }

  /**
   * Remove the cached value with the specified key.
   *
   * @param key The key identifying the cached value.
   *
   * @return True if an element in the cache existed and has been removed, or false if the element does not exist.
   */
  delete(key: string): boolean {
    return this.cache.delete(key);
  }

  /**
   * Retrieve the cached value with the specified key.
   *
   * @param key The key identifying the cached value.
   *
   * @return The cached value or undefined if the cached value could not be found.
   */
  get<T>(key: string): T | undefined {
    return this.cache.get(key) as T | undefined;
  }

  /**
   * Retrieve the cached value if present; otherwise create it using the factory,
   * store it, and return it.
   *
   * @param key     The key identifying the cached value.
   * @param factory The factory function used to create the value if it does not exist.
   */
  getOrSet<T>(key: string, factory: () => T): T {
    if (this.cache.has(key)) {
      return this.cache.get(key) as T;
    }

    const value = factory();
    this.cache.set(key, value);
    return value;
  }

  /**
   * Returns whether the cache has a cached value with the specified key.
   *
   * @param key The key identifying the cached value.
   *
   * @return True if the cached value with the specified key exists or false otherwise.
   */
  has(key: string): boolean {
    return this.cache.has(key);
  }

  /**
   * Store the value in the cache with the specified key.
   *
   * @param key   The key identifying the cached value.
   * @param value The value to cache.
   */
  set<T>(key: string, value: T): void {
    this.cache.set(key, value);
  }
}
