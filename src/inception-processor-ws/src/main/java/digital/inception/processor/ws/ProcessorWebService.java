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

package digital.inception.processor.ws;

import digital.inception.ws.AbstractWebServiceBase;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;
import org.springframework.context.ApplicationContext;

/**
 * The {@code ProcessorWebService} class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "ProcessorService",
    name = "IProcessorService",
    targetNamespace = "https://inception.digital/processor")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class ProcessorWebService extends AbstractWebServiceBase {

  /**
   * Constructs a new {@code ProcessorWebService}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   */
  public ProcessorWebService(ApplicationContext applicationContext) {
    super(applicationContext);
  }
}
