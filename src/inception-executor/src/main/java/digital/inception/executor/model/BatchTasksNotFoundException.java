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

package digital.inception.executor.model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;
import digital.inception.core.service.Problem;
import digital.inception.core.service.ServiceException;

/**
 * The <b>BatchTasksNotFoundException</b> exception is thrown to indicate an error condition as a
 * result of no tasks being found for a batch.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/executor/batch-tasks-not-found",
    title = "No tasks found for the batch.",
    status = 404)
@WebFault(
    name = "BatchTasksNotFoundException",
    targetNamespace = "https://inception.digital/executor",
    faultBean = "digital.inception.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class BatchTasksNotFoundException extends ServiceException {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>BatchTasksNotFoundException</b>.
   *
   * @param batchId the ID for the batch
   */
  public BatchTasksNotFoundException(String batchId) {
    super("No tasks found for the batch (" + batchId + ")");
  }
}
