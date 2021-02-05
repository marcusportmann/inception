/*
 * Copyright 2021 Marcus Portmann
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

package digital.inception.banking.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import digital.inception.party.PartyBase;
import digital.inception.party.PartyType;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

/**
 * The <b>CustomerBase</b> class holds the information common to all types of parties and is the
 * base class that the classes for the different party types are derived from, e.g. <b>Person</b>.
 *
 * <p>This class and its subclasses expose the JSON and XML properties using a property-based
 * approach rather than a field-based approach to support the JPA inheritance model.
 *
 * @author Marcus Portmann
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlTransient
@Entity
@Table(schema = "customer", name = "customers")
public class CustomerBase extends PartyBase implements Serializable {

  private static final long serialVersionUID = 1000000;

  /** The customer type. */
  @NotNull
  @Column(name = "type", length = 30, nullable = false)
  private CustomerType customerType;

  /** Constructs a new <b>CustomerBase</b>. */
  public CustomerBase() {}

  /**
   * Constructs a new <b>CustomerBase</b>.
   *
   * @param partyType the party type for the customer
   * @param customerType the customer type for the customer
   */
  protected CustomerBase(PartyType partyType, CustomerType customerType) {
    super(partyType);

    this.customerType = customerType;
  }

  /**
   * Constructs a new <b>CustomerBase</b>.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the customer is
   *     associated with
   * @param partyType the party type for the customer
   * @param customerType the customer type for the customer
   * @param name the name of the customer
   */
  public CustomerBase(UUID tenantId, PartyType partyType, CustomerType customerType, String name) {
    super(tenantId, partyType, name);

    this.customerType = customerType;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return <b>true</b> if this object is the same as the object argument otherwise <b>false</b>
   */
  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    CustomerBase other = (CustomerBase) object;

    return Objects.equals(getId(), other.getId());
  }

  /**
   * Returns the date and time the customer was created.
   *
   * @return the date and time the customer was created
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getCreated() {
    return super.getCreated();
  }

  /**
   * Returns the customer type for the customer.
   *
   * @return the customer type for the customer
   */
  @JsonIgnore
  @XmlTransient
  public CustomerType getCustomerType() {
    return customerType;
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the customer.
   *
   * @return the Universally Unique Identifier (UUID) for the customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public UUID getId() {
    return super.getId();
  }

  /**
   * Returns the name of the customer.
   *
   * @return the name of the customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public String getName() {
    return super.getName();
  }

  /**
   * Returns the party type for the customer.
   *
   * @return the party type for the customer
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public PartyType getPartyType() {
    return super.getPartyType();
  }

  /**
   * Returns the Universally Unique Identifier (UUID) for the tenant the customer is associated
   * with.
   *
   * @return the Universally Unique Identifier (UUID) for the tenant the customer is associated with
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public UUID getTenantId() {
    return super.getTenantId();
  }

  /**
   * Returns the date and time the customer was last updated.
   *
   * @return the date and time the customer was last updated
   */
  @JsonIgnore
  @XmlTransient
  @Override
  public LocalDateTime getUpdated() {
    return super.getUpdated();
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return ((getId() == null) ? 0 : getId().hashCode());
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the customer.
   *
   * @param id the Universally Unique Identifier (UUID) for the customer
   */
  public void setId(UUID id) {
    super.setId(id);
  }

  /**
   * Set the name of the customer.
   *
   * @param name the name of the customer
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

  /**
   * Set the Universally Unique Identifier (UUID) for the tenant the customer is associated with.
   *
   * @param tenantId the Universally Unique Identifier (UUID) for the tenant the customer is
   *     associated with
   */
  @Override
  public void setTenantId(UUID tenantId) {
    super.setTenantId(tenantId);
  }
}
