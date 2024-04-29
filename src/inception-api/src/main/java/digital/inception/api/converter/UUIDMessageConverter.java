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

package digital.inception.api.converter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

/**
 * The <b>UUIDMessageConverter</b> class.
 *
 * @author Marcus Portmann
 */
@Component
public class UUIDMessageConverter extends AbstractHttpMessageConverter<UUID> {

  /** Constructs a new <b>UUIDMessageConverter</b>. */
  public UUIDMessageConverter() {
    super(MediaType.TEXT_PLAIN);
  }

  @Override
  @NonNull
  protected UUID readInternal(
      @NonNull Class<? extends UUID> clazz, @NonNull HttpInputMessage inputMessage)
      throws IOException, HttpMessageNotReadableException {
    return UUID.fromString(
        StreamUtils.copyToString(inputMessage.getBody(), StandardCharsets.UTF_8));
  }

  @Override
  protected boolean supports(@NonNull Class<?> clazz) {
    return UUID.class == clazz;
  }

  @Override
  protected void writeInternal(UUID uuid, HttpOutputMessage outputMessage)
      throws IOException, HttpMessageNotWritableException {
    try (OutputStreamWriter writer =
        new OutputStreamWriter(outputMessage.getBody(), StandardCharsets.UTF_8)) {
      writer.write(uuid.toString());
    }
  }
}
