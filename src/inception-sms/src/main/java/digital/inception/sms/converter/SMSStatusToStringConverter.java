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

package digital.inception.sms.converter;

import digital.inception.sms.model.SMSStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

/**
 * The <b>SMSStatusToStringConverter</b> class implements the Spring converter that converts a
 * <b>SMSStatus</b> type into a <b>String</b> type.
 *
 * @author Marcus Portmann
 */
@Component
@WritingConverter
public class SMSStatusToStringConverter implements Converter<SMSStatus, String> {

  /** Constructs a new <b>SMSStatusToStringConverter</b>. */
  public SMSStatusToStringConverter() {}

  @Override
  public String convert(SMSStatus source) {
    return source.code();
  }
}
