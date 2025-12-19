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

package digital.inception.processor.test.model;

import digital.inception.processor.AbstractProcessableObject;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * The Report domain object used for tests.
 *
 * @author Marcus Portmann
 */
@Document("reports")
@Entity
@Table(name = "reports")
public class Report extends AbstractProcessableObject<UUID, ReportStatus> implements Serializable {

  @Serial private static final long serialVersionUID = 1000000;

  @org.springframework.data.annotation.Id @jakarta.persistence.Id private UUID id;

  public Report() {
    super(ReportStatus.REQUESTED);

    this.id = UUID.randomUUID();
  }

  @Override
  public UUID getId() {
    return id;
  }

  @Override
  public String getIdAsKey() {
    if (id != null) {
      return id.toString();
    } else {
      return "";
    }
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
