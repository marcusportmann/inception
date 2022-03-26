/*
 * Copyright 2022 Marcus Portmann
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
import {map} from 'rxjs/operators';
import {Session} from '../../session/services/session';
import {SessionService} from '../../session/services/session.service';

/**
 * The Cache Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class CacheService {

  private cache: Map<string, any> = new Map<string, any>();

  /**
   * Constructs a new CacheService.
   *
   * @param sessionService The session service.
   */
  constructor(private sessionService: SessionService) {
    console.log('Initializing the Cache Service');

    this.sessionService.session$.subscribe((session: Session | null) => {
      this.clear();
    });
  }

  /**
   * Clear the cache managed by the Cache Service.
   */
  clear(): void {
    console.log('Clearing ' + this.cache.size  + ' items from the cache managed by the Cache Service');
    this.cache.clear();
  }

  /**
   * Retrieve the cached value with the specified key.
   *
   * @param key The key identifying the cached value.
   *
   * @return The cached value or undefined if the cached value could not be found.
   */
  get(key: string): any | undefined {
    return this.cache.get(key);
  }

  /**
   * Returns whether the cache has a cached value with the specified key.
   *
   * @param key The key identifying the cached value.
   *
   * @return True if the cached value with the specified key exists of false otherwise.
   */
  has(key: string) {
    return this.cache.has(key);
  }

  /**
   * Store the value in the cache with the specified key.
   *
   * @param key   The key identifying the cached value.
   * @param value The value to cache.
   */
  set(key: string, value: any) {
    this.cache.set(key, value);
  }
}
