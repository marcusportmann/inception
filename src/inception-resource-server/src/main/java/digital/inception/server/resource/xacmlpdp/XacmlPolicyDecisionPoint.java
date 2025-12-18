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

import digital.inception.core.util.AnnotationUtil;
import digital.inception.server.resource.PolicyDecisionPoint;
import digital.inception.server.resource.PolicyDecisionPointException;
import digital.inception.web.RequestBodyObjectContext;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import java.lang.reflect.Parameter;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.DecisionType;
import org.aopalliance.intercept.MethodInvocation;
import org.ow2.authzforce.core.pdp.api.DecisionRequest;
import org.ow2.authzforce.core.pdp.api.DecisionRequestBuilder;
import org.ow2.authzforce.core.pdp.api.DecisionResult;
import org.ow2.authzforce.core.pdp.api.EnvironmentProperties;
import org.ow2.authzforce.core.pdp.api.PdpEngine;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.DefaultEnvironmentProperties;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.core.xmlns.pdp.InOutProcChain;
import org.ow2.authzforce.core.xmlns.pdp.Pdp;
import org.ow2.authzforce.core.xmlns.pdp.TopLevelPolicyElementRef;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeCategory;
import org.ow2.authzforce.xacml.identifiers.XacmlAttributeId;
import org.ow2.authzforce.xmlns.pdp.ext.AbstractAttributeProvider;
import org.ow2.authzforce.xmlns.pdp.ext.AbstractDecisionCache;
import org.ow2.authzforce.xmlns.pdp.ext.AbstractPolicyProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The {@code XacmlPolicyDecisionPoint} class provides an implementation of a policy decision point
 * that leverages the AuthzForce PDP engine to apply decisions based on XACML-based policy sets and
 * policies, which are loaded from the classpath and optionally from a RESTful endpoint.
 *
 * @author Marcus Portmann
 */
@ConditionalOnProperty(
    value = "inception.resource-server.xacml-policy-decision-point.enabled",
    havingValue = "true")
@Component
public final class XacmlPolicyDecisionPoint implements PolicyDecisionPoint {

  /**
   * The name of the Micrometer metric that captures the number of deny decisions for the XACML
   * policy decision point.
   */
  private static final String METRIC_NAME_XACML_POLICY_DECISION_POINT_DENY =
      "inception.resource-server.xacml-policy-decision-point.deny";

  /**
   * The name of the Micrometer metric that captures the number of errors for the XACML policy
   * decision point.
   */
  private static final String METRIC_NAME_XACML_POLICY_DECISION_POINT_ERROR =
      "inception.resource-server.xacml-policy-decision-point.error";

  /**
   * The name of the Micrometer metric that captures the number of permit decisions for the XACML
   * policy decision point.
   */
  private static final String METRIC_NAME_XACML_POLICY_DECISION_POINT_PERMIT =
      "inception.resource-server.xacml-policy-decision-point.permit";

  /**
   * The name of the Micrometer metric that captures the time taken by the XAML policy decision
   * endpoint to execute authorization decisions.
   */
  private static final String METRIC_NAME_XACML_POLICY_DECISION_POINT_TIME =
      "inception.resource-server.xacml-policy-decision-point.time";

  /* Logger */
  private static final Logger log = LoggerFactory.getLogger(XacmlPolicyDecisionPoint.class);

  /** The Spring application context. */
  private final ApplicationContext applicationContext;

  /** Should policy sets and policies be loaded from the classpath under pdp/policies. */
  private final boolean classpathPoliciesEnabled;

  /** Should policy sets and policies be loaded by invoking an external API. */
  private final boolean externalPoliciesEnabled;

  /** The external API endpoint used to retrieve policy sets and policies. */
  private final String externalPoliciesEndpoint;

  /** The reload period in seconds for external policy sets and policies. */
  private final int externalPoliciesReloadPeriod;

  /** The policy decision point context providers. */
  private final Map<String, XacmlPolicyDecisionPointContextProvider>
      policyDecisionPointContextProviders;

  /** Is debugging enabled for the Inception Framework? */
  @Value("${inception.debug.enabled:#{false}}")
  private boolean inDebugMode;

  /** The Micrometer registry. */
  private MeterRegistry meterRegistry;

  /** The AuthzForce PDP Engine. */
  private PdpEngine pdpEngine;

  /**
   * Constructs a new {@code XacmlPolicyDecisionPoint}.
   *
   * @param applicationContext the Spring {@link ApplicationContext}
   * @param classpathPoliciesEnabled should policy sets and policies be loaded from the classpath
   *     under pdp/policies
   * @param externalPoliciesEnabled should policy sets and policies be loaded by invoking an
   *     external API
   * @param externalPoliciesEndpoint the external API endpoint used to retrieve policy sets and
   *     policies
   * @param externalPoliciesReloadPeriod the reload period in seconds for external policies
   */
  public XacmlPolicyDecisionPoint(
      ApplicationContext applicationContext,
      @Value(
              "${inception.resource-server.xacml-policy-decision-point.classpath-policies.enabled:#{true}}")
          boolean classpathPoliciesEnabled,
      @Value(
              "${inception.resource-server.xacml-policy-decision-point.external-policies.enabled:#{false}}")
          boolean externalPoliciesEnabled,
      @Value(
              "${inception.resource-server.xacml-policy-decision-point.external-policies.endpoint:#{null}}")
          String externalPoliciesEndpoint,
      @Value(
              "${inception.resource-server.xacml-policy-decision-point.external-policies.reload-period:#{43200}}")
          int externalPoliciesReloadPeriod) {
    this.applicationContext = applicationContext;
    this.classpathPoliciesEnabled = classpathPoliciesEnabled;
    this.externalPoliciesEnabled = externalPoliciesEnabled;
    this.externalPoliciesEndpoint = externalPoliciesEndpoint;
    this.externalPoliciesReloadPeriod = externalPoliciesReloadPeriod;

    try {
      this.meterRegistry = this.applicationContext.getBean(MeterRegistry.class);
    } catch (Throwable ignored) {
    }

    try {
      this.policyDecisionPointContextProviders =
          applicationContext.getBeansOfType(XacmlPolicyDecisionPointContextProvider.class);

      for (Entry<String, XacmlPolicyDecisionPointContextProvider>
          policyDecisionPointContextProvider :
              this.policyDecisionPointContextProviders.entrySet()) {
        log.info(
            "Loaded the XACML policy decision point context provider ({}) with class ({})",
            policyDecisionPointContextProvider.getKey(),
            policyDecisionPointContextProvider.getValue().getClass().getName());
      }
    } catch (Throwable e) {
      throw new BeanCreationException(
          "Failed to initialize the XACML policy decision point context providers", e);
    }
  }

  @Override
  public boolean authorize(Object authenticationObject, MethodInvocation methodInvocation) {
    Timer.Sample sample = null;
    if (meterRegistry != null) {
      sample = Timer.start(meterRegistry);
    }

    // Default decision
    String decision = "DENY";

    List<String> resourceIds = new ArrayList<>();

    try {
      // Create the decision request
      DecisionRequestBuilder<?> decisionRequestBuilder = getPdpEngine().newRequestBuilder(-1, -1);

      RequestMapping classRequestMapping =
          AnnotationUtils.findAnnotation(
              methodInvocation.getMethod().getDeclaringClass(), RequestMapping.class);

      if (classRequestMapping == null) {
        log.error(
            "Policy decision point authorization failed: "
                + "No RequestMapping annotation found on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ")");

        return false;
      }

      String[] classPathMappingUris = classRequestMapping.value();

      if (classPathMappingUris.length == 0) {
        classPathMappingUris = classRequestMapping.path();
      }

      if (classPathMappingUris.length == 0) {
        log.error(
            "Policy decision point authorization failed: "
                + "No path mapping URIs specified for RequestMapping annotation on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ")");

        return false;
      }

      RequestMapping methodRequestMapping =
          AnnotationUtils.findAnnotation(methodInvocation.getMethod(), RequestMapping.class);

      if (methodRequestMapping == null) {
        log.error(
            "Policy decision point authorization failed: "
                + "No RequestMapping annotation found on method ("
                + methodInvocation.getMethod().getName()
                + ") on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ")");

        return false;
      }

      String[] methodPathMappingUris = methodRequestMapping.value();

      if (methodPathMappingUris.length == 0) {
        methodPathMappingUris = methodRequestMapping.path();
      }

      if (methodPathMappingUris.length == 0) {
        log.error(
            "Policy decision point authorization failed: "
                + "No path mapping URIs specified for RequestMapping annotation on method ("
                + methodInvocation.getMethod().getName()
                + ") on class ("
                + methodInvocation.getMethod().getDeclaringClass().getName()
                + ")");

        return false;
      }

      /*
       * Build the resource IDs from a combination of the class-level and method-level path mapping
       * URIs on the RequestMapping annotations.
       */
      for (String classPathMappingUri : classPathMappingUris) {
        for (String methodPathMappingUri : methodPathMappingUris) {
          resourceIds.add(classPathMappingUri + methodPathMappingUri);
        }
      }

      XacmlUtil.addAttributeToRequest(
          decisionRequestBuilder,
          XacmlAttributeCategory.XACML_3_0_RESOURCE.value(),
          XacmlAttributeId.XACML_1_0_RESOURCE_ID.value(),
          resourceIds);

      XacmlUtil.addAttributeToRequest(
          decisionRequestBuilder,
          XacmlAttributeCategory.XACML_3_0_ACTION.value(),
          XacmlAttributeId.XACML_1_0_ACTION_ID.value(),
          Arrays.stream(methodRequestMapping.method())
              .map(Enum::name)
              .collect(Collectors.toList()));

      // Add the attributes for the path variables and request parameters
      for (int i = 0; i < methodInvocation.getMethod().getParameters().length; i++) {
        Parameter methodParameter = methodInvocation.getMethod().getParameters()[i];

        String methodParameterName = methodParameter.getName();
        Object methodParameterValue = methodInvocation.getArguments()[i];

        String policyDecisionPointAttributeCategoryName;

        if (AnnotationUtil.isMethodParameterAnnotatedWithAnnotation(
            methodInvocation, methodParameter, PathVariable.class)) {
          policyDecisionPointAttributeCategoryName =
              XacmlPolicyDecisionPointAttributeCategory.PATH_VARIABLES.value();
        } else if (AnnotationUtil.isMethodParameterAnnotatedWithAnnotation(
            methodInvocation, methodParameter, RequestParam.class)) {
          policyDecisionPointAttributeCategoryName =
              XacmlPolicyDecisionPointAttributeCategory.REQUEST_PARAMETERS.value();
        } else {
          policyDecisionPointAttributeCategoryName = null;

          if (AnnotationUtil.isMethodParameterAnnotatedWithAnnotation(
              methodInvocation, methodParameter, RequestBody.class)) {
            RequestBodyObjectContext.setRequestBodyObject(methodParameterValue);
          }
        }

        if (policyDecisionPointAttributeCategoryName != null) {
          if (methodParameterValue instanceof Map<?, ?> nestedParameterMap) {
            nestedParameterMap.forEach(
                (nestedRequestParameterName, nestedRequestParameterValue) -> {
                  if (XacmlUtil.isValidAttributeValue(nestedRequestParameterValue)) {
                    XacmlUtil.addAttributeToRequest(
                        decisionRequestBuilder,
                        policyDecisionPointAttributeCategoryName,
                        nestedRequestParameterName.toString(),
                        XacmlUtil.getAttributeValues(nestedRequestParameterValue));
                  }
                });
          } else {
            if (XacmlUtil.isValidAttributeValue(methodParameterValue)) {
              XacmlUtil.addAttributeToRequest(
                  decisionRequestBuilder,
                  policyDecisionPointAttributeCategoryName,
                  methodParameterName,
                  XacmlUtil.getAttributeValues(methodParameterValue));
            }
          }
        }
      }

      // Add the current datetime attribute (action category), no issuer, string value
      XacmlUtil.addAttributeToRequest(
          decisionRequestBuilder,
          XacmlAttributeCategory.XACML_3_0_ENVIRONMENT.value(),
          XacmlAttributeId.XACML_1_0_ENVIRONMENT_CURRENT_DATETIME.value(),
          XacmlUtil.getAttributeValues(ZonedDateTime.now()));

      // Add any additional request attributes using the policy decision point context providers
      for (XacmlPolicyDecisionPointContextProvider policyDecisionPointContextProvider :
          policyDecisionPointContextProviders.values()) {
        policyDecisionPointContextProvider.populateDecisionRequest(
            authenticationObject, methodInvocation, decisionRequestBuilder);
      }

      // No more attributes, let's finalize the request creation
      DecisionRequest decisionRequest = decisionRequestBuilder.build(true);

      // Evaluate the request
      DecisionResult decisionResult = evaluate(decisionRequest);

      if (decisionResult.getDecision() == DecisionType.PERMIT) {
        decision = "PERMIT";

        if (inDebugMode) {
          if (authenticationObject instanceof Authentication authentication) {
            log.info(
                "Policy decision point authorization successful for class ("
                    + methodInvocation.getMethod().getDeclaringClass().getName()
                    + ") and method ("
                    + methodInvocation.getMethod().getName()
                    + ") for user ("
                    + authentication.getName()
                    + ") with applicable policies ("
                    + XacmlUtil.policiesToString(decisionResult.getApplicablePolicies())
                    + ")");
          } else {
            log.info(
                "Policy decision point authorization successful for class ("
                    + methodInvocation.getMethod().getDeclaringClass().getName()
                    + ") and method ("
                    + methodInvocation.getMethod().getName()
                    + ") with applicable policies ("
                    + XacmlUtil.policiesToString(decisionResult.getApplicablePolicies())
                    + ")");
          }
        }

        if (meterRegistry != null) {
          meterRegistry.counter(METRIC_NAME_XACML_POLICY_DECISION_POINT_PERMIT).increment();
        }

        return true;
      } else {
        if (authenticationObject instanceof Authentication authentication) {
          log.warn(
              "Policy decision point authorization failed for class ("
                  + methodInvocation.getMethod().getDeclaringClass().getName()
                  + ") and method ("
                  + methodInvocation.getMethod().getName()
                  + ") for user ("
                  + authentication.getName()
                  + ") with applicable policies ("
                  + XacmlUtil.policiesToString(decisionResult.getApplicablePolicies())
                  + ")");
        } else {
          log.warn(
              "Policy decision point authorization failed for class ("
                  + methodInvocation.getMethod().getDeclaringClass().getName()
                  + ") and method ("
                  + methodInvocation.getMethod().getName()
                  + ") with applicable policies ("
                  + XacmlUtil.policiesToString(decisionResult.getApplicablePolicies())
                  + ")");
        }

        if (meterRegistry != null) {
          meterRegistry.counter(METRIC_NAME_XACML_POLICY_DECISION_POINT_DENY).increment();
        }

        return false;
      }
    } catch (AccessDeniedException e) {
      if (meterRegistry != null) {
        meterRegistry.counter(METRIC_NAME_XACML_POLICY_DECISION_POINT_DENY).increment();
      }

      throw e;
    } catch (Throwable e) {
      if (meterRegistry != null) {
        meterRegistry.counter(METRIC_NAME_XACML_POLICY_DECISION_POINT_ERROR).increment();
      }

      log.error(
          "Policy decision point authorization failed for class ("
              + methodInvocation.getMethod().getDeclaringClass().getName()
              + ") and method ("
              + methodInvocation.getMethod().getName()
              + ")",
          e);

      return false;
    } finally {
      if (!resourceIds.isEmpty()) {
        if (sample != null) {
          sample.stop(
              Timer.builder(METRIC_NAME_XACML_POLICY_DECISION_POINT_TIME)
                  .tags("uri", resourceIds.getFirst(), "decision", decision)
                  .register(meterRegistry));
        }
      }

      RequestBodyObjectContext.clear();
    }
  }

  /**
   * Evaluate an individual decision request.
   *
   * <p>This method does not throw any exception but may still return an Indeterminate result if an
   * error occurred. Therefore, clients should check whether {@link DecisionResult#getDecision() ==
   * DecisionType#INDETERMINATE}, in which case they can get more error info from {@link
   * DecisionResult#getCauseForIndeterminate()}).
   *
   * @param request The Individual Decision Request, as defined in the XACML Multiple Decision
   *     Profile (also mentioned in the Hierarchical Resource Profile)
   * @return the individual decision result.
   */
  public DecisionResult evaluate(DecisionRequest request) {
    return pdpEngine.evaluate(request);
  }

  /**
   * Returns the AuthzForce PDP Engine.
   *
   * @return the AuthzForce PDP Engine
   */
  public PdpEngine getPdpEngine() {
    if (pdpEngine == null) {
      initPdpEngine();
    }

    return pdpEngine;
  }

  /** Initialize the AuthzForce PDP engine */
  private void initPdpEngine() {
    // Initialize the AuthzForce PDP engine
    try {
      List<String> attributeDataTypes = null;
      List<String> functions = new ArrayList<>();
      List<String> combiningAlgorithms = null;
      List<AbstractAttributeProvider> attributeProviders = null;
      List<AbstractPolicyProvider> policyProviders = new ArrayList<>();
      TopLevelPolicyElementRef rootPolicyRef =
          new TopLevelPolicyElementRef("RootPolicySet", "1.0", true);
      AbstractDecisionCache decisionCache = null;

      List<InOutProcChain> ioProcChains = null;
      String version = null; // 8.1?

      Boolean standardDataTypesEnabled = true;
      Boolean standardFunctionsEnabled = true;
      Boolean standardCombiningAlgorithmsEnabled = true;
      Boolean standardAttributeProvidersEnabled = true;
      Boolean xPathEnabled = false;
      Boolean strictAttributeIssuerMatch = false;
      BigInteger maxIntegerValue = null; // new BigInteger("2147483647")
      BigInteger maxVariableRefDepth = null;
      BigInteger maxPolicyRefDepth = null;
      BigInteger clientRequestErrorVerbosityLevel = new BigInteger("0");

      /*
       * Policy providers consist of three related classes. The first is the JAXB configuration
       * class for the policy provider, e.g. PolicyDecisionPointPolicyProvider. The second
       * is the policy provider implementation, which must implement the
       * CloseablePolicyProvider<TopLevelPolicyElementEvaluator> interface, e.g.
       * PolicyDecisionPointDynamicPolicyProvider. The third is the nested static factory class
       * used to create instances of the policy provider, e.g.
       * PolicyDecisionPointDynamicPolicyProvider$Factory.
       *
       * NOTE: The policy provider factory is also added to the
       * META/services/org.ow2.authzforce.core.pdp.api.PdpExtension file, which defines the
       * extensions for the AuthzForce PDP.
       */
      policyProviders.add(
          new XacmlPolicyDecisionPointPolicyProvider(
              classpathPoliciesEnabled,
              externalPoliciesEnabled,
              externalPoliciesEndpoint,
              externalPoliciesReloadPeriod));

      /*
       * To add a custom function, we add the function ID here and the name of the function class to
       * the META/services/org.ow2.authzforce.core.pdp.api.PdpExtension file, which defines the
       * extensions for the AuthzForce PDP.
       */
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyBooleanAttributeValueFunction.ID);
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyDateAttributeValueFunction.ID);
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyDateTimeAttributeValueFunction.ID);
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyDoubleAttributeValueFunction.ID);
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyIntegerAttributeValueFunction.ID);
      functions.add(
          XacmlGetRequestBodyObjectAttributeValueFunctions
              .XacmlGetRequestBodyStringAttributeValueFunction.ID);

      Pdp pdp =
          new Pdp(
              attributeDataTypes,
              functions,
              combiningAlgorithms,
              attributeProviders,
              policyProviders,
              rootPolicyRef,
              decisionCache,
              ioProcChains,
              version,
              standardDataTypesEnabled,
              standardFunctionsEnabled,
              standardCombiningAlgorithmsEnabled,
              standardAttributeProvidersEnabled,
              xPathEnabled,
              strictAttributeIssuerMatch,
              maxIntegerValue,
              maxVariableRefDepth,
              maxPolicyRefDepth,
              clientRequestErrorVerbosityLevel);

      EnvironmentProperties environmentProperties = new DefaultEnvironmentProperties();

      PdpEngineConfiguration pdpEngineConfiguration =
          new PdpEngineConfiguration(pdp, environmentProperties);

      // Create the PDP engine. You can reuse the same for all requests, so do it only once for all.
      pdpEngine = new BasePdpEngine(pdpEngineConfiguration);

    } catch (Throwable e) {
      throw new PolicyDecisionPointException("Failed to initialize the AuthzForce PDP Engine", e);
    }
  }
}
