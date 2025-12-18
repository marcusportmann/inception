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

package digital.inception.core.exception;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The <b>ThirdPartyServiceUnavailableException</b> exception is thrown to indicate an error
 * condition as a result of a third-party service not being available.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/third-party-service-unavailable",
    title =
        "A third-party service is not available and your request could not be processed at this time.",
    status = 503)
@WebFault(
    name = "ThirdPartyServiceUnavailableException",
    targetNamespace = "https://inception.digital/core",
    faultBean = "digital.inception.core.exception.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
@SuppressWarnings("unused")
public class ThirdPartyServiceUnavailableException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /** The reference to the original request. */
  private final String requestReference;

  /** The code identifying the third-party service that is unavailable. */
  private final String thirdPartyServiceCode;

  /**
   * Constructs a new <b>ThirdPartyServiceUnavailableException</b>.
   *
   * @param thirdPartyServiceCode the code identifying the third-party service that is unavailable
   * @param requestReference a reference to the original request
   */
  public ThirdPartyServiceUnavailableException(
      String thirdPartyServiceCode, String requestReference) {
    super(
        "Failed to invoke the third-party service ("
            + thirdPartyServiceCode
            + ") with the request reference ("
            + requestReference
            + ")");

    this.thirdPartyServiceCode = thirdPartyServiceCode;
    this.requestReference = requestReference;
  }

  /**
   * Returns the reference to the original request.
   *
   * @return the reference to the original request
   */
  public String getRequestReference() {
    return requestReference;
  }

  /**
   * Returns the code identifying the third-party service that is unavailable.
   *
   * @return the code identifying the third-party service that is unavailable
   */
  public String getThirdPartyServiceCode() {
    return thirdPartyServiceCode;
  }
}
