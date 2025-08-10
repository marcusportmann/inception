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
 * The {@code InteractionSourcePermissionType} enumeration defines the possible interaction source
 * permission types.
 *
 * @author Marcus Portmann
 */
@Schema(description = "The interaction source permission type")
@XmlEnum
@XmlType(
    name = "InteractionSourcePermissionType",
    namespace = "https://inception.digital/operations")
public enum InteractionSourcePermissionType implements CodeEnum {

  /** Assign Interaction. */
  @XmlEnumValue("AssignInteraction")
  ASSIGN_INTERACTION("assign_interaction", "Assign Interaction"),

  /** Claim Interaction. */
  @XmlEnumValue("ClaimInteraction")
  CLAIM_INTERACTIONS("claim_interaction", "Claim Interaction"),

  /** Delete Interaction. */
  @XmlEnumValue("DeleteInteraction")
  DELETE_INTERACTIONS("delete_interaction", "Delete Interaction"),

  /** Edit Interaction. */
  @XmlEnumValue("EditInteraction")
  EDIT_INTERACTIONS("edit_interaction", "Edit Interaction"),

  /** Read Interaction. */
  @XmlEnumValue("ReadInteraction")
  READ_INTERACTION("read_interaction", "Read Interaction"),

  /** Transfer Interaction. */
  @XmlEnumValue("TransferInteraction")
  TRANSFER_INTERACTION("transfer_interaction", "Transfer Interaction");

  private final String code;

  private final String description;

  InteractionSourcePermissionType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  /**
   * Returns the code for the interaction source permission type.
   *
   * @return the code for the interaction source permission type
   */
  public String code() {
    return code;
  }

  /**
   * Returns the description for the interaction source permission type.
   *
   * @return the description for the interaction source permission type
   */
  public String description() {
    return description;
  }
}
