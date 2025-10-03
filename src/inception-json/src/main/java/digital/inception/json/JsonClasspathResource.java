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

package digital.inception.json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The {@code JsonClasspathResource<T>} record provides an immutable DTP for loading files in JSON
 * format from a top-level directory and its immediate subdirectories on the classpath,
 * deserializing each resource into a given type {@code T}.
 *
 * <p>It loads JSON resources from:
 *
 * <ul>
 *   <li>{@code basePath/*.json} (with {@code directoryName = ""})
 *   <li>{@code basePath/directory/*.json} (with {@code directoryName = "directory"})
 * </ul>
 *
 * @param fileName the file name for the JSON file
 * @param directoryName the directory (empty string if top-level under {@code basePath})
 * @param fullPath the classpath-relative full path (e.g. {@code basePath/dir/file.json})
 * @param json the raw JSON content
 * @param sha1Hex the SHA-1 hash (hex, lowercase) of {@code json}
 * @param value the deserialized value of type {@code T}
 * @param <T> the deserialized value type
 * @author Marcus Portmann
 */
public record JsonClasspathResource<T>(
    String fileName, String directoryName, String fullPath, String json, String sha1Hex, T value) {

  /**
   * Loads JSON resources from the classpath using the current thread's context {@link ClassLoader}
   * and deserializes each file into {@code targetType}.
   *
   * <p>Resources are discovered under {@code basePath} and its immediate subdirectories:
   *
   * <ul>
   *   <li>{@code basePath/*.json} (top-level, empty {@code directoryName})
   *   <li>{@code basePath/<directory>/*.json} (one level down)
   * </ul>
   *
   * <p>If {@code basePath} starts with {@code "/"}, the leading slash is ignored. Duplicate
   * resources (same directory/file name across different classpath roots) are de-duplicated, with
   * the first occurrence retained. Discovery works for both exploded directories and JAR files.
   *
   * @param basePath the classpath-relative directory to scan (e.g. {@code "data"} or {@code
   *     "data/catalog"}); a leading {@code "/"} is allowed and will be ignored
   * @param objectMapper the Jackson {@link ObjectMapper} used for deserialization
   * @param targetType the concrete target type to deserialize each JSON resource into
   * @param <T> the element type produced for each resource
   * @return a list of {@link JsonClasspathResource} items containing file metadata, raw JSON, and
   *     deserialized values
   * @throws NullPointerException if {@code objectMapper} or {@code targetType} is {@code null}
   * @throws java.io.UncheckedIOException if scanning the classpath or deserializing a resource
   *     fails with an {@link java.io.IOException}
   * @throws RuntimeException if reading directory resources fails for non-IO reasons
   */
  public static <T> List<JsonClasspathResource<T>> loadFromClasspath(
      String basePath, ObjectMapper objectMapper, Class<T> targetType) {
    return loadFromClasspath(
        basePath, Thread.currentThread().getContextClassLoader(), objectMapper, targetType);
  }

  /**
   * Loads JSON resources from the classpath using the supplied {@link ClassLoader} and deserializes
   * each file into {@code targetType}.
   *
   * <p>Resources are discovered under {@code basePath} and its immediate subdirectories:
   *
   * <ul>
   *   <li>{@code basePath/*.json} (top-level, empty {@code directoryName})
   *   <li>{@code basePath/<directory>/*.json} (one level down)
   * </ul>
   *
   * <p>If {@code basePath} starts with {@code "/"}, the leading slash is ignored. Duplicate
   * resources (same directory/file name across different classpath roots) are de-duplicated, with
   * the first occurrence retained. Discovery works for both exploded directories and JAR files.
   *
   * @param basePath the classpath-relative directory to scan (e.g. {@code "data"} or {@code
   *     "data/catalog"}); a leading {@code "/"} is allowed and will be ignored
   * @param classLoader the class loader to use for locating {@code basePath} on the classpath
   * @param objectMapper the Jackson {@link ObjectMapper} used for deserialization
   * @param targetType the concrete target type to deserialize each JSON resource into
   * @param <T> the element type produced for each resource
   * @return a list of {@link JsonClasspathResource} items containing file metadata, raw JSON, and
   *     deserialized values
   * @throws NullPointerException if {@code objectMapper} or {@code targetType} is {@code null}
   * @throws java.io.UncheckedIOException if scanning the classpath or deserializing a resource
   *     fails with an {@link java.io.IOException}
   * @throws RuntimeException if reading directory resources fails for non-IO reasons
   */
  public static <T> List<JsonClasspathResource<T>> loadFromClasspath(
      String basePath, ClassLoader classLoader, ObjectMapper objectMapper, Class<T> targetType) {
    Objects.requireNonNull(objectMapper, "objectMapper");
    Objects.requireNonNull(targetType, "targetType");
    return loadInternal(basePath, classLoader, json -> objectMapper.readValue(json, targetType));
  }

  /**
   * Loads JSON resources from the classpath using the current thread's context {@link ClassLoader}
   * and deserializes each file into the generic type specified by {@code typeRef}.
   *
   * <p>Use this overload for parameterized types (e.g. {@code List<Foo>}), where a raw {@link
   * Class} is insufficient.
   *
   * <p>Resources are discovered under {@code basePath} and its immediate subdirectories:
   *
   * <ul>
   *   <li>{@code basePath/*.json} (top-level, empty {@code directoryName})
   *   <li>{@code basePath/<directory>/*.json} (one level down)
   * </ul>
   *
   * <p>If {@code basePath} starts with {@code "/"}, the leading slash is ignored. Duplicate
   * resources (same directory/file name across different classpath roots) are de-duplicated, with
   * the first occurrence retained. Discovery works for both exploded directories and JAR files.
   *
   * @param basePath the classpath-relative directory to scan (e.g. {@code "data"} or {@code
   *     "data/catalog"}); a leading {@code "/"} is allowed and will be ignored
   * @param mapper the Jackson {@link ObjectMapper} used for deserialization
   * @param typeRef a {@link TypeReference} describing the target (possibly parameterized) type
   * @param <T> the element type produced for each resource
   * @return a list of {@link JsonClasspathResource} items containing file metadata, raw JSON, and
   *     deserialized values
   * @throws NullPointerException if {@code mapper} or {@code typeRef} is {@code null}
   * @throws java.io.UncheckedIOException if scanning the classpath or deserializing a resource
   *     fails with an {@link java.io.IOException}
   * @throws RuntimeException if reading directory resources fails for non-IO reasons
   */
  public static <T> List<JsonClasspathResource<T>> loadFromClasspath(
      String basePath, ObjectMapper mapper, TypeReference<T> typeRef) {
    return loadFromClasspath(
        basePath, Thread.currentThread().getContextClassLoader(), mapper, typeRef);
  }

  /**
   * Loads JSON resources from the classpath using the supplied {@link ClassLoader} and deserializes
   * each file into the generic type specified by {@code typeRef}.
   *
   * <p>Use this overload for parameterized types (e.g. {@code List<Foo>}), where a raw {@link
   * Class} is insufficient.
   *
   * <p>Resources are discovered under {@code basePath} and its immediate subdirectories:
   *
   * <ul>
   *   <li>{@code basePath/*.json} (top-level, empty {@code directoryName})
   *   <li>{@code basePath/<directory>/*.json} (one level down)
   * </ul>
   *
   * <p>If {@code basePath} starts with {@code "/"}, the leading slash is ignored. Duplicate
   * resources (same directory/file name across different classpath roots) are de-duplicated, with
   * the first occurrence retained. Discovery works for both exploded directories and JAR files.
   *
   * @param basePath the classpath-relative directory to scan (e.g. {@code "data"} or {@code
   *     "data/catalog"}); a leading {@code "/"} is allowed and will be ignored
   * @param classLoader the class loader to use for locating {@code basePath} on the classpath
   * @param objectMapper the Jackson {@link ObjectMapper} used for deserialization
   * @param typeRef a {@link TypeReference} describing the target (possibly parameterized) type
   * @param <T> the element type produced for each resource
   * @return a list of {@link JsonClasspathResource} items containing file metadata, raw JSON, and
   *     deserialized values
   * @throws NullPointerException if {@code objectMapper} or {@code typeRef} is {@code null}
   * @throws java.io.UncheckedIOException if scanning the classpath or deserializing a resource
   *     fails with an {@link java.io.IOException}
   * @throws RuntimeException if reading directory resources fails for non-IO reasons
   */
  public static <T> List<JsonClasspathResource<T>> loadFromClasspath(
      String basePath,
      ClassLoader classLoader,
      ObjectMapper objectMapper,
      TypeReference<T> typeRef) {
    Objects.requireNonNull(objectMapper, "objectMapper");
    Objects.requireNonNull(typeRef, "typeRef");
    return loadInternal(basePath, classLoader, json -> objectMapper.readValue(json, typeRef));
  }

  private static <T> T deserializeSafe(
      Deserializer<T> deserializer, String json, String basePath, String dirName, String fileName) {
    try {
      return deserializer.fromJson(json);
    } catch (IOException ex) {
      String loc =
          (dirName == null || dirName.isEmpty())
              ? basePath + "/" + fileName
              : basePath + "/" + dirName + "/" + fileName;
      throw new UncheckedIOException("Failed to deserialize JSON at '" + loc + "'", ex);
    }
  }

  private static String key(String directoryName, String fileName) {
    return (directoryName == null || directoryName.isEmpty())
        ? fileName
        : (directoryName + "/" + fileName);
  }

  private static <T> List<JsonClasspathResource<T>> loadInternal(
      String basePath, ClassLoader classLoader, Deserializer<T> deserializer) {

    Objects.requireNonNull(basePath, "basePath");
    if (basePath.startsWith("/")) {
      basePath = basePath.substring(1); // classpath resources are slash-based, no leading '/'
    }

    // Deterministic order and de-duplication if the same resource appears multiple times
    Map<String, JsonClasspathResource<T>> results = new LinkedHashMap<>();

    try {
      Enumeration<URL> roots = classLoader.getResources(basePath);
      while (roots.hasMoreElements()) {
        URL url = roots.nextElement();
        String protocol = url.getProtocol();

        if ("file".equals(protocol)) {
          readFromDirectory(results, basePath, url, deserializer);
        } else if ("jar".equals(protocol)) {
          readFromJar(results, basePath, url, deserializer);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to scan classpath for " + basePath, e);
    }

    return new ArrayList<>(results.values());
  }

  private static String readAllUtf8(InputStream in) throws IOException {
    try (BufferedReader br =
        new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      StringBuilder sb = new StringBuilder();
      char[] buf = new char[8192];
      int n;
      while ((n = br.read(buf)) != -1) sb.append(buf, 0, n);
      return sb.toString();
    }
  }

  private static <T> void readFromDirectory(
      Map<String, JsonClasspathResource<T>> sink,
      String basePath,
      URL baseUrl,
      Deserializer<T> deserializer) {

    try {
      Path baseDir = Paths.get(URI.create(baseUrl.toString()));
      if (!Files.isDirectory(baseDir)) return;

      // 1) Top-level JSON files: basePath/*.json  (directoryName = "")
      try (DirectoryStream<Path> topFiles = Files.newDirectoryStream(baseDir, "*.json")) {
        for (Path jsonPath : topFiles) {
          String fileName = jsonPath.getFileName().toString();
          String directoryName = "";
          String key = key(directoryName, fileName);
          if (!sink.containsKey(key)) {
            String json = Files.readString(jsonPath, StandardCharsets.UTF_8);
            String sha1Hex = sha1Hex(json);
            String fullPath = basePath + "/" + fileName;
            T value = deserializeSafe(deserializer, json, basePath, directoryName, fileName);
            sink.put(
                key,
                new JsonClasspathResource<>(
                    fileName, directoryName, fullPath, json, sha1Hex, value));
          }
        }
      }

      // 2) Immediate subdirectories: basePath/<directory>/*.json
      try (DirectoryStream<Path> subDirectories =
          Files.newDirectoryStream(baseDir, Files::isDirectory)) {
        for (Path subDirectory : subDirectories) {
          String subDirectoryName = subDirectory.getFileName().toString();
          try (DirectoryStream<Path> files = Files.newDirectoryStream(subDirectory, "*.json")) {
            for (Path jsonPath : files) {
              String fileName = jsonPath.getFileName().toString();
              String key = key(subDirectoryName, fileName);
              if (!sink.containsKey(key)) {
                String json = Files.readString(jsonPath, StandardCharsets.UTF_8);
                String sha1Hex = sha1Hex(json);
                String fullPath =
                    basePath + "/" + subDirectoryName + "/" + fileName; // classpath-relative
                T value = deserializeSafe(deserializer, json, basePath, subDirectoryName, fileName);
                sink.put(
                    key,
                    new JsonClasspathResource<>(
                        fileName, subDirectoryName, fullPath, json, sha1Hex, value));
              }
            }
          }
        }
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to read the directory resources at " + baseUrl, e);
    }
  }

  private static <T> void readFromJar(
      Map<String, JsonClasspathResource<T>> sink,
      String basePath,
      URL jarUrl,
      Deserializer<T> deserializer) {

    try {
      JarURLConnection conn = (JarURLConnection) jarUrl.openConnection();
      try (JarFile jar = conn.getJarFile()) {
        // Patterns:
        // - Top-level: basePath/<file>.json                     => directoryName=""
        // - One-level: basePath/<directory>/<file>.json
        Pattern topLevel = Pattern.compile(Pattern.quote(basePath) + "/([^/]+\\.json)");
        Pattern oneLevel = Pattern.compile(Pattern.quote(basePath) + "/([^/]+)/([^/]+\\.json)");

        Enumeration<JarEntry> entries = jar.entries();
        while (entries.hasMoreElements()) {
          JarEntry e = entries.nextElement();
          if (e.isDirectory()) continue;
          String name = e.getName();

          Matcher mTop = topLevel.matcher(name);
          Matcher mOne = oneLevel.matcher(name);

          String directoryName;
          String fileName;

          if (mTop.matches()) {
            directoryName = ""; // top-level under basePath
            fileName = mTop.group(1);
          } else if (mOne.matches()) {
            directoryName = mOne.group(1); // immediate subdirectory
            fileName = mOne.group(2);
          } else {
            continue; // not a target path
          }

          String key = key(directoryName, fileName);
          if (sink.containsKey(key)) continue;

          try (InputStream in = jar.getInputStream(e)) {
            String json = readAllUtf8(in);
            String sha1Hex = sha1Hex(json);
            String fullPath = name; // already classpath-relative inside the JAR
            T value = deserializeSafe(deserializer, json, basePath, directoryName, fileName);
            sink.put(
                key,
                new JsonClasspathResource<>(
                    fileName, directoryName, fullPath, json, sha1Hex, value));
          }
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("Failed to read the JAR resources at " + jarUrl, e);
    }
  }

  private static String sha1Hex(String json) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      byte[] digest = md.digest(json.getBytes(StandardCharsets.UTF_8));
      char[] HEX = "0123456789abcdef".toCharArray();
      char[] out = new char[digest.length * 2];
      for (int i = 0, j = 0; i < digest.length; i++) {
        int b = digest[i] & 0xFF;
        out[j++] = HEX[b >>> 4];
        out[j++] = HEX[b & 0x0F];
      }
      return new String(out);
    } catch (NoSuchAlgorithmException e) {
      // Should never happen on a standard JVM
      throw new RuntimeException("SHA-1 algorithm not available", e);
    }
  }

  private interface Deserializer<T> {
    T fromJson(String json) throws IOException;
  }
}
