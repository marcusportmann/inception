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

package digital.inception.process;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>IAuditService</code> interface defines the functionality provided by a Process
 * Service implementation.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public interface IProcessService
{
  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the IDs for the BPMN processes if the BPMN XML data was successfully validated
   */
  List<String> validateBPMN(byte[] bpmnXml)
    throws InvalidBPMNException, ProcessServiceException;

  /**
   * Validate the BPMN XML data.
   *
   * @param bpmnXml the BPMN XML data
   *
   * @return the IDs for the BPMN processes if the BPMN XML data was successfully validated
   */
  List<String> validateBPMN(String bpmnXml)
    throws InvalidBPMNException, ProcessServiceException;
}
