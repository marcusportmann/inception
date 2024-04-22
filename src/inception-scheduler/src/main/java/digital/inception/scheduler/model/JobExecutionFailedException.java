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

package digital.inception.scheduler.model;

import digital.inception.core.service.Problem;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.ws.WebFault;
import java.io.Serial;

/**
 * The <b>JobExecutionFailedException</b> exception is thrown to indicate an error condition when
 * executing a job.
 *
 * <p>This is a checked exception to prevent the automatic rollback of the current transaction.
 *
 * @author Marcus Portmann
 */
@Problem(
    type = "https://inception.digital/problems/scheduler/job-execution-failed",
    title = "The job execution failed.",
    status = 500)
@WebFault(
    name = "JobExecutionFailedException",
    targetNamespace = "http://inception.digital/scheduler",
    faultBean = "za.co.discovery.nova.core.service.ServiceError")
@XmlAccessorType(XmlAccessType.PROPERTY)
public class JobExecutionFailedException extends Exception {

  @Serial private static final long serialVersionUID = 1000000;

  /**
   * Constructs a new <b>JobExecutionFailedException</b>.
   *
   * @param jobId the ID for the job
   */
  public JobExecutionFailedException(String jobId) {
    super("The job (" + jobId + ") execution failed");
  }

  /**
   * Constructs a new <b>JobExecutionFailedException</b>.
   *
   * @param jobId the ID for the job
   * @param message The message saved for later retrieval by the <b>getMessage()</b> method.
   */
  public JobExecutionFailedException(String jobId, String message) {
    super("The job (" + jobId + ") execution failed: " + message);
  }

  /**
   * Constructs a new <b>JobExecutionFailedException</b>.
   *
   * @param jobId the ID for the job
   * @param cause The cause saved for later retrieval by the <b>getCause()</b> method. (A
   *     <b>null</b> value is permitted if the cause is nonexistent or unknown)
   */
  public JobExecutionFailedException(String jobId, Throwable cause) {
    super("The job (" + jobId + ") execution failed: " + cause.getMessage(), cause);
  }
}
