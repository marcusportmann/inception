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

package digital.inception.core.wbxml;

import java.io.OutputStream;

/**
 * The {@code Content} interface is implemented by all of the WBXML content types e.g. <b>CDATA</b>,
 * <b>Element</b>, <b>Opaque</b>, etc.
 *
 * @author Marcus Portmann
 */
public interface Content {

  /**
   * Print the content using the specified indent level.
   *
   * @param indent the indent level
   */
  void print(int indent);

  /**
   * Print the content to the specified <b>OutputStream</b> using the specified indent level.
   *
   * @param out the <b>OuputStream</b> to output the content to
   * @param indent the indent level
   */
  void print(OutputStream out, int indent);
}
