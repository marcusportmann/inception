/// *
// * Copyright Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.flowable.flowable;
//
/// **
// * The {@code EventDbSchemaManager} class provides a no-op database schema manager for the Process
// * component of the Flowable platform, which defers the Flowable database schema initialization to
// * the standard Inception Framework database initialization capabilities using the
// * {@code db/flowable-all.changelog.xml} Liquibase changelog on the classpath.
// *
// * @author Marcus Portmann
// */
// public class ProcessDbSchemaManager extends org.flowable.engine.impl.db.ProcessDbSchemaManager {
//
//  @Override
//  public void schemaCreate() {}
//
//  @Override
//  public void schemaDrop() {}
//
//  @Override
//  public String schemaUpdate() { return null;}
//
//  @Override
//  public void schemaCheckVersion() {}
// }
