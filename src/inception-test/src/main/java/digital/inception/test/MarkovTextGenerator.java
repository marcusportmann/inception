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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The {@code MarkovTextGenerator} class generates random text using Markov chains.
 *
 * @author Marcus Portmann
 */
public class MarkovTextGenerator {
  private final Map<String, Chain<String>> chainsMap = new ConcurrentHashMap<>();
  private final Random random = new Random();

  /** Constructs a new {@code MarkovTextGenerator}. */
  public MarkovTextGenerator() {}

  /**
   * Returns a new {@code MarkovTextGenerator} populated using the Jules Verne text.
   *
   * @return a new {@code MarkovTextGenerator} populated using the Jules Verne text
   */
  public static MarkovTextGenerator getJulesVerneGenerator() {
    try (InputStream is =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("JulesVerne.txt");
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

      MarkovTextGenerator markovTextGenerator = new MarkovTextGenerator();

      String line;
      while ((line = reader.readLine()) != null) {
        markovTextGenerator.populate(line);
      }

      return markovTextGenerator;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Markov text generator", e);
    }
  }

  /**
   * Returns a new {@code MarkovTextGenerator} populated using the Lorem Ipsum text.
   *
   * @return a new {@code MarkovTextGenerator} populated using the Lorem Ipsum text
   */
  public static MarkovTextGenerator getLoremIpsumGenerator() {
    try (InputStream is =
            Thread.currentThread().getContextClassLoader().getResourceAsStream("LoremImpsum.txt");
        BufferedReader reader =
            new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

      MarkovTextGenerator markovTextGenerator = new MarkovTextGenerator();

      String line;
      while ((line = reader.readLine()) != null) {
        markovTextGenerator.populate(line);
      }

      return markovTextGenerator;
    } catch (Throwable e) {
      throw new RuntimeException("Failed to initialize the Markov text generator", e);
    }
  }

  /**
   * Generate random text.
   *
   * @return the random text
   */
  public Optional<String> generate() {
    List<Chain<String>> chains = new ArrayList<>(chainsMap.values());
    return generateWithChain(chains.get(random.nextInt(chains.size())), null);
  }

  /**
   * Generate random text of the specified length.
   *
   * @param fixedLength the length of the random text to generate
   * @return the random text
   */
  public Optional<String> generate(Integer fixedLength) {
    List<Chain<String>> chains = new ArrayList<>(chainsMap.values());
    return generateWithChain(chains.get(random.nextInt(chains.size())), fixedLength);
  }

  /**
   * Generate random text of the specified length, starting with the specified word.
   *
   * @param startWord the word to start with
   * @param fixedLength the length of the random text to generate
   * @return the random text
   */
  public Optional<String> generateWithStartWord(String startWord, Integer fixedLength) {
    Chain<String> chain = chainsMap.get(startWord.toLowerCase());
    return chain == null ? Optional.empty() : generateWithChain(chain, fixedLength);
  }

  /**
   * Generate random text starting with the specified word.
   *
   * @param startWord the word to start with
   * @return the random text
   */
  public Optional<String> generateWithStartWord(String startWord) {
    return generateWithStartWord(startWord, null);
  }

  /**
   * Populate the model.
   *
   * @param input the input
   */
  public void populate(String input) {
    String[] words = input.toLowerCase().split("\\s+");
    for (int i = 0; i < words.length - 1; i++) {
      String currentWord = words[i];
      String nextWord = words[i + 1];
      chainsMap.computeIfAbsent(currentWord, Chain::new).add(nextWord);
    }
  }

  private Optional<String> generateWithChain(Chain<String> chain, Integer fixedLength) {
    StringBuilder sentence = new StringBuilder();
    Chain<String> currentChain = chain;

    while (true) {
      sentence.append(currentChain.getOriginalInput()).append(" ");
      Optional<String> nextWord = currentChain.choose();
      if (nextWord.isEmpty() || (fixedLength != null && sentence.length() > fixedLength)) {
        break;
      }
      currentChain = chainsMap.get(nextWord.get());
      if (currentChain == null) {
        sentence.append(nextWord.get());
        break;
      }
    }

    return sentence.length() > 0 ? Optional.of(sentence.toString().trim()) : Optional.empty();
  }

  /**
   * The {@code Chain} class holds the information for A Markov chain with the original input and a
   * weight map with the other inputs and the number of times they come after the original input.
   */
  private static class Chain<T> {

    /** The original input. */
    private final T originalInput;

    /** A weight map with the inputs and the number of times they come after the original input. */
    private final Map<T, Integer> weightMap = new ConcurrentHashMap<>();

    /**
     * Constructs a new {@code Chain}.
     *
     * @param input the input
     */
    public Chain(T input) {
      this.originalInput = input;
    }

    /**
     * Add an input to the weight map.
     *
     * @param input the input
     */
    public void add(T input) {
      weightMap.merge(input, 1, Integer::sum);
    }

    /**
     * Choose an input from the weight map based on total weight and randomness.
     *
     * @return the input value or {@code null} if there is an error.
     */
    public Optional<T> choose() {
      int totalWeight = weightMap.values().stream().mapToInt(Integer::intValue).sum();
      int randomValue = new Random().nextInt(totalWeight);

      for (Map.Entry<T, Integer> entry : weightMap.entrySet()) {
        randomValue -= entry.getValue();
        if (randomValue < 0) {
          return Optional.of(entry.getKey());
        }
      }

      return Optional.empty();
    }

    /**
     * Returns the original input.
     *
     * @return the original input.
     */
    public T getOriginalInput() {
      return originalInput;
    }

    /**
     * Returns the weight map.
     *
     * @return the weight map.
     */
    public Map<T, Integer> getWeightMap() {
      return weightMap;
    }
  }
}
