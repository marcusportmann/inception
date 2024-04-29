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
 * Provides the classes for the Mail module.
 *
 * @author Marcus Portmann
 */
@jakarta.xml.bind.annotation.XmlSchema(
    namespace = "https://inception.digital/mail",
    elementFormDefault = jakarta.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
    xmlns = {
      @jakarta.xml.bind.annotation.XmlNs(
          prefix = "core",
          namespaceURI = "https://inception.digital/core"),
      @jakarta.xml.bind.annotation.XmlNs(
          prefix = "mail",
          namespaceURI = "https://inception.digital/mail"),
    })
package digital.inception.mail.ws;
