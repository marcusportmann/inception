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

package digital.inception.security.converter;

import digital.inception.security.model.TokenSortBy;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * The <b>StringToTokenSortByConverter</b> class implements the Spring converter that converts a
 * <b>String</b> type into a <b>TokenSortBy</b> type.
 *
 * @author Marcus Portmann
 */
@Component
public class StringToTokenSortByConverter implements Converter<String, TokenSortBy> {

  /** Constructs a new <b>StringToTokenSortByConverter</b>. */
  public StringToTokenSortByConverter() {}

  @Override
  public TokenSortBy convert(String source) {
    return TokenSortBy.fromCode(source);
  }
}
