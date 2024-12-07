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

package digital.inception.mongodb.test;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The <b>TestDocument</b> class.
 *
 * @author Marcus Portmann
 */
@Document(collection = "test_document")
@TypeAlias("TestDocument")
@SuppressWarnings("unused")
public class TestDocument implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  /** The age for the test document. */
  private int age;

  /** The ID for the test document. */
  @Id private String id;

  /** The name for the test document. */
  private String name;

  /** Constructs a new <b>TestDocument</b>. */
  public TestDocument() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TestDocument that = (TestDocument) o;

    if (age != that.age) return false;
    if (!Objects.equals(id, that.id)) return false;
    return Objects.equals(name, that.name);
  }

  /**
   * Returns the age for the test document.
   *
   * @return the age for the test document
   */
  public int getAge() {
    return age;
  }

  /**
   * Returns the ID for the test document.
   *
   * @return the ID for the test document
   */
  public String getId() {
    return id;
  }

  /**
   * Returns the name for the test document.
   *
   * @return the name for the test document
   */
  public String getName() {
    return name;
  }

  @Override
  public int hashCode() {
    int result = id != null ? id.hashCode() : 0;
    result = 31 * result + (name != null ? name.hashCode() : 0);
    result = 31 * result + age;
    return result;
  }

  /**
   * Set the age for the test document.
   *
   * @param age the age for the test document
   */
  public void setAge(int age) {
    this.age = age;
  }

  /**
   * Set the ID for the test document.
   *
   * @param id the ID for the test document
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set the name for the test document.
   *
   * @param name the name for the test document
   */
  public void setName(String name) {
    this.name = name;
  }
}
