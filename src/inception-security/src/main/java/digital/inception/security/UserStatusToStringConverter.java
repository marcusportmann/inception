/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.security;

// ~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

/**
 * The <code>UserStatusToStringConverter</code> class implements the Spring converter that converts
 * a <code>UserStatus</code> type into a <code>String</code> type.
 *
 * @author Marcus Portmann
 */
@Component
@WritingConverter
public class UserStatusToStringConverter implements Converter<UserStatus, String> {

  /** Constructs a new <code>UserStatusToStringConverter</code>. */
  public UserStatusToStringConverter() {}

  @Override
  public String convert(UserStatus source) {
    return source.code();
  }
}
