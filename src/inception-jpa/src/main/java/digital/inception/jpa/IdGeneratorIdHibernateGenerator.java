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

package digital.inception.jpa;

import digital.inception.core.jdbc.IdGenerator;
import java.lang.reflect.Member;
import java.sql.SQLException;
import java.util.EnumSet;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.AnnotationBasedGenerator;
import org.hibernate.generator.BeforeExecutionGenerator;
import org.hibernate.generator.EventType;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.resource.beans.spi.ManagedBean;
import org.hibernate.resource.beans.spi.ManagedBeanRegistry;

/**
 * Hibernate {@link BeforeExecutionGenerator} used with {@link
 * org.hibernate.annotations.IdGeneratorType} (via the {@link IdGeneratorId} meta-annotation) to
 * implement an “assigned-or-generated” identifier strategy.
 *
 * <h2>Behaviour</h2>
 *
 * <ul>
 *   <li>If the identifier is already populated (i.e. {@code currentValue != null}) when an {@link
 *       EventType#INSERT} occurs, the existing value is preserved and no allocation is performed.
 *   <li>If the identifier is {@code null} on insert, a new identifier is allocated by invoking
 *       {@link IdGenerator#nextId(String)} using the logical key supplied by {@link
 *       IdGeneratorId#name()}.
 * </ul>
 *
 * <p>The allocation algorithm is implemented by {@link IdGenerator}, which typically uses a
 * dedicated single-row-per-key table (for example {@code idgenerator}) and pessimistic locking to
 * produce monotonically increasing values per logical key.
 *
 * <h2>Lifecycle and container integration</h2>
 *
 * <p>Hibernate may instantiate generator classes reflectively during metadata/bootstrap. For this
 * reason the class provides a public no-argument constructor and implements {@link
 * AnnotationBasedGenerator} so Hibernate can configure it afterwards via {@link
 * #initialize(IdGeneratorId, Member, GeneratorCreationContext)}.
 *
 * <p>The {@link IdGenerator} dependency is obtained from Hibernate’s {@link ManagedBeanRegistry},
 * which delegates to the configured {@code BeanContainer} (for example Spring’s integration) when
 * container integration is enabled.
 *
 * <h2>Event types</h2>
 *
 * <p>This generator participates only in {@link EventType#INSERT} events and is not invoked for
 * updates.
 *
 * <h2>Error handling</h2>
 *
 * <p>{@link SQLException} instances thrown by {@link IdGenerator#nextId(String)} are wrapped in a
 * {@link HibernateException}.
 *
 * @author Marcus Portmann
 */
public class IdGeneratorIdHibernateGenerator
    implements BeforeExecutionGenerator, AnnotationBasedGenerator<IdGeneratorId> {

  /**
   * Container-managed handle to the {@link IdGenerator} used to allocate identifiers. Populated
   * during {@link #initialize(IdGeneratorId, Member, GeneratorCreationContext)}.
   */
  private ManagedBean<IdGenerator> idGeneratorBean;

  /**
   * Logical key used by {@link IdGenerator} to allocate identifiers. Derived from {@link
   * IdGeneratorId#name()} during {@link #initialize(IdGeneratorId, Member,
   * GeneratorCreationContext)}.
   */
  private String name;

  /**
   * Public no-argument constructor required for bootstrap paths where Hibernate instantiates
   * generators reflectively prior to calling {@link #initialize(IdGeneratorId, Member,
   * GeneratorCreationContext)}.
   */
  public IdGeneratorIdHibernateGenerator() {}

  /**
   * Generates (or preserves) the identifier value for an {@link EventType#INSERT} event.
   *
   * <p>If {@code currentValue} is non-null, it is treated as application-assigned and returned
   * unchanged. Otherwise, this method allocates the next identifier by calling {@link
   * IdGenerator#nextId(String)} using the allocation key configured via {@link
   * IdGeneratorId#name()}.
   *
   * @param session the Hibernate session
   * @param owner the entity instance being inserted
   * @param currentValue the current identifier value (may be {@code null})
   * @param eventType the triggering event type (expected to be {@link EventType#INSERT})
   * @return the identifier value to use for the insert
   * @throws HibernateException if allocation fails due to an underlying {@link SQLException}
   */
  @Override
  public Object generate(
      SharedSessionContractImplementor session,
      Object owner,
      Object currentValue,
      EventType eventType) {

    if (currentValue != null) {
      return currentValue;
    }

    try {
      return idGeneratorBean.getBeanInstance().nextId(name);
    } catch (SQLException e) {
      throw new HibernateException("Failed to generate id for name=" + name, e);
    }
  }

  /**
   * Declares the Hibernate event types for which this generator should be invoked.
   *
   * @return an {@link EnumSet} containing only {@link EventType#INSERT}
   */
  @Override
  public EnumSet<EventType> getEventTypes() {
    return EnumSet.of(EventType.INSERT);
  }

  /**
   * Initializes this generator instance from the {@link IdGeneratorId} annotation applied to the
   * identifier attribute.
   *
   * <p>This method is invoked by Hibernate after reflective construction, supplying the annotation
   * instance, the mapped member (field or getter), and the generator creation context.
   *
   * @param annotation the {@link IdGeneratorId} annotation from the identifier attribute; its
   *     {@link IdGeneratorId#name()} value is used as the allocation key
   * @param member the mapped identifier member (field or getter) on which {@code annotation} is
   *     declared
   * @param context the generator creation context providing access to Hibernate services
   * @throws IllegalStateException if the {@link ManagedBeanRegistry} is not available (i.e. bean
   *     container integration is not configured)
   */
  @Override
  public void initialize(
      IdGeneratorId annotation, Member member, GeneratorCreationContext context) {
    this.name = annotation.name();

    ManagedBeanRegistry beanRegistry =
        context.getServiceRegistry().getService(ManagedBeanRegistry.class);

    if (beanRegistry == null) {
      throw new IllegalStateException(
          "ManagedBeanRegistry not available. Ensure Hibernate BeanContainer is configured.");
    }

    this.idGeneratorBean = beanRegistry.getBean(IdGenerator.class);
  }
}
