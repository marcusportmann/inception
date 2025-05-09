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
import digital.inception.security.model.PolicySummary;
import digital.inception.security.persistence.jpa.PolicyRepository;
import digital.inception.security.persistence.jpa.PolicySummaryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.context.annotation.Conditional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * The {@code InternalPolicyStore} class provides the internal policy store implementation.
 *
 * @author Marcus Portmann
 */
@Service
@Conditional(InternalPolicyStoreEnabledCondition.class)
public class InternalPolicyStore implements PolicyStore {

  /** The default maximum number of filtered policies. */
  private static final int DEFAULT_MAX_FILTERED_POLICIES = 100;

  /** The Policy Repository. */
  private final PolicyRepository policyRepository;

  /** The Policy Summary Repository. */
  private final PolicySummaryRepository policySummaryRepository;

  /**
   * * Constructs a new {@code InternalPolicyDataStore}.
   *
   * @param policyRepository the Policy Repository
   * @param policySummaryRepository the Policy Summary Repository
   */
  public InternalPolicyStore(
      PolicyRepository policyRepository, PolicySummaryRepository policySummaryRepository) {
    this.policyRepository = policyRepository;
    this.policySummaryRepository = policySummaryRepository;
  }

  @Override
  public Policy createPolicy(Policy policy)
      throws DuplicatePolicyException, ServiceUnavailableException {
    try {
      if (policyRepository.existsById(policy.getId())) {
        throw new DuplicatePolicyException(policy.getId());
      }

      return policyRepository.saveAndFlush(policy);
    } catch (DuplicatePolicyException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to create the policy (" + policy.getId() + ")", e);
    }
  }

  @Override
  public void deletePolicy(String policyId)
      throws PolicyNotFoundException, ServiceUnavailableException {
    try {
      if (!policyRepository.existsById(policyId)) {
        throw new PolicyNotFoundException(policyId);
      }

      policyRepository.deleteById(policyId);
    } catch (PolicyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to delete the policy (" + policyId + ")", e);
    }
  }

  @Override
  public List<Policy> getPolicies() throws ServiceUnavailableException {
    try {
      return policyRepository.findAllByOrderByNameAsc();
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the policies", e);
    }
  }

  @Override
  public Policy getPolicy(String policyId)
      throws PolicyNotFoundException, ServiceUnavailableException {
    try {
      Optional<Policy> policyOptional = policyRepository.findById(policyId);

      if (policyOptional.isPresent()) {
        return policyOptional.get();
      } else {
        throw new PolicyNotFoundException(policyId);
      }
    } catch (PolicyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the policy (" + policyId + ")", e);
    }
  }

  @Override
  public String getPolicyName(String policyId)
      throws PolicyNotFoundException, ServiceUnavailableException {
    try {
      Optional<String> nameOptional = policyRepository.getNameById(policyId);

      if (nameOptional.isPresent()) {
        return nameOptional.get();
      } else {
        throw new PolicyNotFoundException(policyId);
      }
    } catch (PolicyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to retrieve the name of the policy (" + policyId + ")", e);
    }
  }

  @Override
  public PolicySummaries getPolicySummaries(
      String filter,
      PolicySortBy sortBy,
      SortDirection sortDirection,
      Integer pageIndex,
      Integer pageSize)
      throws ServiceUnavailableException {
    try {
      PageRequest pageRequest;

      if (pageIndex == null) {
        pageIndex = 0;
      }

      if (pageSize == null) {
        pageSize = DEFAULT_MAX_FILTERED_POLICIES;
      }

      String sortProperty;
      if (sortBy == PolicySortBy.TYPE) {
        sortProperty = "type";
      } else {
        sortProperty = "name";
      }

      pageRequest =
          PageRequest.of(
              pageIndex,
              Math.min(pageSize, DEFAULT_MAX_FILTERED_POLICIES),
              (sortDirection == SortDirection.ASCENDING) ? Sort.Direction.ASC : Sort.Direction.DESC,
              sortProperty);

      Page<PolicySummary> policySummaryPage;
      if (StringUtils.hasText(filter)) {
        policySummaryPage =
            policySummaryRepository.findAll(
                (Specification<PolicySummary>)
                    (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + filter.toLowerCase() + "%"),
                pageRequest);
      } else {
        policySummaryPage = policySummaryRepository.findAll(pageRequest);
      }

      return new PolicySummaries(
          policySummaryPage.toList(),
          policySummaryPage.getTotalElements(),
          filter,
          sortBy,
          sortDirection,
          pageIndex,
          pageSize);
    } catch (Throwable e) {
      throw new ServiceUnavailableException("Failed to retrieve the filtered policy summaries", e);
    }
  }

  @Override
  public Policy updatePolicy(Policy policy)
      throws PolicyNotFoundException, ServiceUnavailableException {
    try {
      Optional<Policy> policyOptional = policyRepository.findById(policy.getId());

      if (policyOptional.isPresent()) {
        Policy existingPolicy = policyOptional.get();

        existingPolicy.setVersion(policy.getVersion());
        existingPolicy.setName(policy.getName());
        existingPolicy.setType(policy.getType());
        existingPolicy.setData(policy.getData());

        return policyRepository.saveAndFlush(existingPolicy);
      } else {
        throw new PolicyNotFoundException(policy.getId());
      }
    } catch (PolicyNotFoundException e) {
      throw e;
    } catch (Throwable e) {
      throw new ServiceUnavailableException(
          "Failed to update the policy (" + policy.getId() + ")", e);
    }
  }
}
