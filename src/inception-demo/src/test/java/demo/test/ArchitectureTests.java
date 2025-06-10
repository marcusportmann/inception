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

package demo.test;

import com.tngtech.archunit.junit.AnalyzeClasses;
import digital.inception.test.archunit.InceptionArchUnitRules;
import org.junit.jupiter.api.Test;

/**
 * The {@code ArchitectureTests} class applies the ArchUnit tests.
 *
 * @author Marcus Portmann
 */
@AnalyzeClasses(packages = {"digital.inception", "demo"})
public class ArchitectureTests {

  /** Constructs a new {@code ArchitectureTests}. */
  public ArchitectureTests() {}

  /** Execute the ArchUnit rules. */
  @Test
  public void archUnitRulesTest() {
    InceptionArchUnitRules.executeArchUnitRules("digital.inception", "demo");
  }
}
