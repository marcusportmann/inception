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

package digital.inception.cache;

import com.hazelcast.config.AwsConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionConfig;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.InMemoryFormat;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizePolicy;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import java.util.List;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code HazelcastCacheConfiguration} class provides the Hazelcast cache configuration.
 *
 * @author Marcus Portmann
 */
@Configuration
@ConditionalOnClass(name = "com.hazelcast.config.Config")
@ConditionalOnProperty(value = "inception.cache.hazelcast.server.enabled", havingValue = "true")
@ConfigurationProperties("inception.cache.hazelcast")
public class HazelcastCacheConfiguration {

  /** The distributed in-memory caches. (inception.cache.hazelcast.caches) */
  private List<CacheConfig> caches;

  /** The server configuration. (inception.cache.hazelcast.server) */
  private ServerConfig server;

  /** Constructs a new {@code HazelcastCacheConfiguration}. */
  public HazelcastCacheConfiguration() {}

  /**
   * Returns the distributed in-memory caches.
   *
   * @return the distributed in-memory caches
   */
  public List<CacheConfig> getCaches() {
    return caches;
  }

  /**
   * Convenience accessor for the cluster configuration.
   *
   * @return the distributed in-memory cache cluster configuration
   */
  public ClusterConfig getCluster() {
    return (server != null) ? server.getCluster() : null;
  }

  /**
   * Convenience accessor for the port auto-increment flag.
   *
   * @return whether port auto increment is enabled
   */
  public boolean getPortAutoIncrement() {
    return (server != null) && server.isPortAutoIncrement();
  }

  /**
   * Returns the server configuration.
   *
   * @return the server configuration
   */
  public ServerConfig getServer() {
    return server;
  }

  /**
   * Returns the Hazelcast configuration.
   *
   * @return the Hazelcast configuration
   */
  @Bean(name = "hazelcastConfig")
  public Config hazelcastConfig() {
    Config config = new Config();

    ClusterConfig cluster = getCluster();

    config.setInstanceName(cluster.getName());

    config.setProperty("hazelcast.logging.type", "slf4j");
    config.setProperty("hazelcast.rest.enabled", "false");

    NetworkConfig networkConfig = config.getNetworkConfig();

    networkConfig.setPort(cluster.getPort());
    networkConfig.setPortAutoIncrement(getPortAutoIncrement());
    networkConfig.setReuseAddress(true);

    JoinConfig joinConfig = networkConfig.getJoin();

    MulticastConfig multicastConfig = joinConfig.getMulticastConfig();
    multicastConfig.setEnabled(false);

    AwsConfig awsConfig = joinConfig.getAwsConfig();
    awsConfig.setEnabled(false);

    TcpIpConfig tcpIpConfig = joinConfig.getTcpIpConfig();
    tcpIpConfig.setEnabled(true);

    // Add the cache members
    String[] members = {"127.0.0.1"};

    if ((cluster.getMembers() != null) && (!cluster.getMembers().trim().isEmpty())) {
      members = cluster.getMembers().trim().split(",");
    }

    for (String member : members) {
      tcpIpConfig.addMember(member);
    }

    config.setProperty("hazelcast.application.validation.token", cluster.getPassword());

    config.setClusterName(cluster.getName());

    // Initialise the caches
    if (getCaches() != null) {
      for (CacheConfig cacheConfig : getCaches()) {
        MapConfig mapConfig = config.getMapConfig(cacheConfig.getName());

        mapConfig.setInMemoryFormat(
            Enum.valueOf(InMemoryFormat.class, cacheConfig.getInMemoryFormat()));

        EvictionConfig evictionConfig = new EvictionConfig();
        evictionConfig.setEvictionPolicy(
            Enum.valueOf(EvictionPolicy.class, cacheConfig.getEvictionPolicy()));
        evictionConfig.setMaxSizePolicy(
            Enum.valueOf(MaxSizePolicy.class, cacheConfig.getMaxSizePolicy()));
        evictionConfig.setSize(cacheConfig.getMaxSize());

        mapConfig.setEvictionConfig(evictionConfig);

        mapConfig.setStatisticsEnabled(cacheConfig.getStatisticsEnabled());

        mapConfig.setMaxIdleSeconds(cacheConfig.getMaxIdleSeconds());

        mapConfig.setBackupCount(cacheConfig.getBackupCount());

        mapConfig.setAsyncBackupCount(cacheConfig.getAsyncBackupCount());

        mapConfig.setReadBackupData(cacheConfig.getReadBackupData());
      }
    }

    return config;
  }

  /**
   * Returns whether the Hazelcast server cache is enabled.
   *
   * @return {@code true} if the Hazelcast server cache is enabled or {@code false} otherwise
   */
  public boolean isEnabled() {
    return (server != null) && server.isEnabled();
  }

  /**
   * Sets the distributed in-memory caches.
   *
   * @param caches the distributed in-memory caches
   */
  public void setCaches(List<CacheConfig> caches) {
    this.caches = caches;
  }

  /**
   * Sets the server configuration.
   *
   * @param server the server configuration
   */
  public void setServer(ServerConfig server) {
    this.server = server;
  }

  /**
   * The {@code CacheConfig} class provides access to the distributed in-memory cache configuration.
   */
  public static class CacheConfig {

    /** Are statistics enabled for the distributed in-memory cache? */
    boolean statisticsEnabled;

    /** The number of asynchronous backups for the distributed in-memory cache. */
    private int asyncBackupCount;

    /** The number of synchronous backups for the distributed in-memory cache. */
    private int backupCount;

    /** The eviction policy for the distributed in-memory cache. */
    private String evictionPolicy;

    /** The in-memory format for the distributed in-memory cache. */
    private String inMemoryFormat;

    /** The maximum idle seconds for the distributed in-memory cache. */
    private int maxIdleSeconds;

    /** The maximum size for the distributed in-memory cache. */
    private int maxSize;

    /** The maximum size policy for the distributed in-memory cache. */
    private String maxSizePolicy;

    /** The name of the distributed in-memory cache. */
    private String name;

    /** Is read-backup-data enabled for the distributed in-memory cache. */
    private boolean readBackupData;

    /** Constructs a new {@code CacheConfig}. */
    public CacheConfig() {}

    /**
     * Returns the number of asynchronous backups for the distributed in-memory cache.
     *
     * @return the number of asynchronous backups for the distributed in-memory cache
     */
    public int getAsyncBackupCount() {
      return asyncBackupCount;
    }

    /**
     * Returns the number of synchronous backups for the distributed in-memory cache.
     *
     * @return the number of synchronous backups for the distributed in-memory cache
     */
    public int getBackupCount() {
      return backupCount;
    }

    /**
     * Returns the eviction policy for the distributed in-memory cache.
     *
     * @return the eviction policy for the distributed in-memory cache
     */
    public String getEvictionPolicy() {
      return evictionPolicy;
    }

    /**
     * Returns the in-memory format for the distributed in-memory cache.
     *
     * @return the in-memory format for the distributed in-memory cache
     */
    public String getInMemoryFormat() {
      return inMemoryFormat;
    }

    /**
     * Returns the maximum idle seconds for the distributed in-memory cache.
     *
     * @return the maximum idle seconds for the distributed in-memory cache
     */
    public int getMaxIdleSeconds() {
      return maxIdleSeconds;
    }

    /**
     * Returns the maximum size for the distributed in-memory cache.
     *
     * @return the maximum size for the distributed in-memory cache
     */
    public int getMaxSize() {
      return maxSize;
    }

    /**
     * Returns the maximum size policy for the distributed in-memory cache.
     *
     * @return the maximum size policy for the distributed in-memory cache
     */
    public String getMaxSizePolicy() {
      return maxSizePolicy;
    }

    /**
     * Returns the name of the distributed in-memory cache.
     *
     * @return the name of the distributed in-memory cache
     */
    public String getName() {
      return name;
    }

    /**
     * Returns whether read-backup-data is enabled for the distributed in-memory cache.
     *
     * @return {@code true} if read-backup-data is enabled for the distributed in-memory cache or
     *     {@code false} otherwise
     */
    public boolean getReadBackupData() {
      return readBackupData;
    }

    /**
     * Returns whether statistics are enabled for the distributed in-memory cache?
     *
     * @return {@code true} if statistics are enabled for the distributed in-memory cache or {@code
     *     false} otherwise
     */
    public boolean getStatisticsEnabled() {
      return statisticsEnabled;
    }

    /**
     * Sets the number of asynchronous backups for the distributed in-memory cache.
     *
     * @param asyncBackupCount the number of asynchronous backups for the distributed in-memory
     *     cache
     */
    public void setAsyncBackupCount(int asyncBackupCount) {
      this.asyncBackupCount = asyncBackupCount;
    }

    /**
     * Sets the number of synchronous backups for the distributed in-memory cache.
     *
     * @param backupCount the number of synchronous backups for the distributed in-memory cache
     */
    public void setBackupCount(int backupCount) {
      this.backupCount = backupCount;
    }

    /**
     * Sets the eviction policy for the distributed in-memory cache.
     *
     * @param evictionPolicy the eviction policy for the distributed in-memory cache
     */
    public void setEvictionPolicy(String evictionPolicy) {
      this.evictionPolicy = evictionPolicy;
    }

    /**
     * Sets the in-memory format for the distributed in-memory cache.
     *
     * @param inMemoryFormat the in-memory format for the distributed in-memory cache
     */
    public void setInMemoryFormat(String inMemoryFormat) {
      this.inMemoryFormat = inMemoryFormat;
    }

    /**
     * Sets the maximum idle seconds for the distributed in-memory cache.
     *
     * @param maxIdleSeconds the maximum idle seconds for the distributed in-memory cache
     */
    public void setMaxIdleSeconds(int maxIdleSeconds) {
      this.maxIdleSeconds = maxIdleSeconds;
    }

    /**
     * Sets the maximum size for the distributed in-memory cache.
     *
     * @param maxSize the maximum size for the distributed in-memory cache
     */
    public void setMaxSize(int maxSize) {
      this.maxSize = maxSize;
    }

    /**
     * Sets the maximum size policy for the distributed in-memory cache.
     *
     * @param maxSizePolicy the maximum size policy for the distributed in-memory cache
     */
    public void setMaxSizePolicy(String maxSizePolicy) {
      this.maxSizePolicy = maxSizePolicy;
    }

    /**
     * Sets the name of the distributed in-memory cache.
     *
     * @param name the name of the distributed in-memory cache
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * Sets whether read-backup-data is enabled for the distributed in-memory cache.
     *
     * @param readBackupData {@code true} if read-backup-data enabled for the distributed in-memory
     *     cache or {@code false} otherwise
     */
    public void setReadBackupData(boolean readBackupData) {
      this.readBackupData = readBackupData;
    }

    /**
     * Sets whether statistics are enabled for the distributed in-memory cache.
     *
     * @param statisticsEnabled {@code true} if statistics are enabled for the distributed in-memory
     *     cache or {@code false} otherwise
     */
    public void setStatisticsEnabled(boolean statisticsEnabled) {
      this.statisticsEnabled = statisticsEnabled;
    }
  }

  /**
   * The {@code ClusterConfig} class provides access to the distributed in-memory cache cluster
   * configuration.
   */
  public static class ClusterConfig {

    /**
     * The comma-delimited IP addresses or hostnames for the members of the distributed in-memory
     * cache cluster.
     */
    private String members;

    /** The name of the distributed in-memory cache cluster. */
    private String name;

    /** The password used to connect to the distributed in-memory cache cluster. */
    private String password;

    /** The port for the distributed in-memory cache cluster. */
    private int port;

    /** Constructs a new {@code ClusterConfig}. */
    public ClusterConfig() {}

    /**
     * Returns the comma-delimited IP addresses or hostnames for the members of the distributed
     * in-memory cache cluster.
     *
     * @return the comma-delimited IP addresses or hostnames for the members of the distributed
     *     in-memory cache cluster
     */
    public String getMembers() {
      return members;
    }

    /**
     * Returns the name of the distributed in-memory cache cluster.
     *
     * @return the name of the distributed in-memory cache cluster
     */
    public String getName() {
      return name;
    }

    /**
     * Returns the password used to connect to the distributed in-memory cache cluster.
     *
     * @return the password used to connect to the distributed in-memory cache cluster
     */
    public String getPassword() {
      return password;
    }

    /**
     * Returns the port for the distributed in-memory cache cluster.
     *
     * @return the port for the distributed in-memory cache cluster
     */
    public int getPort() {
      return port;
    }

    /**
     * Sets the comma-delimited IP addresses or hostnames for the members of the distributed
     * in-memory cache cluster.
     *
     * @param members the comma-delimited IP addresses or hostnames for the members of the
     *     distributed in-memory cache cluster
     */
    public void setMembers(String members) {
      this.members = members;
    }

    /**
     * Sets the name of the distributed in-memory cache cluster.
     *
     * @param name the name of the distributed in-memory cache cluster
     */
    public void setName(String name) {
      this.name = name;
    }

    /**
     * Sets the password used to connect to the distributed in-memory cache cluster.
     *
     * @param password the password used to connect to the distributed in-memory cache cluster
     */
    public void setPassword(String password) {
      this.password = password;
    }

    /**
     * Sets the port for the distributed in-memory cache cluster.
     *
     * @param port the port for the distributed in-memory cache cluster
     */
    public void setPort(int port) {
      this.port = port;
    }
  }

  /**
   * The {@code ServerConfig} class provides access to the Hazelcast server configuration.
   *
   * <p>Bound under: inception.cache.hazelcast.server
   */
  public static class ServerConfig {

    /** The distributed in-memory cache cluster configuration. */
    private ClusterConfig cluster;

    /** Is the Hazelcast server cache enabled? */
    private boolean enabled;

    /** Is port auto increment enabled? */
    private boolean portAutoIncrement;

    /** Constructs a new {@code ServerConfig}. */
    public ServerConfig() {}

    /**
     * Returns the distributed in-memory cache cluster configuration.
     *
     * @return the distributed in-memory cache cluster configuration
     */
    public ClusterConfig getCluster() {
      return cluster;
    }

    /**
     * Returns whether the Hazelcast server cache is enabled.
     *
     * @return {@code true} if the Hazelcast server cache is enabled or {@code false} otherwise
     */
    public boolean isEnabled() {
      return enabled;
    }

    /**
     * Returns whether port auto increment is enabled.
     *
     * @return whether port auto increment is enabled
     */
    public boolean isPortAutoIncrement() {
      return portAutoIncrement;
    }

    /**
     * Sets the distributed in-memory cache cluster configuration.
     *
     * @param cluster the distributed in-memory cache cluster configuration
     */
    public void setCluster(ClusterConfig cluster) {
      this.cluster = cluster;
    }

    /**
     * Sets whether the Hazelcast server cache is enabled.
     *
     * @param enabled {@code true} if the Hazelcast server cache is enabled or {@code false}
     *     otherwise
     */
    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    /**
     * Sets whether port auto increment is enabled.
     *
     * @param portAutoIncrement is port auto increment enabled
     */
    public void setPortAutoIncrement(boolean portAutoIncrement) {
      this.portAutoIncrement = portAutoIncrement;
    }
  }
}
