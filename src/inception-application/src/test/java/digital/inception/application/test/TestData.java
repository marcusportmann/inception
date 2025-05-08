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

package digital.inception.application.test;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

/**
 * The {@code TestData} class.
 *
 * @author Marcus Portmann
 */
@Entity
@Table(name = "test_data")
public class TestData implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The ID. */
  @Id
  @Column(name = "id", nullable = false)
  private String id;

  /** The name. */
  @Column(name = "name", nullable = false)
  private String name;

  /** The value. */
  @Column(name = "value", nullable = false)
  private String value;

  /** Creates a new {@code TestData} instance. */
  @SuppressWarnings("unused")
  TestData() {}

  /**
   * Creates a new {@code TestData} instance.
   *
   * @param id the ID
   * @param name the name
   * @param value the value
   */
  TestData(String id, String name, String value) {
    this.id = id;
    this.name = name;
    this.value = value;
  }

  /**
   * Indicates whether some other object is "equal to" this one.
   *
   * @param object the reference object with which to compare
   * @return {@code true} if this object is the same as the object argument, otherwise {@code false}
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

    TestData other = (TestData) object;

    return Objects.equals(id, other.id);
  }

  /**
   * Returns a hash code value for the object.
   *
   * @return a hash code value for the object
   */
  @Override
  public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
  }

  /**
   * Returns the ID.
   *
   * @return the ID
   */
  String getId() {
    return id;
  }

  /**
   * Returns the name.
   *
   * @return the name
   */
  String getName() {
    return name;
  }

  /**
   * Returns the value.
   *
   * @return the value
   */
  String getValue() {
    return value;
  }
}
