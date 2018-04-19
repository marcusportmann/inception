/*
 * Copyright 2018 Marcus Portmann
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

package digital.inception.cache;

//~--- non-JDK imports --------------------------------------------------------

import com.hazelcast.config.*;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//~--- JDK imports ------------------------------------------------------------

/**
 * The <code>CacheManager</code> class implements the distributed in-memory cache manager.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings({ "unused", "WeakerAccess" })
public class CacheManager
{
  /* Logger */
  private static final Logger logger = LoggerFactory.getLogger(CacheManager.class);

  /**
   * The distributed in-memory caches we have initialized.
   */
  Map<String, Map> caches = new ConcurrentHashMap<>();

  /**
   * Constructs a new <code>CacheManager</code>.
   *
   * @param configuration the distributed in-memory cache manager configuration
   */
  public CacheManager(CacheManagerConfiguration configuration)
    throws CacheManagerException
  {
    try
    {
      Thread.currentThread().getContextClassLoader().loadClass("com.hazelcast.config.Config");
    }
    catch (ClassNotFoundException ignored)
    {
      throw new CacheManagerException(
          "Failed to initialize the distributed in-memory cache cluster: The Hazelcast library could not be found");
    }

    try
    {
      logger.info("Initialising the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ")");

      Config config = new Config();

      config.setInstanceName(configuration.getCluster().getName());

      config.setProperty("hazelcast.logging.type", "slf4j");
      config.setProperty("hazelcast.rest.enabled", "false");

      NetworkConfig networkConfig = config.getNetworkConfig();

      networkConfig.setPort(configuration.getCluster().getPort());
      networkConfig.setPortAutoIncrement(false);
      networkConfig.setReuseAddress(true);

      JoinConfig joinConfig = networkConfig.getJoin();

      MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
      multicastConfig.setEnabled(false);

      AwsConfig awsConfig = joinConfig.getAwsConfig();
      awsConfig.setEnabled(false);

      TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
      tcpIpConfig.setEnabled(true);

      // Add the cache members
      String[] members = { "127.0.0.1" };

      if ((configuration.getCluster().getMembers() != null)
          && (configuration.getCluster().getMembers().trim().length() > 0))
      {
        members = configuration.getCluster().getMembers().trim().split(",");
      }

      if (members.length > 0)
      {
        for (String member : members)
        {
          tcpIpConfig.addMember(member);
        }
      }

      GroupConfig groupConfig = config.getGroupConfig();
      groupConfig.setName(configuration.getCluster().getName());
      groupConfig.setPassword(configuration.getCluster().getPassword());

      // Initialise the caches
      for (CacheManagerConfiguration.CacheConfiguration cacheConfiguration :
          configuration.getCaches())
      {
        logger.info("Initialising the distributed in-memory cache (" + cacheConfiguration.getName()
            + ")");

        MapConfig mapConfig = config.getMapConfig(cacheConfiguration.getName());

        mapConfig.setInMemoryFormat(Enum.valueOf(InMemoryFormat.class,
            cacheConfiguration.getInMemoryFormat()));

        mapConfig.setEvictionPolicy(Enum.valueOf(EvictionPolicy.class,
            cacheConfiguration.getEvictionPolicy()));

        mapConfig.setStatisticsEnabled(cacheConfiguration.getStatisticsEnabled());

        mapConfig.setMaxIdleSeconds(cacheConfiguration.getMaxIdleSeconds());

        MaxSizeConfig maxSizeConfig = new MaxSizeConfig();
        maxSizeConfig.setMaxSizePolicy(Enum.valueOf(MaxSizeConfig.MaxSizePolicy.class,
            cacheConfiguration.getMaxSizePolicy()));
        maxSizeConfig.setSize(cacheConfiguration.getMaxSize());

        mapConfig.setMaxSizeConfig(maxSizeConfig);

        mapConfig.setBackupCount(cacheConfiguration.getBackupCount());

        mapConfig.setAsyncBackupCount(cacheConfiguration.getAsyncBackupCount());

        mapConfig.setReadBackupData(cacheConfiguration.getReadBackupData());
      }

      HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

      for (CacheManagerConfiguration.CacheConfiguration cacheConfiguration :
          configuration.getCaches())
      {
        Map<?, ?> cache = hazelcastInstance.getMap(cacheConfiguration.getName());

        caches.put(cacheConfiguration.getName(), cache);
      }

      logger.info("Successfully connected to the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ") with " + hazelcastInstance.getCluster()
          .getMembers().size() + " cluster member(s)");
    }
    catch (Throwable e)
    {
      throw new CacheManagerException(
          "Failed to initialise the distributed in-memory cache cluster ("
          + configuration.getCluster().getName() + ")", e);
    }
  }

  /**
   * Returns the distributed in-memory cache with the specified name.
   *
   * @param name the name of the distributed in-memory cache
   *
   * @return the distributed in-memory cache with the specified name
   */
  public Map getCache(String name)
    throws CacheManagerException
  {
    if (caches.containsKey(name))
    {
      return caches.get(name);
    }
    else
    {
      throw new CacheManagerException("The distributed in-memory cache (" + name
          + ") does not exist");
    }
  }
}
