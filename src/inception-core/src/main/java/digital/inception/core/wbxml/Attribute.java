/*
 * Copyright 2020 Marcus Portmann
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

package digital.inception.core.wbxml;

// ~--- JDK imports ------------------------------------------------------------

import java.io.Serializable;

/**
 * The <code>Attribute</code> class stores the name and value of a WBXML attribute.
 *
 * @author Marcus Portmann
 */
public class Attribute implements Serializable {

  private static final long serialVersionUID = 1000000;

  private String name;

  private String value;

  /** Constructs a new <code>Attribute</code>. */
  public Attribute() {
    name = "";
    value = "";
  }

  /**
   * Constructs a new <code>Attribute</code> with the specified name and value.
   *
   * @param name the name of the attribute
   * @param value the value for the attribute
   */
  public Attribute(String name, String value) {
    this.name = name;
    this.value = value;
  }

  /**
   * Returns the name of the attribute.
   *
   * @return the name of the attribute
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the value of the attribute.
   *
   * @return the value of the attribute
   */
  public String getValue() {
    return value;
  }

  /**
   * Set the name of the attribute.
   *
   * @param name the name of the attribute
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Set the value of the attribute.
   *
   * @param value the value of the attribute
   */
  public void setValue(String value) {
    this.value = value;
  }
}
