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
 * The <b>InternalWorkflowStoreEnabledCondition</b> class implements the condition that must be matched
 * to enable the internal workflow store.
 *
 * @author Marcus Portmann
 */
public class InternalWorkflowStoreEnabledCondition implements Condition {

  /** Constructs a new <b>InternalWorkflowStoreEnabledCondition</b>. */
  public InternalWorkflowStoreEnabledCondition() {}

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String workflowStoreType =
        context.getEnvironment().getProperty("inception.operations.workflow-store-type");

    return (workflowStoreType == null) || "internal".equals(workflowStoreType);
  }
}
