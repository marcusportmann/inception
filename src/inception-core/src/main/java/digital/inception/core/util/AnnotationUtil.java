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

package digital.inception.core.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.aopalliance.intercept.MethodInvocation;

/**
 * The <b>AnnotationUtil</b> class is a utility class that supports working with annotations.
 *
 * @author Marcus Portmann
 */
public final class AnnotationUtil {

  private static final Map<String, Boolean> methodParameterAnnotationCheckCache =
      new ConcurrentHashMap<>();

  /** Private constructor to prevent instantiation. */
  private AnnotationUtil() {}

  /**
   * Checks if the specified method parameter for the method invocation is annotated with the
   * specified annotation taking into consideration both the implementation and interface methods.
   *
   * @param methodInvocation the method invocation
   * @param methodParameter the method parameter to check
   * @param annotationType the annotation type to check for
   * @return <b>true</b> if the parameter is annotated with the specified annotation or <b>false</b>
   *     otherwise
   */
  public static boolean isMethodParameterAnnotatedWithAnnotation(
      MethodInvocation methodInvocation,
      Parameter methodParameter,
      Class<? extends Annotation> annotationType) {
    // Get the method being invoked
    Method invokedMethod = methodInvocation.getMethod();

    // Create a unique cache key for the method and parameter
    String cacheKey = createCacheKey(invokedMethod, methodParameter, annotationType);

    // Check the cache
    return methodParameterAnnotationCheckCache.computeIfAbsent(
        cacheKey,
        key -> {
          try {
            if (methodParameter.getAnnotation(annotationType) != null) {
              return true;
            }

            // Find the interface declaring the method
            Class<?> declaringInterface =
                findDeclaringInterface(invokedMethod, methodInvocation.getThis().getClass());
            if (declaringInterface == null) {
              return false; // No interface declares the method
            }

            // Find the corresponding method in the interface
            Method interfaceMethod =
                declaringInterface.getMethod(
                    invokedMethod.getName(), invokedMethod.getParameterTypes());

            // Find the index of the parameter in the invoked method
            Parameter[] parameters = invokedMethod.getParameters();
            int paramIndex = -1;
            for (int i = 0; i < parameters.length; i++) {
              if (parameters[i].equals(methodParameter)) {
                paramIndex = i;
                break;
              }
            }

            // Parameter not found in the method
            if (paramIndex == -1) {
              return false;
            }

            // Get the parameter annotations from the interface method
            Parameter interfaceParameter = interfaceMethod.getParameters()[paramIndex];
            for (Annotation annotation : interfaceParameter.getAnnotations()) {
              if (annotation.annotationType().equals(annotationType)) {
                return true; // Found the specified annotation
              }
            }

            // No matching annotation found
            return false;
          } catch (Exception e) {
            throw new RuntimeException(
                "Failed to check whether the method parameter ("
                    + methodParameter.getName()
                    + ") on the method ("
                    + methodInvocation.getMethod().getName()
                    + ") has the annotation ("
                    + annotationType.getName()
                    + ")",
                e);
          }
        });
  }

  /**
   * Creates a unique cache key for the given method, parameter, and annotation type.
   *
   * @param method the method
   * @param parameter the parameter
   * @param annotationType the annotation type
   * @return a unique string key
   */
  private static String createCacheKey(
      Method method, Parameter parameter, Class<? extends Annotation> annotationType) {
    return method.toGenericString() + "#" + parameter.getName() + "#" + annotationType.getName();
  }

  /**
   * Finds the interface that declares the given method in the provided class.
   *
   * @param method the method to find
   * @param clazz the class implementing the interface
   * @return the interface declaring the method, or {@code null} if not found
   */
  private static Class<?> findDeclaringInterface(Method method, Class<?> clazz) {
    for (Class<?> iface : clazz.getInterfaces()) {
      try {
        iface.getMethod(method.getName(), method.getParameterTypes());
        return iface; // Found the interface declaring the method
      } catch (NoSuchMethodException ignored) {
        // Continue searching in other interfaces
      }
    }
    return null; // No interface found declaring the method
  }
}
