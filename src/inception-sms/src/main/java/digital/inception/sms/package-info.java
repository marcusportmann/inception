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

/**
 * Provides the classes for the SMS module.
 *
 * @author Marcus Portmann
 */
@jakarta.xml.bind.annotation.XmlSchema(
    namespace = "http://inception.digital/sms",
    elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
    xmlns = {
      @jakarta.xml.bind.annotation.XmlNs(
          prefix = "core",
          namespaceURI = "http://inception.digital/core"),
      @jakarta.xml.bind.annotation.XmlNs(
          prefix = "sms",
          namespaceURI = "http://inception.digital/sms"),
    })
package digital.inception.sms;
