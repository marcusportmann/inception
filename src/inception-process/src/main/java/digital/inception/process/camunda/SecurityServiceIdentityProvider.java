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

import digital.inception.security.ISecurityService;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.identity.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>SecurityServiceIdentityProvider</code> class.
 *
 * @author Marcus Portmann
 */
public class SecurityServiceIdentityProvider
  implements ReadOnlyIdentityProvider
{
  /**
   * The Security Service.
   */
  private ISecurityService securityService;


  SecurityServiceIdentityProvider(ISecurityService securityService)
  {
    this.securityService = securityService;
  }


  @Override
  public User findUserById(String userId)
  {
    return null;
  }

  @Override
  public UserQuery createUserQuery()
  {
    return null;
  }

  @Override
  public UserQuery createUserQuery(
    CommandContext commandContext)
  {
    return null;
  }

  @Override
  public NativeUserQuery createNativeUserQuery()
  {
    return null;
  }

  @Override
  public boolean checkPassword(String userId, String password)
  {
    return false;
  }

  @Override
  public Group findGroupById(String groupId)
  {
    return null;
  }

  @Override
  public GroupQuery createGroupQuery()
  {
    return null;
  }

  @Override
  public GroupQuery createGroupQuery(
    CommandContext commandContext)
  {
    return null;
  }

  @Override
  public Tenant findTenantById(String tenantId)
  {
    return null;
  }

  @Override
  public TenantQuery createTenantQuery()
  {
    return null;
  }

  @Override
  public TenantQuery createTenantQuery(
    CommandContext commandContext)
  {
    return null;
  }

  @Override
  public void flush()
  {
    // Do nothing
  }

  @Override
  public void close()
  {
    // Do nothing
  }

  public long findUserCountByQueryCriteria(SecurityServiceUserQuery query) {
    return 0;
  }

  public long findGroupCountByQueryCriteria(SecurityServiceGroupQuery query) {
    return 0;
  }


  public List<User> findUsersByQueryCriteria(SecurityServiceUserQuery query, Page page) {
    return new ArrayList<>();
  }

  public List<Group> findGroupsByQueryCriteria(SecurityServiceGroupQuery query, Page page) {
    return new ArrayList<>();
  }

}
