/*
 * Copyright 2019 Marcus Portmann
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

package digital.inception.process.camunda;

//~--- non-JDK imports --------------------------------------------------------

import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.GroupQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.interceptor.CommandExecutor;

//~--- JDK imports ------------------------------------------------------------

import java.util.List;

/**
 * The <code>SecurityServiceGroupQuery</code> class.
 *
 * @author Marcus Portmann
 */
public class SecurityServiceGroupQuery extends GroupQueryImpl
{
  private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <code>SecurityServiceGroupQuery</code>.
   */
  public SecurityServiceGroupQuery()
  {
    super();
  }

  /**
   * Constructs a new <code>SecurityServiceGroupQuery</code>.
   *
   * @param commandExecutor the command executor
   */
  public SecurityServiceGroupQuery(CommandExecutor commandExecutor)
  {
    super(commandExecutor);
  }

  @Override
  public long executeCount(CommandContext commandContext)
  {
    final SecurityServiceIdentityProvider securityServiceIdentityProvider =
        getSecurityServiceIdentityProvider(commandContext);

    return securityServiceIdentityProvider.findGroupCountByQueryCriteria(this);
  }

  @Override
  public List<Group> executeList(CommandContext commandContext, Page page)
  {
    final SecurityServiceIdentityProvider securityServiceIdentityProvider =
        getSecurityServiceIdentityProvider(commandContext);

    return securityServiceIdentityProvider.findGroupsByQueryCriteria(this, page);
  }

  protected SecurityServiceIdentityProvider getSecurityServiceIdentityProvider(
      CommandContext commandContext)
  {
    return (SecurityServiceIdentityProvider) commandContext.getReadOnlyIdentityProvider();
  }
}
