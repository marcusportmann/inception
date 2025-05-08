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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import digital.inception.api.converter.EnumMessageConverter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;

/** The {@code EnumMessageConverterTests} class. */
public class EnumMessageConverterTests {

  /** Test the conversion of an enum implementing the CodeEnum interface. */
  @Test
  public void codeEnumTest() throws Exception {
    TestEnum testEnum = TestEnum.OPTION2;

    EnumMessageConverter enumMessageConverter = new EnumMessageConverter();

    assertTrue(enumMessageConverter.canRead(testEnum.getClass(), MediaType.TEXT_PLAIN));

    Enum<?> readEnum =
        enumMessageConverter.read(
            testEnum.getClass(),
            new HttpInputMessage() {
              @Override
              @NonNull
              public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("option2".getBytes(StandardCharsets.UTF_8));
              }

              @Override
              @NonNull
              public HttpHeaders getHeaders() {
                return new HttpHeaders();
              }
            });

    assertEquals(TestEnum.OPTION2, readEnum);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    enumMessageConverter.write(
        TestEnum.OPTION2,
        MediaType.TEXT_PLAIN,
        new HttpOutputMessage() {
          @Override
          @NonNull
          public OutputStream getBody() throws IOException {
            return baos;
          }

          @Override
          @NonNull
          public HttpHeaders getHeaders() {
            return new HttpHeaders();
          }
        });

    String enumValue = baos.toString(StandardCharsets.UTF_8);

    assertEquals(TestEnum.OPTION2.code(), enumValue);
  }

  /** Test the conversion of a normal enum. */
  @Test
  public void normalEnumTest() throws Exception {
    AnotherTestEnum anotherTestEnum = AnotherTestEnum.OPTION2;

    EnumMessageConverter enumMessageConverter = new EnumMessageConverter();

    assertTrue(enumMessageConverter.canRead(anotherTestEnum.getClass(), MediaType.TEXT_PLAIN));

    Enum<?> readEnum =
        enumMessageConverter.read(
            anotherTestEnum.getClass(),
            new HttpInputMessage() {
              @Override
              @NonNull
              public InputStream getBody() throws IOException {
                return new ByteArrayInputStream("OPTION2".getBytes(StandardCharsets.UTF_8));
              }

              @Override
              @NonNull
              public HttpHeaders getHeaders() {
                return new HttpHeaders();
              }
            });

    assertEquals(AnotherTestEnum.OPTION2, readEnum);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    enumMessageConverter.write(
        AnotherTestEnum.OPTION2,
        MediaType.TEXT_PLAIN,
        new HttpOutputMessage() {
          @Override
          @NonNull
          public OutputStream getBody() throws IOException {
            return baos;
          }

          @Override
          @NonNull
          public HttpHeaders getHeaders() {
            return new HttpHeaders();
          }
        });

    String enumValue = baos.toString(StandardCharsets.UTF_8);

    assertEquals(AnotherTestEnum.OPTION2.name(), enumValue);
  }
}
