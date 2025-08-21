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

package digital.inception.operations.model;

import digital.inception.core.model.CodeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.xml.bind.annotation.XmlEnum;
import jakarta.xml.bind.annotation.XmlEnumValue;
import jakarta.xml.bind.annotation.XmlType;

/**
 * The {@code InteractionPermissionType} enumeration defines the possible interaction permission
 * types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction permission type")
@XmlEnum
@XmlType(name = "InteractionPermissionType", namespace = "https://inception.digital/operations")
public enum InteractionPermissionType implements CodeEnum {

  /** Assign Interaction. */
  @XmlEnumValue("AssignInteraction")
  ASSIGN_INTERACTION("assign_interaction", "Assign Interaction"),

  /** Create Interaction. */
  @XmlEnumValue("CreateInteraction")
  CREATE_INTERACTION("create_interaction", "Create Interaction"),

  /** Delete Interaction. */
  @XmlEnumValue("DeleteInteraction")
  DELETE_INTERACTION("delete_interaction", "Delete Interaction"),

  /** Retrieve Interaction. */
  @XmlEnumValue("RetrieveInteraction")
  RETRIEVE_INTERACTION("retrieve_interaction", "Retrieve Interaction"),

  /** Transfer Interaction. */
  @XmlEnumValue("TransferInteraction")
  TRANSFER_INTERACTION("transfer_interaction", "Transfer Interaction"),

  /** Update Interaction. */
  @XmlEnumValue("UpdateInteraction")
  UPDATE_INTERACTION("update_interaction", "Update Interaction");

  private final String code;

  private final String description;

  InteractionPermissionType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the interaction permission type.
   *
   * @return the code for the interaction permission type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction permission type.
   *
   * @return the description for the interaction permission type
   */
  public String description() {
    return description;
  }
}
