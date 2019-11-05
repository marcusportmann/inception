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

package digital.inception.scheduler;

//~--- non-JDK imports --------------------------------------------------------

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.stereotype.Component;

/**
 * The <code>JobStatusToIntegerConverter</code> class implements the Spring converter that
 * converts a <code>JobStatus</code> type into an <code>Integer</code> type.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
@Component
@WritingConverter
public class JobStatusToIntegerConverter
  implements Converter<JobStatus, Integer>
{
  /**
   * Constructs a new <code>JobStatusToIntegerConverter</code>.
   */
  public JobStatusToIntegerConverter() {}

  @Override
  public Integer convert(JobStatus source)
  {
    if (source == null)
    {
      return null;
    }

    return source.code();
  }
}
