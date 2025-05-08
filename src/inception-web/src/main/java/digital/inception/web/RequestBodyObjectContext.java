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

package digital.inception.web;

import java.lang.reflect.Parameter;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * The {@code RequestBodyObjectContext} class provides a mechanism to store and retrieve the Java
 * object populated from the JSON request body of a RESTful API invocation in a thread-local
 * variable.
 *
 * <p>This class is intended for use in applications built with the Spring web framework, where each
 * HTTP request is handled by a separate thread, ensuring that the stored object is isolated to the
 * specific request context.
 *
 * <p><b>Usage:</b>
 *
 * <ul>
 *   <li>Call {@link #setRequestBodyObject(Object)} to store the request body object at the start of
 *       the request processing.
 *   <li>Use {@link #getRequestBodyObject()} to retrieve the stored object during processing.
 *   <li>Invoke {@link #clear()} at the end of request processing to avoid memory leaks.
 * </ul>
 *
 * <p><b>Thread Safety:</b> This class uses a {@link ThreadLocal} to ensure thread-local storage,
 * making it suitable for multi-threaded environments such as web applications.
 *
 * <p><b>Important:</b> Always call {@link #clear()} to clean up the thread-local variable after the
 * request is processed to prevent potential memory leaks in thread pools.
 *
 * @author Marcus Portmann
 */
public final class RequestBodyObjectContext {
  private static final ThreadLocal<Object> threadLocalRequestBodyObject = new ThreadLocal<>();

  /** Private constructor to prevent instantiation. */
  private RequestBodyObjectContext() {}

  /** Clear the request body object context. */
  public static void clear() {
    threadLocalRequestBodyObject.remove();
  }

  /**
   * Retrieve the Java object populated from the JSON request body for a RESTful API invocation by
   * the Spring web framework
   *
   * @return the Java object populated from the JSON request body for a RESTful API invocation by
   *     the Spring web framework
   */
  public static Object getRequestBodyObject() {
    return threadLocalRequestBodyObject.get();
  }

  /**
   * Set the Java object populated from the JSON request body for a RESTful API invocation by the
   * Spring web framework.
   *
   * @param requestBody the Java object populated from the JSON request body for a RESTful API
   *     invocation by the Spring web framework
   */
  public static void setRequestBodyObject(Object requestBody) {
    threadLocalRequestBodyObject.set(requestBody);
  }

  /**
   * Set the Java object populated from the JSON request body for a RESTful API invocation by the
   * Spring web framework.
   *
   * @param methodInvocation the method invocation the Java object populated from the JSON request
   *     body for a RESTful API invocation by the Spring web framework
   */
  public static void setRequestBodyObject(MethodInvocation methodInvocation) {
    for (int i = 0; i < methodInvocation.getMethod().getParameters().length; i++) {
      Parameter methodParameter = methodInvocation.getMethod().getParameters()[i];

      RequestBody requestBody = AnnotationUtils.findAnnotation(methodParameter, RequestBody.class);

      if (requestBody != null) {
        Object methodParameterValue = methodInvocation.getArguments()[i];

        threadLocalRequestBodyObject.set(methodParameterValue);
      }
    }
  }
}
