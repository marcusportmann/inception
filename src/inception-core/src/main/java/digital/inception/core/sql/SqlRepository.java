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

package digital.inception.core.sql;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

/**
 * The {@code SqlRepository} class implements a Spring component that provides an in-memory
 * repository for SQL resources discovered on the classpath.
 *
 * <p><strong>Location &amp; naming</strong>
 *
 * <ul>
 *   <li>All SQL files live under a base path (default {@code sql}) on the classpath, e.g. {@code
 *       classpath:sql/...}.
 *   <li>Default SQL files end with {@code .sql}. Keys are the relative path under the base path
 *       without the extension (e.g., {@code account/find_by_email}).
 *   <li>Optional vendor-specific overrides end with {@code -<vendor>.sql} (e.g., {@code
 *       account/find_by_email-postgres.sql}). When present, they replace the default with the same
 *       base key.
 * </ul>
 *
 * <p><strong>Initialization gating</strong> <br>
 * Initialization occurs only if at least one {@code *.sql} resource exists under the base path. If
 * none are found, the repository remains empty.
 *
 * <p><strong>Configuration</strong> (via Spring {@link Environment})
 *
 * <ul>
 *   <li>{@code app.sql.enabled} (default {@code true}): globally enable/disable loading.
 *   <li>{@code app.sql.base-path} (default {@code sql}): classpath base directory to scan.
 *   <li>{@code app.sql.vendor} (optional): vendor suffix for override files (e.g., {@code
 *       postgres}, {@code oracle}).
 * </ul>
 *
 * @author Marcus Portmann
 */
@Component
public class SqlRepository {

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(SqlRepository.class);

  /** The SQL resources. */
  private final Map<String, String> sql;

  /**
   * Constructs a new {@code SqlRepository} instance and eagerly loads SQL resources if enabled and
   * present.
   *
   * @param applicationContext the Spring application context
   */
  public SqlRepository(ApplicationContext applicationContext) {
    Environment environment = applicationContext.getEnvironment();

    boolean enabled = environment.getProperty("nova.sql.enabled", Boolean.class, Boolean.TRUE);
    String basePath = environment.getProperty("nova.sql.base-path", "sql").trim();
    String vendor = environment.getProperty("nova.sql.vendor", "").trim();

    // Attempt to determine the database vendor from the application data source
    if (!StringUtils.hasText(vendor)) {
      try {
        DataSource applicationDataSource =
            applicationContext.getBean("applicationDataSource", DataSource.class);

        try (Connection connection = applicationDataSource.getConnection()) {
          DatabaseMetaData metaData = connection.getMetaData();

          String product =
              String.valueOf(metaData.getDatabaseProductName()).toLowerCase(Locale.ROOT);
          String url = String.valueOf(metaData.getURL()).toLowerCase(Locale.ROOT);

          // Oracle
          if (product.contains("oracle") || url.startsWith("jdbc:oracle:")) {
            vendor = "oracle";

            // Microsoft SQL Server
          } else if (product.contains("microsoft sql server")
              || product.contains("sql server")
              || url.startsWith("jdbc:sqlserver:")) {
            vendor = "mssql";

            // PostgreSQL
          } else if (product.contains("postgresql")
              || product.contains("postgres")
              || url.startsWith("jdbc:postgresql:")) {
            vendor = "postgres";

            // Optional: keep H2 convenience (commonly used in tests)
          } else if (product.contains("h2") || url.startsWith("jdbc:h2:")) {
            vendor = "h2";
          }
        }
      } catch (Throwable ignored) {
      }
    }

    if (!enabled) {
      log.debug("SQL loading disabled via property 'nova.sql.enabled=false'");
      this.sql = Collections.emptyMap();
      return;
    }

    final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    // Probe for presence of any *.sql under basePath. If none, remain empty.
    final String defaultsGlob = "classpath*:" + basePath + "/**/*.sql";
    final Resource[] defaults;
    try {
      defaults = resolver.getResources(defaultsGlob);
    } catch (IOException e) {
      throw new BeanInitializationException(
          "Failed to scan for SQL resources using glob (" + defaultsGlob + ")", e);
    }

    if (defaults.length == 0) {
      log.info("No SQL resources found under base path ({})", basePath);
      this.sql = Collections.emptyMap();
      return;
    }

    // Vendor-specific overrides (optional)
    Resource[] overrides = new Resource[0];
    if (!vendor.isEmpty()) {
      final String vendorGlob = "classpath*:" + basePath + "/**/*-" + vendor + ".sql";
      try {
        overrides = resolver.getResources(vendorGlob);
      } catch (IOException e) {
        throw new BeanInitializationException(
            "Failed to scan for vendor SQL overrides for vendor ("
                + vendor
                + ") using glob ("
                + vendorGlob
                + ")",
            e);
      }
    }

    this.sql = loadAll(defaults, overrides, basePath, vendor);
  }

  /**
   * Returns whether the SQL resource repository contains a SQL resource with the specified name
   * (key).
   *
   * @param name the name (key) for the SQL resource
   * @return {@code true} if the SQL resource repository contains a SQL resource with the specified
   *     name (key) or {@code false} otherwise
   */
  public boolean contains(String name) {
    return sql.containsKey(name);
  }

  /**
   * Find the SQL resource with the specified name (key).
   *
   * @param name the name (key) for the SQL resource
   * @return an {@code Optional<String>} containing the SQL resource with the specified name (key)
   *     or an empty {@code Optional} if the SQL resource could not be found
   */
  public Optional<String> find(String name) {
    return Optional.ofNullable(sql.get(name));
  }

  /**
   * Returns the SQL resource with the specified name (key).
   *
   * <p>Example: resource {@code sql/account/find_by_email.sql} &rarr; name {@code
   * account/find_by_email}
   *
   * @param name the name (key) for the SQL resource
   * @return the SQL resource with the specified name (key)
   * @throws IllegalArgumentException if the SQL resource could not be found
   */
  public String get(String name) {
    String s = sql.get(name);
    if (s == null) {
      throw new IllegalArgumentException("No SQL named (" + name + ")");
    }
    return s;
  }

  /**
   * Returns the names for all the registered SQL resources.
   *
   * @return the names for all the registered SQL resources
   */
  public Set<String> keys() {
    return sql.keySet();
  }

  private static String describe(Resource resource) {
    try {
      return resource.getURI().toString();
    } catch (Exception e) {
      return resource.getDescription();
    }
  }

  private static String readSql(Resource resource) throws IOException {
    try (var in = resource.getInputStream()) {
      String text = StreamUtils.copyToString(in, StandardCharsets.UTF_8);
      // Strip UTF-8 BOM if present
      if (!text.isEmpty() && text.charAt(0) == '\uFEFF') {
        text = text.substring(1);
      }
      return text.trim();
    }
  }

  /**
   * Derives the repository key from a resource URI. Works with {@code file:} and {@code jar:} URIs,
   * and on Windows/Unix alike.
   *
   * @param resource resource
   * @param basePath classpath base (e.g., {@code sql})
   * @param stripSuffix optional trailing suffix to remove from the filename stem (e.g., {@code
   *     -postgres})
   * @return key relative to basePath without {@code .sql} and optional suffix
   */
  private static String deriveKey(Resource resource, String basePath, String stripSuffix)
      throws IOException {
    final URI uri = Objects.requireNonNull(resource.getURI(), "Resource URI must not be null");
    String loc = uri.toString().replace('\\', '/'); // normalize

    final String needle = "/" + basePath + "/";
    final int i = loc.indexOf(needle);
    if (i < 0) {
      throw new IOException("Resource not under expected basePath '" + basePath + "': " + loc);
    }

    String sub = loc.substring(i + needle.length()); // e.g. "account/find_by_email-postgres.sql"
    if (sub.endsWith(".sql")) {
      sub = sub.substring(0, sub.length() - 4);
    }
    if (!stripSuffix.isEmpty() && sub.endsWith(stripSuffix)) {
      sub = sub.substring(0, sub.length() - stripSuffix.length());
    }
    return sub;
  }

  private Map<String, String> loadAll(
      Resource[] defaults, Resource[] vendorOverrides, String basePath, String vendorName) {

    final Map<String, String> out = new HashMap<>(defaults.length + vendorOverrides.length);
    int loaded = 0;
    int overridden = 0;

    for (Resource r : defaults) {
      try {
        final String key = deriveKey(r, basePath, "");
        out.put(key, readSql(r));
        loaded++;
      } catch (Exception e) {
        log.warn("Skipping unreadable SQL resource: {}", describe(r), e);
      }
    }

    if (vendorOverrides.length > 0 && !vendorName.isEmpty()) {
      final String suffix = "-" + vendorName;
      for (Resource r : vendorOverrides) {
        try {
          final String baseKey = deriveKey(r, basePath, suffix);
          final String prev = out.put(baseKey, readSql(r));
          if (prev != null) {
            overridden++;
            log.info(
                "Overrode SQL ({}) with vendor-specific variant for vendor ({})",
                baseKey,
                vendorName);
          } else {
            loaded++;
            log.info("Loaded vendor-specific SQL ({}) for vendor ({})", baseKey, vendorName);
          }
        } catch (Exception e) {
          log.warn("Skipping unreadable vendor SQL resource ({})", describe(r), e);
        }
      }
    }

    log.info(
        "Loaded "
            + loaded
            + " SQL resources with "
            + overridden
            + " overrides found under the base path ("
            + basePath
            + ")");

    return Collections.unmodifiableMap(out);
  }
}
