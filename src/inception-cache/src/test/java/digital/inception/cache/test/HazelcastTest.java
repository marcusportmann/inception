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

package digital.inception.cache.test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import com.hazelcast.map.IMap;
import digital.inception.cache.HazelcastCacheConfiguration;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code HazelcastTest} class.
 *
 * @author Marcus Portmann
 */
@SpringBootTest(
    classes = {HazelcastTest.HazelcastTestConfig.class, HazelcastCacheConfiguration.class},
    properties = {
      "inception.cache.hazelcast.server.enabled=true",
      "inception.cache.hazelcast.server.port-auto-increment=true",
      "inception.cache.hazelcast.server.cluster.port=15000",
      "inception.cache.hazelcast.server.cluster.name=test-cluster",
      "inception.cache.hazelcast.server.cluster.password=Password1",
      "inception.cache.hazelcast.caches[0].name=testCache",
      "inception.cache.hazelcast.caches[0].max-size-policy=PER_NODE",
      "inception.cache.hazelcast.caches[0].maxSize=10000",
      "inception.cache.hazelcast.caches[0].in-memory-format=OBJECT",
      "inception.cache.hazelcast.caches[0].eviction-policy=LRU",
      "inception.cache.hazelcast.caches[0].statistics-enabled=true",
      "inception.cache.hazelcast.caches[0].max-idle-seconds=300",
      "inception.cache.hazelcast.caches[0].backup-count=0",
      "inception.cache.hazelcast.caches[0].async-backup-count=0",
      "inception.cache.hazelcast.caches[0].read-backup-data=false"
    },
    webEnvironment = WebEnvironment.NONE)
@Disabled
public class HazelcastTest {

  @Autowired private ApplicationContext applicationContext;

  @Test
  public void hazelcastTest() {
    CacheManager cacheManager = applicationContext.getBean(CacheManager.class);

    Cache cache = cacheManager.getCache("testCache");

    cache.put("key1", "value1");

    Object nativeCacheObject = cache.getNativeCache();

    assertInstanceOf(IMap.class, nativeCacheObject);

    @SuppressWarnings("unchecked")
    IMap<Object, Object> iMap = (IMap<Object, Object>) nativeCacheObject;

    for (Object key : iMap.keySet()) {
      System.out.println("key: " + key + " value: " + iMap.get(key));
    }
  }

  @Configuration
  @EnableAutoConfiguration
  @EnableCaching
  static class HazelcastTestConfig {}
}
