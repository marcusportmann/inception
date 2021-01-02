/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.party;

// ~--- JDK imports ------------------------------------------------------------

import digital.inception.core.sorting.SortDirection;
import digital.inception.core.validation.InvalidArgumentException;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlElement;

/**
 * The <code>PartyWebService</code> class.
 *
 * @author Marcus Portmann
 */
@WebService(
    serviceName = "PartyService",
    name = "IPartyService",
    targetNamespace = "http://party.inception.digital")
@SOAPBinding
@SuppressWarnings({"unused", "ValidExternallyBoundObject"})
public class PartyWebService {

  /** The Party Service. */
  private final IPartyService partyService;

  /**
   * Constructs a new <code>PartyWebService</code>.
   *
   * @param partyService the Party Service
   */
  public PartyWebService(IPartyService partyService) {
    this.partyService = partyService;
  }

  /**
   * Retrieve the organizations.
   *
   * @param filter the optional filter to apply to the organizations
   * @param sortDirection the optional sort direction to apply to the organizations
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the organizations
   */
  @WebMethod(operationName = "GetOrganizations")
  public Organizations getOrganizations(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyServiceException {
    return partyService.getOrganizations(filter, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the parties.
   *
   * @param filter the optional filter to apply to the parties
   * @param sortDirection the optional sort direction to apply to the parties
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the parties
   */
  @WebMethod(operationName = "GetParties")
  public Parties getParties(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyServiceException {
    return partyService.getParties(filter, sortDirection, pageIndex, pageSize);
  }

  /**
   * Retrieve the persons.
   *
   * @param filter the optional filter to apply to the persons
   * @param sortDirection the optional sort direction to apply to the persons
   * @param pageIndex the optional page index
   * @param pageSize the optional page size
   * @return the persons
   */
  @WebMethod(operationName = "GetPersons")
  public Persons getPersons(
      @WebParam(name = "Filter") @XmlElement String filter,
      @WebParam(name = "SortBy") @XmlElement PersonSortBy sortBy,
      @WebParam(name = "SortDirection") @XmlElement SortDirection sortDirection,
      @WebParam(name = "PageIndex") @XmlElement Integer pageIndex,
      @WebParam(name = "PageSize") @XmlElement Integer pageSize)
      throws InvalidArgumentException, PartyServiceException {
    return partyService.getPersons(filter, sortBy, sortDirection, pageIndex, pageSize);
  }
}
