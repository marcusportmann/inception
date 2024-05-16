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
package digital.inception.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import org.junit.jupiter.api.Test;

/**
 * The <b>MarkovTextGeneratorTest</b> class.
 *
 * @author Marcus Portmann
 */
public class MarkovTextGeneratorTest {

  /** Test the Markov text generator functionality. */
  @Test
  void markovTextGeneratorTest() {
    MarkovTextGenerator markovTextGenerator = MarkovTextGenerator.getLoremIpsumGenerator();

    Optional<String> generatedTextOptional = markovTextGenerator.generate(1024);

    assertTrue(generatedTextOptional.isPresent());

    markovTextGenerator = MarkovTextGenerator.getJulesVerneGenerator();

    generatedTextOptional = markovTextGenerator.generate(1024);

    assertTrue(generatedTextOptional.isPresent());
  }
}
