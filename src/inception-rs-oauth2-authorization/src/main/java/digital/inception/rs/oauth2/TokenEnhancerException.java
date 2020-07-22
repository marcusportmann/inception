/// *
// * Copyright 2020 Marcus Portmann
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *   http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
// package digital.inception.rs.oauth;
//
//// ~--- non-JDK imports --------------------------------------------------------
//
// import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
//
/// **
// * The <code>ApplicationException</code> exception is thrown to indicate an application error
// * condition.
// *
// * @author Marcus Portmann
// */
// public class TokenEnhancerException extends OAuth2Exception {
//
//  private static final long serialVersionUID = 1000000;
//
//  /**
//   * Constructs a new <code>TokenEnhancerException</code> with the specified message and cause.
//   *
//   * @param message the message saved for later retrieval by the <code>getMessage()</code> method
//   * @param cause The cause saved for later retrieval by the <code>getCause()</code> method. (A
//   *     <code>null</code> value is permitted if the cause is nonexistent or unknown)
//   */
//  public TokenEnhancerException(String message, Throwable cause) {
//    super(message, cause);
//  }
//
//  @Override
//  public int getHttpErrorCode() {
//    return 500;
//  }
//
//  @Override
//  public String getOAuth2ErrorCode() {
//    return "server_error";
//  }
// }
