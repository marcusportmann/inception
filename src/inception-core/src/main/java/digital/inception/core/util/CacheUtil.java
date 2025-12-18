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

package digital.inception.core.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

/**
 * The {@code CacheUtil} class provides cache related utility functions.
 *
 * @author Marcus Portmann
 */
public final class CacheUtil {

  /** Private constructor to prevent instantiation. */
  private CacheUtil() {}

  /**
   * Returns all values from the cache with the specified name.
   *
   * <p>This method:
   *
   * <ul>
   *   <li>Obtains a {@link CacheManager} from the given {@link ApplicationContext}, if available.
   *   <li>Returns an empty collection if the application context is {@code null} or no {@code
   *       CacheManager} is configured.
   *   <li>Returns an empty collection if a cache with the specified name does not exist.
   *   <li>If the underlying native cache is a Hazelcast {@code IMap} (which implements {@link
   *       ConcurrentMap}) or any other {@link ConcurrentMap} / {@link Map}, returns a new
   *       collection containing its values.
   *   <li>Returns an empty collection for non-{@code Map}-based cache implementations.
   * </ul>
   *
   * @param applicationContext the Spring {@code ApplicationContext} used to resolve the {@code
   *     CacheManager}; may be {@code null}
   * @param cacheName the name of the cache
   * @return a collection containing the values stored in the cache, or an empty collection if the
   *     cache manager, cache, or underlying map is not available
   */
  public static Collection<Object> getCacheValues(
      ApplicationContext applicationContext, String cacheName) {
    if (applicationContext == null) {
      return Collections.emptyList();
    }

    // Resolve CacheManager lazily and safely
    ObjectProvider<CacheManager> provider = applicationContext.getBeanProvider(CacheManager.class);

    CacheManager cacheManager = provider.getIfAvailable();
    if (cacheManager == null) {
      return Collections.emptyList();
    }

    Cache cache = cacheManager.getCache(cacheName);
    if (cache == null) {
      return Collections.emptyList();
    }

    Object nativeCache = cache.getNativeCache();

    // Hazelcast IMap implements ConcurrentMap, so this covers Hazelcast maps too
    if (nativeCache instanceof ConcurrentMap<?, ?> concurrentMap) {
      return new ArrayList<>(concurrentMap.values());
    }

    if (nativeCache instanceof Map<?, ?> map) {
      return new ArrayList<>(map.values());
    }

    // Not a Map/ConcurrentMap-based cache
    return Collections.emptyList();
  }
}
