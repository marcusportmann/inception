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

package digital.inception.security.persistence;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import digital.inception.json.InceptionModule;
import digital.inception.security.model.TokenClaim;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.StringUtils;

/**
 * The <b>TokenClaimListConverter</b> class implements the custom JPA converter for a list of
 * <b>TokenClaim</b> objects.
 *
 * @author Marcus Portmann
 */
@Converter
public class TokenClaimListAttributeConverter
    implements AttributeConverter<List<TokenClaim>, String> {

  private static final ObjectMapper objectMapper;

  static {
    objectMapper = new ObjectMapper();
    objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    objectMapper.registerModule(new InceptionModule());
  }

  /** Constructs a new <b>TokenClaimListConverter</b>. */
  public TokenClaimListAttributeConverter() {}

  /**
   * Converts the value stored in the entity attribute into the data representation to be stored in
   * the database.
   *
   * @param attribute the entity attribute value to be converted
   * @return the converted data to be stored in the database column
   */
  @Override
  public String convertToDatabaseColumn(List<TokenClaim> attribute) {
    if (attribute == null) {
      return null;
    }

    try {
      return objectMapper.writeValueAsString(attribute);
    } catch (Throwable e) {
      throw new RuntimeException("Failed to serialize the token claims", e);
    }
  }

  /**
   * Converts the data stored in the database column into the value to be stored in the entity
   * attribute. Note that it is the responsibility of the converter writer to specify the correct
   * dbData type for the corresponding column for use by the JDBC driver: i.e., persistence
   * providers are not expected to do such type conversion.
   *
   * @param dbData the data from the database column to be converted
   * @return the converted value to be stored in the entity attribute
   */
  @Override
  public List<TokenClaim> convertToEntityAttribute(String dbData) {
    if (dbData == null) {
      return null;
    }

    if (StringUtils.hasText(dbData)) {
      try {
        return objectMapper.convertValue(
            objectMapper.readValue(dbData, List.class), new TypeReference<>() {});
      } catch (Throwable e) {
        throw new RuntimeException("Failed to deserialize the token claims JSON", e);
      }
    } else {
      return new ArrayList<>();
    }
  }
}
