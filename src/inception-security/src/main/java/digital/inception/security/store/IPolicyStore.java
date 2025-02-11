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

package digital.inception.security.store;

import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.core.sorting.SortDirection;
import digital.inception.security.model.DuplicatePolicyException;
import digital.inception.security.model.Policy;
import digital.inception.security.model.PolicyNotFoundException;
import digital.inception.security.model.PolicySortBy;
import digital.inception.security.model.PolicySummaries;
import java.util.List;

/**
 * The <b>IPolicyDataStore</b> interface defines the functionality provided by a policy store, which
 * manages the storage of structured and unstructured information for polices.
 *
 * @author Marcus Portmann
 */
public interface IPolicyStore {

  /**
   * Create the new policy.
   *
   * @param policy the policy
   * @return the policy
   * @throws DuplicatePolicyException if the policy already exists
   * @throws ServiceUnavailableException if the policy could not be created
   */
  Policy createPolicy(Policy policy) throws DuplicatePolicyException, ServiceUnavailableException;

  /**
   * Delete the policy.
   *
   * @param policyId the ID for the policy
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be deleted
   */
  void deletePolicy(String policyId) throws PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the policies.
   *
   * @return the policies
   * @throws ServiceUnavailableException if the policies could not be retrieved
   */
  List<Policy> getPolicies() throws ServiceUnavailableException;

  /**
   * Retrieve the policy.
   *
   * @param policyId the ID for the policy
   * @return the policy
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be retrieved
   */
  Policy getPolicy(String policyId) throws PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the name of the policy.
   *
   * @param policyId the ID for the policy
   * @return the name of the policy
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the name of the policy could not be retrieved
   */
  String getPolicyName(String policyId) throws PolicyNotFoundException, ServiceUnavailableException;

  /**
   * Retrieve the summaries for the policies.
   *
   * @param filter the filter to apply to the policy summaries
   * @param sortBy the method used to sort the policy summaries e.g. by name
   * @param sortDirection the sort direction to apply to the policy summaries
   * @param pageIndex the page index
   * @param pageSize the page size
   * @return the summaries for the policies
   * @throws ServiceUnavailableException if the policy summaries could not be retrieved
   */
  PolicySummaries getPolicySummaries(
      String filter,
      PolicySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException;

  /**
   * Update the policy.
   *
   * @param policy the policy
   * @return the policy
   * @throws PolicyNotFoundException if the policy could not be found
   * @throws ServiceUnavailableException if the policy could not be updated
   */
  Policy updatePolicy(Policy policy) throws PolicyNotFoundException, ServiceUnavailableException;
}
