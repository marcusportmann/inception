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

package digital.inception.core.service;

/**
 * The <code>ServiceException</code> exception is the base class that all service exceptions that
 * will be returned as a web service faults should be derived from.
 *
 * @author Marcus Portmann
 */
@SuppressWarnings("unused")
public abstract class ServiceException extends Exception {

  private static final long serialVersionUID = 1000000;

  /**
   * The service error information.
   */
  private ServiceError serviceError;

  /**
   * Constructs a new <code>ServiceException</code> with the specified message.
   *
   * @param code    The code identifying the error.
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   */
  public ServiceException(String code, String message) {
    super(message);

    this.serviceError = new ServiceError(code, this);
  }

  /**
   * Constructs a new <code>ServiceException</code> with the specified cause and a message of
   * <code>(cause==null ? null : cause.toString())</code> (which typically contains the class and
   * message of cause).
   *
   * @param code  The code identifying the service error.
   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *              <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ServiceException(String code, Throwable cause) {
    super(cause);

    this.serviceError = new ServiceError(code, this);
  }

  /**
   * Constructs a new <code>ServiceException</code> with the specified message and cause.
   *
   * @param code    The code identifying the service error.
   * @param message The message saved for later retrieval by the <code>getMessage()</code> method.
   * @param cause   The cause saved for later retrieval by the <code>getCause()</code> method. (A
   *                <code>null</code> value is permitted if the cause is nonexistent or unknown)
   */
  public ServiceException(String code, String message, Throwable cause) {
    super(message, cause);

    this.serviceError = new ServiceError(code, this);
  }

  /**
   * Returns the fault info.
   *
   * @return the fault info
   */
  public ServiceError getFaultInfo() {
    return serviceError;
  }

  /**
   * Returns the service error info.
   *
   * @return the service error info
   */
  public ServiceError getServiceError() {
    return serviceError;
  }
}
