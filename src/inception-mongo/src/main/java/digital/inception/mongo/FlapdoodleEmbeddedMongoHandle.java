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

package digital.inception.mongo;

import com.mongodb.ConnectionString;
import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.StateID;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.Transitions;
import de.flapdoodle.reverse.transitions.Start;
import java.io.IOException;
import java.net.ServerSocket;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * Handle for an embedded MongoDB server started via Flapdoodle.
 *
 * <p>This class is a small lifecycle wrapper around Flapdoodle's embedded MongoDB “transitions”
 * API. It starts an embedded {@code mongod} bound to {@code localhost} on a randomly selected free
 * port, exposes the chosen host/port for constructing a connection URI, and shuts the embedded
 * server down when {@link #close()} is invoked.
 *
 * <p>The embedded process is represented by a {@link TransitionWalker.ReachedState} for {@link
 * RunningMongodProcess}. Releasing that reached state (via {@link #close()}) stops the {@code
 * mongod} process and cleans up temporary resources created by Flapdoodle.
 *
 * <p><strong>Usage:</strong> register this handle as a Spring-managed bean with a destroy method
 * (e.g. {@code destroyMethod = "close"}) or otherwise ensure {@link #close()} is called on
 * application shutdown.
 *
 * @author Marcus Portmann
 */
final class FlapdoodleEmbeddedMongoHandle implements AutoCloseable {

  private final String host;

  private final int port;

  private final TransitionWalker.ReachedState<RunningMongodProcess> running;

  private FlapdoodleEmbeddedMongoHandle(
      String host, int port, TransitionWalker.ReachedState<RunningMongodProcess> running) {
    this.host = host;
    this.port = port;
    this.running = running;
  }

  /**
   * Stops the embedded MongoDB server and releases any temporary resources created by Flapdoodle.
   *
   * <p>This method is idempotent and safe to call during application shutdown.
   */
  @Override
  public void close() {
    if (running != null) {
      running.close(); // shuts down the mongod process + cleans up
    }
  }

  /**
   * Starts an embedded MongoDB server if Flapdoodle is available on the runtime classpath.
   *
   * <p>The server is started on {@code localhost} using an ephemeral free port. The returned handle
   * can be used to obtain the resolved host/port and to generate a MongoDB connection URI via
   * {@link #connectionUri(String)}.
   *
   * <p>The {@code ignored} parameter is currently unused and exists to align with call sites that
   * make the embedded decision based on an existing {@link ConnectionString}. Future versions may
   * use it to derive version or configuration.
   *
   * @param ignored an existing MongoDB connection string (currently unused)
   * @param embeddedMongoDbServerVersion the MongoDB version to use for the embedded server
   * @return a handle representing the running embedded MongoDB server
   * @throws IllegalStateException if Flapdoodle classes are not present on the classpath
   * @throws IllegalStateException if the server cannot be started for any reason (e.g. no free
   *     port, binary download/extraction failures, process start failures)
   */
  static FlapdoodleEmbeddedMongoHandle startIfAvailable(
      ConnectionString ignored, String embeddedMongoDbServerVersion) {
    if (!ClassUtils.isPresent(
        "de.flapdoodle.embed.mongo.transitions.Mongod",
        FlapdoodleEmbeddedMongoHandle.class.getClassLoader())) {
      throw new IllegalStateException(
          "Embedded MongoDB server requested, but Flapdoodle is not on the classpath "
              + "(missing de.flapdoodle.embed:de.flapdoodle.embed.mongo).");
    }

    final String host = "localhost";
    final int port;
    try {
      port = findFreePort();
    } catch (IOException e) {
      throw new IllegalStateException(
          "Failed to allocate a free local port for embedded MongoDB.", e);
    }

    Mongod mongod =
        Mongod.builder()
            .net(Start.to(Net.class).initializedWith(Net.defaults().withPort(port)))
            .build();

    final TransitionWalker.ReachedState<RunningMongodProcess> running;
    try {
      Transitions transitions =
          mongod.transitions(resolveEmbeddedMongoDbServerVersion(embeddedMongoDbServerVersion));
      running = transitions.walker().initState(StateID.of(RunningMongodProcess.class));
    } catch (RuntimeException e) {
      throw new IllegalStateException("Failed to start embedded MongoDB via Flapdoodle.", e);
    }

    return new FlapdoodleEmbeddedMongoHandle(host, port, running);
  }

  /**
   * Builds a {@code mongodb://} connection URI for the embedded server using the supplied database
   * name.
   *
   * @param database the database name to append to the URI
   * @return a MongoDB connection URI pointing at the embedded server
   */
  String connectionUri(String database) {
    return "mongodb://" + host + ":" + port + "/" + database;
  }

  /**
   * Returns the host that the embedded MongoDB server is bound to.
   *
   * @return the host, typically {@code "localhost"}
   */
  String host() {
    return host;
  }

  /**
   * Returns the TCP port that the embedded MongoDB server is bound to.
   *
   * @return the chosen TCP port
   */
  int port() {
    return port;
  }

  /**
   * Allocates an ephemeral free TCP port on the local machine.
   *
   * @return a free TCP port number
   * @throws IOException if a free port cannot be allocated
   */
  private static int findFreePort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0)) {
      socket.setReuseAddress(true);
      return socket.getLocalPort();
    }
  }

  private static Version.Main resolveEmbeddedMongoDbServerVersion(
      String embeddedMongoDbServerVersion) {
    String v = embeddedMongoDbServerVersion == null ? "" : embeddedMongoDbServerVersion.trim();
    if (!StringUtils.hasText(v)) {
      throw new IllegalArgumentException(
          "Property 'inception.application.mongodb.embedded' must be set to enable the embedded MongoDB server");
    }

    String key = v.toUpperCase().replace('-', '_');

    return switch (key) {
      case "8", "8.0", "V8", "V8_0", "V8.0" -> Version.Main.V8_0;
      case "7", "7.0", "V7", "V7_0", "V7.0" -> Version.Main.V7_0;
      case "6", "6.0", "V6", "V6_0", "V6.0" -> Version.Main.V6_0;
      default -> {
        try {
          yield Version.Main.valueOf(key);
        } catch (IllegalArgumentException ex) {
          throw new IllegalArgumentException(
              "Invalid value for 'inception.application.mongodb.embedded': '"
                  + embeddedMongoDbServerVersion
                  + "'. Expected values like '8.0', '7.0', 'V8_0', 'V7_0', or 'PRODUCTION'.",
              ex);
        }
      }
    };
  }
}
