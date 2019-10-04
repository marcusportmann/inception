/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.messaging;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.stereotype.Component;

/**
 * The <code>IntegerToMessageStatusConverter</code> class implements the Spring converter that
 * converts an <code>Integer</code> type into a <code>MessageStatus</code> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
@ReadingConverter
public class IntegerToMessageStatusConverter
  implements Converter<Integer, MessageStatus>
{
  /**
   * Constructs a new <code>IntegerToMessageStatusConverter</code>.
   */
  public IntegerToMessageStatusConverter() {}

  @Override
  public MessageStatus convert(Integer source)
  {
    if (source == null)
    {
      return null;
    }

    return MessageStatus.fromCode(source);
  }
}