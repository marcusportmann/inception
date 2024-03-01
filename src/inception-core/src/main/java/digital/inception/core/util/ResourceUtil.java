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

package digital.inception.core.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import org.springframework.core.io.Resource;

/**
 * The <b>ResourceUtil</b> class is a utility class which provides methods for working with
 * resources.
 *
 * @author Marcus Portmann
 */
public final class ResourceUtil {

  /** Private constructor to prevent instantiation. */
  private ResourceUtil() {}

  /**
   * Confirms whether the resource with the specified path exists on the classpath using the context
   * class loader.
   *
   * @param path the classpath path for the resource
   * @return <b>true</b> if the resource with the specified path exists on the classpath or
   *     <b>false</b> otherwise.
   */
  public static boolean classpathResourceExists(String path) {
    try {
      return Thread.currentThread().getContextClassLoader().getResource(path) != null;
    } catch (Throwable e) {
      return false;
    }
  }

  /**
   * Retrieves the resource with the specified path on the classpath using the context class loader.
   *
   * @param path the path to the resource on the classpath
   * @return the resource with the specified path on the classpath using the context class loader
   */
  public static byte[] getClasspathResource(String path) {
    try {
      try (InputStream is =
          Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
        if (is == null) {
          throw new ResourceException(
              "Failed to read the classpath resource ("
                  + path
                  + "): The resource could not be found");
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];
        int numberOfBytesRead;

        while ((numberOfBytesRead = is.read(buffer)) != -1) {
          baos.write(buffer, 0, numberOfBytesRead);
        }

        return baos.toByteArray();
      }
    } catch (ResourceException e) {
      throw e;
    } catch (Throwable e) {
      throw new ResourceException("Failed to read the classpath resource (" + path + ")", e);
    }
  }

  /**
   * Retrieves the URL for the resource with the specified path on the classpath using the context
   * class loader.
   *
   * @param path the path to the resource on the classpath
   * @return the URL for the resource with the specified path on the classpath using the context
   *     class loader
   */
  public static URL getClasspathResourceURL(String path) {
    try {
      return Thread.currentThread().getContextClassLoader().getResource(path);

    } catch (Throwable e) {
      throw new ResourceException(
          "Failed to retrieve the URL for the classpath resource (" + path + ")", e);
    }
  }

  /**
   * Retrieves the RSA private key resource.
   *
   * @param resource the resource
   * @return the RSA private key
   */
  public static RSAPrivateKey getRSAPrivateKeyResource(Resource resource) {
    if (!resource.exists()) {
      throw new ResourceException(
          "The RSA private key resource (" + resource.getFilename() + ") does not exist");
    }

    try {
      String keyData = resource.getContentAsString(Charset.defaultCharset());

      StringBuilder pemData = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new StringReader(keyData))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains("-----BEGIN PRIVATE KEY-----")
              || line.contains("-----END PRIVATE KEY-----")) {
            continue;
          }
          pemData.append(line);
        }
      }

      byte[] encodedKey = Base64.getDecoder().decode(pemData.toString());

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (Throwable e) {
      throw new ResourceException(
          "Failed to retrieve the RSA private key resource (" + resource.getFilename() + ")", e);
    }
  }

  /**
   * Retrieves the RSA public key resource.
   *
   * @param resource the resource
   * @return the RSA public key
   */
  public static RSAPublicKey getRSAPublicKeyResource(Resource resource) {
    if (!resource.exists()) {
      throw new ResourceException(
          "The RSA public key resource (" + resource.getFilename() + ") does not exist");
    }

    try {
      String keyData = resource.getContentAsString(Charset.defaultCharset());

      StringBuilder pemData = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new StringReader(keyData))) {
        String line;
        while ((line = reader.readLine()) != null) {
          if (line.contains("-----BEGIN PUBLIC KEY-----")
              || line.contains("-----END PUBLIC KEY-----")) {
            continue;
          }
          pemData.append(line);
        }
      }

      byte[] encodedKey = Base64.getDecoder().decode(pemData.toString());

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (Throwable e) {
      throw new ResourceException(
          "Failed to retrieve the RSA public key resource (" + resource.getFilename() + ")", e);
    }
  }

  /**
   * Retrieves the resource with the specified path on the classpath using the context class loader.
   *
   * @param path the path to the resource on the classpath
   * @return the resource with the specified path on the classpath using the context class loader
   */
  public static String getStringClasspathResource(String path) {
    byte[] data = getClasspathResource(path);

    return new String(data, StandardCharsets.UTF_8);
  }
}
