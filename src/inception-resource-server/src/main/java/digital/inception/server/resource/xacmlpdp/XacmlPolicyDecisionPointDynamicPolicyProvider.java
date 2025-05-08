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

package digital.inception.server.resource.xacmlpdp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import digital.inception.server.resource.PolicyDecisionPointException;
import jakarta.xml.bind.JAXBElement;
import jakarta.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringReader;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import javax.xml.namespace.QName;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.AdviceExpressions;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.DefaultsType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.IdReferenceType;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.ObligationExpressions;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Policy;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicyIssuer;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.PolicySet;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Target;
import org.ow2.authzforce.core.pdp.api.EnvironmentProperties;
import org.ow2.authzforce.core.pdp.api.EvaluationContext;
import org.ow2.authzforce.core.pdp.api.IndeterminateEvaluationException;
import org.ow2.authzforce.core.pdp.api.XmlUtils.XmlnsFilteringParser;
import org.ow2.authzforce.core.pdp.api.XmlUtils.XmlnsFilteringParserFactory;
import org.ow2.authzforce.core.pdp.api.combining.CombiningAlgRegistry;
import org.ow2.authzforce.core.pdp.api.expression.ExpressionFactory;
import org.ow2.authzforce.core.pdp.api.policy.BasePrimaryPolicyMetadata;
import org.ow2.authzforce.core.pdp.api.policy.CloseablePolicyProvider;
import org.ow2.authzforce.core.pdp.api.policy.PolicyProvider;
import org.ow2.authzforce.core.pdp.api.policy.PolicyVersion;
import org.ow2.authzforce.core.pdp.api.policy.PolicyVersionPatterns;
import org.ow2.authzforce.core.pdp.api.policy.PrimaryPolicyMetadata;
import org.ow2.authzforce.core.pdp.api.policy.TopLevelPolicyElementEvaluator;
import org.ow2.authzforce.core.pdp.api.policy.TopLevelPolicyElementType;
import org.ow2.authzforce.core.pdp.impl.policy.PolicyEvaluators;
import org.ow2.authzforce.core.pdp.impl.policy.PolicyVersions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

/**
 * The {@code XacmlPolicyDecisionPointDynamicPolicyProvider} class provides a custom AuthzForce
 * dynamic policy provider implementation, which provides the XACML-based policy sets and policies
 * that will be applied by the XACML policy decision point.
 *
 * @author Marcus Portmann
 */
public class XacmlPolicyDecisionPointDynamicPolicyProvider
    implements CloseablePolicyProvider<TopLevelPolicyElementEvaluator> {

  /* Logger */
  private static final Logger log =
      LoggerFactory.getLogger(XacmlPolicyDecisionPointDynamicPolicyProvider.class);

  /** Should policy sets and policies be loaded from the classpath under pdp/policies? */
  private final Boolean classpathPoliciesEnabled;

  /** The combining algorithm registry. */
  private final CombiningAlgRegistry combiningAlgorithmRegistry;

  /** The expression factory. */
  private final ExpressionFactory expressionFactory;

  /** Should policy sets and policies be loaded by invoking an external API. */
  private final Boolean externalPoliciesEnabled;

  /** The external API endpoint used to retrieve policy sets and policies. */
  private final String externalPoliciesEndpoint;

  /** The reload period in seconds for external policies. */
  private final int externalPoliciesReloadPeriod;

  private final Map<String, Map<PolicyVersion, ParsedPolicy>> policies = new HashMap<>();

  private final Map<String, Map<PolicyVersion, ParsedPolicySet>> policySets = new HashMap<>();

  /** The factory used to create XACML parsers. */
  private final XmlnsFilteringParserFactory xacmlParserFactory;

  /** The date and time the external policy sets and policies should be reloaded. */
  private LocalDateTime reloadExternalPoliciesWhen;

  /** The root policy set. */
  private PolicySet rootPolicySet = null;

  /**
   * Creates a new {@code XacmlPolicyDecisionPointDynamicPolicyProvider} instance.
   *
   * @param xacmlParserFactory the factory used to create XACML parsers
   * @param expressionFactory the expression factory
   * @param combiningAlgorithmRegistry the combining algorithm registry
   * @param classpathPoliciesEnabled should policy sets and policies be loaded from the classpath
   *     under pdp/policies
   * @param externalPoliciesEnabled should policy sets and policies be loaded by invoking an
   *     external API
   * @param externalPoliciesEndpoint the external API endpoint used to retrieve policy sets and
   *     policies
   * @param externalPoliciesReloadPeriod the reload period in seconds for external policies
   */
  public XacmlPolicyDecisionPointDynamicPolicyProvider(
      XmlnsFilteringParserFactory xacmlParserFactory,
      ExpressionFactory expressionFactory,
      CombiningAlgRegistry combiningAlgorithmRegistry,
      boolean classpathPoliciesEnabled,
      boolean externalPoliciesEnabled,
      String externalPoliciesEndpoint,
      int externalPoliciesReloadPeriod) {

    this.xacmlParserFactory = xacmlParserFactory;
    this.expressionFactory = expressionFactory;
    this.combiningAlgorithmRegistry = combiningAlgorithmRegistry;
    this.classpathPoliciesEnabled = classpathPoliciesEnabled;
    this.externalPoliciesEnabled = externalPoliciesEnabled;
    this.externalPoliciesEndpoint = externalPoliciesEndpoint;
    this.externalPoliciesReloadPeriod = externalPoliciesReloadPeriod;

    try {
      loadPolicies();
    } catch (Throwable e) {
      throw new PolicyDecisionPointException(
          "Failed to initialize the XacmlPolicyDecisionPointDynamicPolicyProvider", e);
    }
  }

  @Override
  public void close() throws IOException {
    // Do nothing
  }

  @Override
  public TopLevelPolicyElementEvaluator get(
      TopLevelPolicyElementType policyType,
      String policyId,
      Optional<PolicyVersionPatterns> policyVersionConstraints,
      Deque<String> policySetRefChain,
      EvaluationContext evaluationCtx,
      Optional<EvaluationContext> mdpContext)
      throws IllegalArgumentException, IndeterminateEvaluationException {
    /*
     * Attempt to reload the external policy sets and policies.
     *
     * The external policy sets and policies will only be reloaded if they were not successfully
     * loaded previously or the reload period has expired.
     */
    reloadExternalPolicies();

    if (policyType == TopLevelPolicyElementType.POLICY_SET) {
      return getPolicySet(policyId, policyVersionConstraints, policySetRefChain);
    } else {
      return getPolicy(policyId, policyVersionConstraints);
    }
  }

  @Override
  public Optional<PrimaryPolicyMetadata> getCandidateRootPolicy() {
    return Optional.of(
        new BasePrimaryPolicyMetadata(
            TopLevelPolicyElementType.POLICY_SET,
            rootPolicySet.getPolicySetId(),
            new PolicyVersion(rootPolicySet.getVersion())));
  }

  @Override
  public Deque<String> joinPolicyRefChains(
      Deque<String> policyRefChain1, List<String> policyRefChain2) throws IllegalArgumentException {
    return PolicyProvider.joinPolicyRefChains(
        policyRefChain1, policyRefChain2, PolicyProvider.UNLIMITED_POLICY_REF_DEPTH);
  }

  /** Load the policies. */
  public void loadPolicies() {
    // Load the policy sets and policies from the classpath
    if (classpathPoliciesEnabled) {
      loadClasspathPolicies();
    }

    // Attempt to load the external policy sets and policies, this will be retried if unsuccessful
    reloadExternalPolicies();

    rootPolicySet = buildRootPolicySet();
  }

  /**
   * Build the root policy set.
   *
   * @return the root policy set
   */
  private PolicySet buildRootPolicySet() {
    PolicyIssuer policyIssuer = null;
    DefaultsType defaultsType = null;
    Target target = new Target(null);
    List<Serializable> policySetsAndPoliciesAndPolicySetIdReferences = new ArrayList<>();
    ObligationExpressions obligationExpressions = null;
    AdviceExpressions adviceExpressions = null;
    String policyCombiningAlgId =
        "urn:oasis:names:tc:xacml:3.0:policy-combining-algorithm:deny-unless-permit";
    BigInteger maxDelegationDepth = null;

    for (String policySetId : policySets.keySet()) {
      IdReferenceType policySetIdReference = new IdReferenceType(policySetId, null, null, null);

      policySetsAndPoliciesAndPolicySetIdReferences.add(
          new JAXBElement<>(
              new QName("oasis:names:tc:xacml:3.0:core:schema:wd-17", "PolicySetIdReference"),
              IdReferenceType.class,
              policySetIdReference));
    }

    for (String policyId : policies.keySet()) {
      IdReferenceType policyIdReference = new IdReferenceType(policyId, null, null, null);

      policySetsAndPoliciesAndPolicySetIdReferences.add(
          new JAXBElement<>(
              new QName("oasis:names:tc:xacml:3.0:core:schema:wd-17", "PolicyIdReference"),
              IdReferenceType.class,
              policyIdReference));
    }

    return new PolicySet(
        "Root Policy Set",
        policyIssuer,
        defaultsType,
        target,
        policySetsAndPoliciesAndPolicySetIdReferences,
        obligationExpressions,
        adviceExpressions,
        "RootPolicySet",
        "1.0",
        policyCombiningAlgId,
        maxDelegationDepth);
  }

  private TopLevelPolicyElementEvaluator getPolicy(
      String policyIdRef, Optional<PolicyVersionPatterns> constraints)
      throws IndeterminateEvaluationException {
    if (policies.containsKey(policyIdRef)) {
      PolicyVersions<ParsedPolicy> policyVersions = new PolicyVersions<>(policies.get(policyIdRef));

      Entry<PolicyVersion, ParsedPolicy> policyVersion = policyVersions.getLatest(constraints);

      if (policyVersion != null) {
        ParsedPolicy parsedPolicy = policyVersion.getValue();

        return PolicyEvaluators.getInstance(
            parsedPolicy.policy(),
            expressionFactory,
            combiningAlgorithmRegistry,
            Optional.empty(),
            parsedPolicy.xmlnsToPrefixMap());
      }
    }

    return null;
  }

  private TopLevelPolicyElementEvaluator getPolicySet(
      String policySetIdRef,
      Optional<PolicyVersionPatterns> constraints,
      Deque<String> policySetRefChainWithPolicyIdRef)
      throws IndeterminateEvaluationException {
    if (policySetIdRef.equals("RootPolicySet")) {
      return PolicyEvaluators.getInstance(
          rootPolicySet,
          expressionFactory,
          combiningAlgorithmRegistry,
          this,
          policySetRefChainWithPolicyIdRef,
          Optional.empty(),
          null);
    } else if (policySets.containsKey(policySetIdRef)) {
      PolicyVersions<ParsedPolicySet> policySetVersions =
          new PolicyVersions<>(policySets.get(policySetIdRef));

      Entry<PolicyVersion, ParsedPolicySet> policySetVersion =
          policySetVersions.getLatest(constraints);

      if (policySetVersion != null) {
        ParsedPolicySet parsedPolicySet = policySetVersion.getValue();

        return PolicyEvaluators.getInstance(
            parsedPolicySet.policySet(),
            expressionFactory,
            combiningAlgorithmRegistry,
            this,
            policySetRefChainWithPolicyIdRef,
            Optional.empty(),
            parsedPolicySet.xmlnsToPrefixMap());
      }
    }

    return null;
  }

  /** Load the local policy sets and policies from the classpath. */
  private void loadClasspathPolicies() {
    try {
      ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

      Resource[] policyResources =
          resourcePatternResolver.getResources("classpath:/pdp/policies/*.xml");

      for (Resource policyResource : policyResources) {
        try {
          try (InputStream inputStream = policyResource.getInputStream()) {
            loadPolicyOrPolicySet(new InputSource(inputStream));
          }
        } catch (Throwable e) {
          log.error(
              "Failed to load the classpath policy resource ("
                  + policyResource.getFilename()
                  + ") for the policy decision point",
              e);
        }
      }

      log.info(
          "Successfully loaded the classpath policy sets and policies for the policy decision point");
    } catch (Throwable e) {
      throw new PolicyDecisionPointException(
          "Failed to load the classpath policy sets and policies for the policy decision point", e);
    }
  }

  /**
   * Load the policy set or policy.
   *
   * @param inputSource the input source for the policy set or policy
   * @throws JAXBException if a Jakarta XML Binding exception occurs
   */
  private void loadPolicyOrPolicySet(InputSource inputSource) throws JAXBException {
    XmlnsFilteringParser xacmlParser = xacmlParserFactory.getInstance();

    Object policyObject = xacmlParser.parse(inputSource);

    if (policyObject instanceof PolicySet policySet) {
      PolicyVersion policySetVersion = new PolicyVersion(policySet.getVersion());

      Map<PolicyVersion, ParsedPolicySet> policySetVersions;

      if (policySets.containsKey(policySet.getPolicySetId())) {
        policySetVersions = policySets.get(policySet.getPolicySetId());
      } else {
        policySetVersions = new HashMap<>();
        policySets.put(policySet.getPolicySetId(), policySetVersions);
      }

      policySetVersions.put(
          policySetVersion, new ParsedPolicySet(policySet, xacmlParser.getNamespacePrefixUriMap()));

      if (log.isDebugEnabled()) {
        log.debug(
            "Loaded the policy set ("
                + policySet.getPolicySetId()
                + ") with version ("
                + policySet.getVersion()
                + ") for the policy decision point");
      }
    } else if (policyObject instanceof Policy policy) {
      PolicyVersion policyVersion = new PolicyVersion(policy.getVersion());

      Map<PolicyVersion, ParsedPolicy> policyVersions;

      if (policies.containsKey(policy.getPolicyId())) {
        policyVersions = policies.get(policy.getPolicyId());
      } else {
        policyVersions = new HashMap<>();
        policies.put(policy.getPolicyId(), policyVersions);
      }

      policyVersions.put(
          policyVersion, new ParsedPolicy(policy, xacmlParser.getNamespacePrefixUriMap()));

      if (log.isDebugEnabled()) {
        log.debug(
            "Loaded the policy ("
                + policy.getPolicyId()
                + ") with version ("
                + policy.getVersion()
                + ") for the policy decision point");
      }
    }
  }

  /** Reload the external policy sets and policies using the policy API endpoint. */
  private void reloadExternalPolicies() {
    if (externalPoliciesEnabled && StringUtils.hasText(externalPoliciesEndpoint)) {
      if ((reloadExternalPoliciesWhen == null)
          || (LocalDateTime.now().isAfter(reloadExternalPoliciesWhen))) {
        try {
          RestTemplate restTemplate = new RestTemplate();

          ResponseEntity<List<ExternalPolicy>> response =
              restTemplate.exchange(
                  externalPoliciesEndpoint,
                  HttpMethod.GET,
                  null,
                  new ParameterizedTypeReference<>() {});

          if (response.getStatusCode() == HttpStatus.OK) {
            List<ExternalPolicy> externalPolicies = response.getBody();
            if ((externalPolicies != null) && (!externalPolicies.isEmpty())) {
              for (ExternalPolicy externalPolicy : externalPolicies) {
                try {
                  loadPolicyOrPolicySet(new InputSource(new StringReader(externalPolicy.data())));
                } catch (Throwable e) {
                  if ("xacml_policy_set".equals(externalPolicy.type())) {
                    log.error(
                        "Failed to load the external policy set ("
                            + externalPolicy.id()
                            + ") for the policy decision point",
                        e);
                  } else if ("xacml_policy".equals(externalPolicy.type())) {
                    log.error(
                        "Failed to load the external policy ("
                            + externalPolicy.id()
                            + ")  for the policy decision point",
                        e);
                  }
                }
              }
            }

            rootPolicySet = buildRootPolicySet();

            reloadExternalPoliciesWhen =
                LocalDateTime.now().plusSeconds(externalPoliciesReloadPeriod);

            log.info(
                "Successfully loaded the external policy sets and policies for the "
                    + "policy decision point");
          }

        } catch (Throwable e) {
          log.error(
              "Failed to load the external policy sets and policies for the "
                  + "policy decision point using the API endpoint ("
                  + externalPoliciesEndpoint
                  + ")",
              e);
        }
      }
    }
  }

  @JsonIgnoreProperties(ignoreUnknown = true)
  private record ExternalPolicy(String id, String version, String name, String type, String data) {}

  /**
   * The {@code Factory} class implements the factory that creates {@code
   * PolicyDecisionPointCloseableDynamicPolicyProvider} instances.
   */
  public static class Factory
      extends CloseablePolicyProvider.Factory<XacmlPolicyDecisionPointPolicyProvider> {

    /** Creates a new {@code Factory} instance. */
    public Factory() {
      int xxx = 0;
      xxx++;
    }

    @Override
    public CloseablePolicyProvider<?> getInstance(
        XacmlPolicyDecisionPointPolicyProvider configuration,
        XmlnsFilteringParserFactory xacmlParserFactory,
        int maxPolicySetRefDepth,
        ExpressionFactory expressionFactory,
        CombiningAlgRegistry combiningAlgRegistry,
        EnvironmentProperties environmentProperties,
        Optional<PolicyProvider<?>> otherHelpingPolicyProvider)
        throws IllegalArgumentException {
      try {
        return new XacmlPolicyDecisionPointDynamicPolicyProvider(
            xacmlParserFactory,
            expressionFactory,
            combiningAlgRegistry,
            configuration.getClasspathPoliciesEnabled(),
            configuration.getExternalPoliciesEnabled(),
            configuration.getExternalPoliciesEndpoint(),
            configuration.getExternalPoliciesReloadPeriod());
      } catch (Throwable e) {
        throw new RuntimeException(
            "Failed to create the XacmlPolicyDecisionPointDynamicPolicyProvider instance", e);
      }
    }

    @Override
    public Class<XacmlPolicyDecisionPointPolicyProvider> getJaxbClass() {
      return XacmlPolicyDecisionPointPolicyProvider.class;
    }
  }

  private record ParsedPolicy(Policy policy, Map<String, String> xmlnsToPrefixMap) {}

  private record ParsedPolicySet(PolicySet policySet, Map<String, String> xmlnsToPrefixMap) {}
}
