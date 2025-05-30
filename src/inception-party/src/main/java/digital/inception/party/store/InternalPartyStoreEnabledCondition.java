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

package digital.inception.party.store;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

/**
 * The {@code InternalPartyStoreEnabledCondition} class implements the condition that must be
 * matched to enable the internal party store.
 *
 * @author Marcus Portmann
 */
public class InternalPartyStoreEnabledCondition implements Condition {

  /** Constructs a new {@code InternalPartyStoreEnabledCondition}. */
  public InternalPartyStoreEnabledCondition() {}

  @Override
  public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
    String policyStoreType =
        context.getEnvironment().getProperty("inception.party.party-store-type");

    return (policyStoreType == null) || "internal".equals(policyStoreType);
  }
}
