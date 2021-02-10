/*
 * Copyright 2021 Marcus Portmann
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

@javax.xml.bind.annotation.XmlSchema(
    namespace = "http://inception.digital/banking/account",
    elementFormDefault = javax.xml.bind.annotation.XmlNsForm.UNQUALIFIED,
    xmlns = {
      @javax.xml.bind.annotation.XmlNs(
          prefix = "core",
          namespaceURI = "http://inception.digital/core"),
      @javax.xml.bind.annotation.XmlNs(
          prefix = "party",
          namespaceURI = "http://inception.digital/party"),
      @javax.xml.bind.annotation.XmlNs(
          prefix = "customer",
          namespaceURI = "http://inception.digital/banking/customer"),
      @javax.xml.bind.annotation.XmlNs(
          prefix = "account",
          namespaceURI = "http://inception.digital/banking/account")
    })
package digital.inception.banking.account;