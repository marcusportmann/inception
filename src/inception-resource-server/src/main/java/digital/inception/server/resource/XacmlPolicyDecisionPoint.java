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

package digital.inception.server.resource;

import java.lang.reflect.Method;
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
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The <b>XacmlPolicyDecisionPoint</b> class provides an implementation of a policy decision point
 * that leverages the AuthZForce PDP engine to apply decisions based on XACML-based policy sets and
 * policies, which are loaded from the classpath and optionally from a RESTful endpoint.
 *
 * @author Marcus Portmann
 */
@Service
@ConditionalOnProperty(
    value = "inception.resource-server.xacml-policy-decision-point.enabled",
    havingValue = "true")
public final class XacmlPolicyDecisionPoint implements PolicyDecisionPoint {

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

  /** The AuthZForce PDP Engine. */
  private PdpEngine pdpEngine;

  /**
   * Constructs a new <b>XacmlPolicyDecisionPoint</b>.
   *
   * @param applicationContext the Spring application context
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
      List<String> resourceIds = new ArrayList<>();

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

        if (methodParameter.getAnnotations().length == 0) {
          Method interfaceMethod = findMethodInInterface(methodInvocation.getMethod());

          if (interfaceMethod != null) {
            methodParameter = interfaceMethod.getParameters()[i];
          }
        }

        String methodRequestParameterName = methodParameter.getName();
        Object methodRequestParameterValue = methodInvocation.getArguments()[i];

        PathVariable pathVariable = methodParameter.getAnnotation(PathVariable.class);
        RequestParam requestParam = methodParameter.getAnnotation(RequestParam.class);

        String policyDecisionPointAttributeCategoryName;

        if (pathVariable != null) {
          policyDecisionPointAttributeCategoryName =
              XacmlPolicyDecisionPointAttributeCategory.PATH_VARIABLES.value();
        } else if (requestParam != null) {
          policyDecisionPointAttributeCategoryName =
              XacmlPolicyDecisionPointAttributeCategory.REQUEST_PARAMETERS.value();
        } else {
          policyDecisionPointAttributeCategoryName = null;
        }

        if (policyDecisionPointAttributeCategoryName != null) {
          if (methodRequestParameterValue instanceof Map<?, ?> nestedParameterMap) {
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
            if (XacmlUtil.isValidAttributeValue(methodRequestParameterValue)) {
              XacmlUtil.addAttributeToRequest(
                  decisionRequestBuilder,
                  policyDecisionPointAttributeCategoryName,
                  methodRequestParameterName,
                  XacmlUtil.getAttributeValues(methodRequestParameterValue));
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

        return true;
      } else {
        if (inDebugMode) {
          if (authenticationObject instanceof Authentication authentication) {
            log.info(
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
            log.info(
                "Policy decision point authorization failed for class ("
                    + methodInvocation.getMethod().getDeclaringClass().getName()
                    + ") and method ("
                    + methodInvocation.getMethod().getName()
                    + ") with applicable policies ("
                    + XacmlUtil.policiesToString(decisionResult.getApplicablePolicies())
                    + ")");
          }
        }

        return false;
      }
    } catch (AccessDeniedException e) {
      throw e;
    } catch (Throwable e) {
      log.error(
          "Policy decision point authorization failed for class ("
              + methodInvocation.getMethod().getDeclaringClass().getName()
              + ") and method ("
              + methodInvocation.getMethod().getName()
              + ")",
          e);

      return false;
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
   * Returns the AuthZForce PDP Engine.
   *
   * @return the AuthZForce PDP Engine
   */
  public PdpEngine getPdpEngine() {
    if (pdpEngine == null) {
      initPdpEngine();
    }

    return pdpEngine;
  }

  private Method findMethodInInterface(Method invokedMethod) {
    Class<?>[] interfaces = invokedMethod.getDeclaringClass().getInterfaces();

    for (Class<?> iface : interfaces) {
      try {
        // Attempt to find the method in the interface
        return iface.getMethod(invokedMethod.getName(), invokedMethod.getParameterTypes());
      } catch (NoSuchMethodException e) {
        // Method not found in this interface, continue searching
      }
    }

    return null; // Method not found in any interface
  }

  /** Initialize the AuthZForce PDP engine */
  private void initPdpEngine() {
    // Initialize the AuthZForce PDP engine
    try {
      List<String> attributeDataTypes = null;
      List<String> functions = null;
      List<String> combiningAlgorithms = null;
      List<AbstractAttributeProvider> attributeProviders = null;
      List<AbstractPolicyProvider> policyProviders = new ArrayList<>();
      TopLevelPolicyElementRef rootPolicyRef =
          new TopLevelPolicyElementRef("RootPolicySet", "1.0", true);
      AbstractDecisionCache decisionCache = null;

      List<InOutProcChain> ioProcChains = null;
      String version = null; // 8.1?

      Boolean standardDataTypesEnabled = null;
      Boolean standardFunctionsEnabled = null;
      Boolean standardCombiningAlgorithmsEnabled = null;
      Boolean standardAttributeProvidersEnabled = null;
      Boolean xPathEnabled = null;
      Boolean strictAttributeIssuerMatch = null;
      BigInteger maxIntegerValue = null;
      BigInteger maxVariableRefDepth = null;
      BigInteger maxPolicyRefDepth = null;
      BigInteger clientRequestErrorVerbosityLevel = null;

      /*
       * Policy providers consist of three related classes. The first is the JAXB configuration
       * class for the policy provider, e.g. PolicyDecisionPointPolicyProvider. The second
       * is the policy provider implementation, which must implement the
       * CloseablePolicyProvider<TopLevelPolicyElementEvaluator> interface, e.g.
       * PolicyDecisionPointDynamicPolicyProvider. The third is the nested static factory class
       * used to create instances of the policy provider, e.g.
       * PolicyDecisionPointDynamicPolicyProvider$Factory.
       */
      policyProviders.add(
          new XacmlPolicyDecisionPointPolicyProvider(
              classpathPoliciesEnabled,
              externalPoliciesEnabled,
              externalPoliciesEndpoint,
              externalPoliciesReloadPeriod));

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
      throw new PolicyDecisionPointException("Failed to initialize the AuthZForce PDP Engine", e);
    }
  }
}
