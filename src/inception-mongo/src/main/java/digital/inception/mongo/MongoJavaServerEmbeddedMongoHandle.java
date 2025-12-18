/*
 * Copyright (c) Discovery Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Discovery Ltd
 * ("Confidential Information"). It may not be copied or reproduced in any manner
 * without the express written permission of Discovery Ltd.
 */

package digital.inception.mongo;

import com.mongodb.ConnectionString;
import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;
import java.io.IOException;
import java.net.ServerSocket;

/**
 * Handle for an embedded MongoDB server started via mongo-java-server.
 *
 * <p>This class is a small lifecycle wrapper around mongo-java-server. It starts an in-memory
 * embedded MongoDB server bound to {@code localhost} on a randomly selected free port, exposes the
 * chosen host/port for constructing a connection URI, and shuts the embedded server down when
 * {@link #close()} is invoked.
 *
 * <p><strong>Usage:</strong> register this handle as a Spring-managed bean with a destroy method
 * (e.g. {@code destroyMethod = "close"}) or otherwise ensure {@link #close()} is called on
 * application shutdown.
 *
 * @author Marcus Portmann
 */
final class MongoJavaServerEmbeddedMongoHandle implements AutoCloseable {

  private final String host;

  private final int port;

  private final MongoServer server;

  private MongoJavaServerEmbeddedMongoHandle(String host, int port, MongoServer server) {
    this.host = host;
    this.port = port;
    this.server = server;
  }

  /**
   * Stops the embedded MongoDB server.
   *
   * <p>This method is idempotent and safe to call during application shutdown.
   */
  @Override
  public void close() {
    if (server != null) {
      try {
        server.shutdown();
      } catch (RuntimeException ignored) {
        // best-effort shutdown
      }
    }
  }

  /**
   * Starts an embedded MongoDB server using mongo-java-server.
   *
   * <p>The server is started on {@code localhost} using an ephemeral free port. The returned handle
   * can be used to obtain the resolved host/port and to generate a MongoDB connection URI via
   * {@link #connectionUri(String)}.
   *
   * <p>The {@code ignored} parameter is currently unused and exists to align with call sites that
   * make the embedded decision based on an existing {@link ConnectionString}.
   *
   * @param ignored an existing MongoDB connection string (currently unused)
   * @return a handle representing the running embedded MongoDB server
   * @throws IllegalStateException if the server cannot be started for any reason (e.g. no free
   *     port, bind failures)
   */
  static MongoJavaServerEmbeddedMongoHandle start(ConnectionString ignored) {
    final String host = "localhost";
    final int port;
    try {
      port = findFreePort();
    } catch (IOException e) {
      throw new IllegalStateException(
          "Failed to allocate a free local port for embedded MongoDB.", e);
    }

    final MongoServer server;
    try {
      server = new MongoServer(new MemoryBackend());

      // Bind explicitly to the chosen port
      server.bind(host, port);
    } catch (Exception e) {
      throw new IllegalStateException("Failed to start embedded MongoDB via mongo-java-server.", e);
    }

    return new MongoJavaServerEmbeddedMongoHandle(host, port, server);
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
}
