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

package digital.inception.operations.store;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * The <b>InternalDocumentStoreEnabledCondition</b> class implements the condition that must be
 * matched to enable the internal document store.
 *
 * @author Marcus Portmann
 */
public class InternalDocumentStoreEnabledCondition implements Condition {

  /** Constructs a new <b>InternalDocumentStoreEnabledCondition</b>. */
  public InternalDocumentStoreEnabledCondition() {}

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String documentStoreType =
        context.getEnvironment().getProperty("inception.operations.document-store-type");

    return (documentStoreType == null) || "internal".equals(documentStoreType);
  }
}
