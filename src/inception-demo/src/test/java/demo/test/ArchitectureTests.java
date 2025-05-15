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
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.junit.ArchTests;
import digital.inception.test.archunit.InceptionArchUnitTests;

/**
 * The {@code ArchitectureTests} class applies the ArchUnit tests.
 *
 * @author Marcus Portmann
 */
@AnalyzeClasses(packages = {"digital.inception", "demo"})
public class ArchitectureTests {

  /** Include the standard ArchUnit tests for the Inception Framework. */
  @ArchTest
  static final ArchTests INCEPTION_ARCH_UNIT_TESTS = ArchTests.in(InceptionArchUnitTests.class);

  /** Constructs a new {@code ArchitectureTests}. */
  public ArchitectureTests() {}
}
