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
// package digital.inception.rs.oauth;
//
//// ~--- non-JDK imports --------------------------------------------------------
//
// import com.nimbusds.jose.jwk.RSAKey;
// import org.springframework.core.convert.converter.Converter;
// import org.springframework.stereotype.Component;
// import org.springframework.util.StringUtils;
//
//// ~--- JDK imports ------------------------------------------------------------
//
/// **
// * The <code>StringToRSAKeyConverter</code> class implements the Spring converter that converts a
// * <code>String</code> type into a <code>RSAKey</code> type.
// *
// * @author Marcus Portmann
// */
// @Component
// @SuppressWarnings("unused")
// public final class StringToRSAKeyConverter implements Converter<String, RSAKey> {
//
//  /** Constructs a new <code>StringToRSAKeyConverter</code>. */
//  public StringToRSAKeyConverter() {}
//
//  @Override
//  public RSAKey convert(String source) {
//    if (StringUtils.isEmpty(source)) {
//      return null;
//    }
//
//    try {
//      return RSAKey.parse(source);
//    } catch (Throwable e) {
//      throw new RuntimeException("Failed to parse the RSAKey value (" + source + ")", e);
//    }
//  }
// }
