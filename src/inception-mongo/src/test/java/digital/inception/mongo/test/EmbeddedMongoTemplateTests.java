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

package digital.inception.mongo.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import de.flapdoodle.embed.mongo.config.Net;
import de.flapdoodle.embed.mongo.distribution.Version;
import de.flapdoodle.embed.mongo.transitions.Mongod;
import de.flapdoodle.embed.mongo.transitions.RunningMongodProcess;
import de.flapdoodle.reverse.StateID;
import de.flapdoodle.reverse.TransitionWalker;
import de.flapdoodle.reverse.Transitions;
import de.flapdoodle.reverse.transitions.Start;
import digital.inception.mongo.MongoConfiguration;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;

/**
 * The {@code EmbeddedMongoTemplateTests} class contains the JUnit tests for the default
 * MongoTemplate using the embedded MongoDB process.
 *
 * @author Marcus Portmann
 */
@SpringBootTest(
    classes = {MongoConfiguration.class},
    properties = "spring.data.mongodb.uri=mongodb://localhost:49017/test",
    webEnvironment = WebEnvironment.NONE)
@Disabled
public class EmbeddedMongoTemplateTests {

  private static TransitionWalker.ReachedState<RunningMongodProcess>
      embeddedMongodProcessRunningState;

  @Qualifier("defaultMongoTemplate")
  @Autowired
  private MongoTemplate defaultMongoTemplate;

  /**
   * Test the functionality to read and write a document to the embedded MongoDB process using the
   * default MongoTemplate initialised by the MongoDBConfiguration class.
   */
  @Test
  public void testWriteAndReadFromMongo() throws Exception {
    assertNotNull(
        defaultMongoTemplate, "The default MongoTemplate should be initialized by Spring Boot");

    System.out.println(
        "Started embedded MongoDB process using host ("
            + embeddedMongodProcessRunningState.current().getServerAddress().getHost()
            + ") and port ("
            + embeddedMongodProcessRunningState.current().getServerAddress().getPort()
            + ")");

    assertEquals(49017, embeddedMongodProcessRunningState.current().getServerAddress().getPort());

    TestDocument originalDocument = new TestDocument();
    originalDocument.setName("John Doe");
    originalDocument.setAge(30);

    TestDocument savedDocument = defaultMongoTemplate.save(originalDocument);

    org.bson.Document document =
        defaultMongoTemplate.findById(
            savedDocument.getId(), org.bson.Document.class, "test_document");

    TestDocument retrievedDocument =
        defaultMongoTemplate.findById(savedDocument.getId(), TestDocument.class);

    assertNotNull(retrievedDocument);

    assertEquals(originalDocument, retrievedDocument);

    defaultMongoTemplate.remove(retrievedDocument);

    TestDocument deletedDocument =
        defaultMongoTemplate.findById(savedDocument.getId(), TestDocument.class);

    assertNull(deletedDocument, "The document should be deleted from the database");

    //    Mongod mongod =
    //        Mongod.builder()
    //            .net(Start.to(Net.class).initializedWith(Net.defaults().withPort(49017)))
    //            .build();
    //
    //    Transitions transitions = mongod.transitions(Version.V8_0_3);
    //
    //    try (TransitionWalker.ReachedState<RunningMongodProcess> running =
    //        transitions.walker().initState(StateID.of(RunningMongodProcess.class))) {
    //      System.out.println(
    //          "Started embedded MongoDB process using host ("
    //              + running.current().getServerAddress().getHost()
    //              + ") and port ("
    //              + running.current().getServerAddress().getPort()
    //              + ")");
    //
    //      assertEquals(49017, running.current().getServerAddress().getPort());
    //
    //      TestDocument originalDocument = new TestDocument();
    //      originalDocument.setName("John Doe");
    //      originalDocument.setAge(30);
    //
    //      TestDocument savedDocument = defaultMongoTemplate.save(originalDocument);
    //
    //      TestDocument retrievedDocument =
    //          defaultMongoTemplate.findById(savedDocument.getId(), TestDocument.class);
    //
    //    assertNotNull(retrievedDocument);
    //
    //    assertEquals(originalDocument, retrievedDocument);
    //
    //    defaultMongoTemplate.remove(retrievedDocument);
    //
    //    TestDocument deletedDocument =
    //        defaultMongoTemplate.findById(savedDocument.getId(), TestDocument.class);
    //
    //    assertNull(deletedDocument, "The document should be deleted from the database");
    //    }
  }

  /** Shutdown the embedded MongoDB process. */
  @AfterAll
  static void shutdownEmbeddedMongoDBProcess() {
    if (embeddedMongodProcessRunningState != null) {
      embeddedMongodProcessRunningState.close();
    }
  }

  /** Start the embedded MongoDB process. */
  @BeforeAll
  static void startEmbeddedMongoDBProcess() {
    Mongod mongod =
        Mongod.builder()
            .net(Start.to(Net.class).initializedWith(Net.defaults().withPort(49017)))
            .build();

    Transitions transitions = mongod.transitions(Version.V6_0_18);

    embeddedMongodProcessRunningState =
        transitions.walker().initState(StateID.of(RunningMongodProcess.class));
  }
}
