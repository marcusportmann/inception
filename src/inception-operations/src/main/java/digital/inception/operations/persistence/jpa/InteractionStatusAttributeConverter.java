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

package digital.inception.operations.persistence.jpa;

import digital.inception.jpa.AbstractCodeEnumAttributeConverter;
import digital.inception.operations.model.InteractionStatus;
import jakarta.persistence.Converter;

/**
 * The {@code InteractionStatusAttributeConverter} class implements the custom JPA attribute
 * converter for the {@code InteractionStatusAttribute} enumeration.
 *
 * @author Marcus Portmann
 */
@Converter(autoApply = true)
public class InteractionStatusAttributeConverter
    extends AbstractCodeEnumAttributeConverter<InteractionStatus> {

  /** Constructs a new {@code InteractionStatusAttributeConverter}. */
  public InteractionStatusAttributeConverter() {
    super(InteractionStatus.class);
  }
}
