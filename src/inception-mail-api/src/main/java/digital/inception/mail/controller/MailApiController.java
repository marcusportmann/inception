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

package digital.inception.mail.controller;

import digital.inception.api.ApiUtil;
import digital.inception.api.SecureApiController;
import digital.inception.core.service.InvalidArgumentException;
import digital.inception.core.service.ServiceUnavailableException;
import digital.inception.mail.model.DuplicateMailTemplateException;
import digital.inception.mail.model.MailTemplate;
import digital.inception.mail.model.MailTemplateNotFoundException;
import digital.inception.mail.model.MailTemplateSummary;
import digital.inception.mail.service.IMailService;
import java.util.List;
import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

/**
 * The <b>MailApiController</b> class.
 *
 * @author Marcus Portmann
 */
@RestController
@CrossOrigin
@SuppressWarnings({"unused"})
public class MailApiController extends SecureApiController implements IMailApiController {

  /** The Mail Service. */
  private final IMailService mailService;

  /**
   * Constructs a new <b>MailApiController</b>.
   *
   * @param applicationContext the Spring application context
   * @param mailService the Mail Service
   */
  public MailApiController(ApplicationContext applicationContext, IMailService mailService) {
    super(applicationContext);

    this.mailService = mailService;
  }

  @Override
  public void createMailTemplate(MailTemplate mailTemplate)
      throws InvalidArgumentException, DuplicateMailTemplateException, ServiceUnavailableException {
    mailService.createMailTemplate(mailTemplate);
  }

  @Override
  public void deleteMailTemplate(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    mailService.deleteMailTemplate(mailTemplateId);
  }

  @Override
  public MailTemplate getMailTemplate(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    return mailService.getMailTemplate(mailTemplateId);
  }

  @Override
  public String getMailTemplateName(String mailTemplateId)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    return ApiUtil.quote(mailService.getMailTemplateName(mailTemplateId));
  }

  @Override
  public List<MailTemplateSummary> getMailTemplateSummaries() throws ServiceUnavailableException {
    return mailService.getMailTemplateSummaries();
  }

  @Override
  public List<MailTemplate> getMailTemplates() throws ServiceUnavailableException {
    return mailService.getMailTemplates();
  }

  //  @Override
  //  public void sendMailTest()
  //      throws InvalidArgumentException, DuplicateMailTemplateException,
  //          MailTemplateNotFoundException, ServiceUnavailableException {
  //    MailTemplate mailTemplate = new MailTemplate();
  //    mailTemplate.setId("TestMailTemplate");
  //    mailTemplate.setName("Test Mail Template");
  //    mailTemplate.setContentType(MailTemplateContentType.HTML);
  //    mailTemplate.setTemplate("Hello World!".getBytes());
  //
  //    mailService.createMailTemplate(mailTemplate);
  //
  //    MailTemplate retrievedMailTemplate = mailService.getMailTemplate(mailTemplate.getId());
  //
  //    logger.info(
  //        "Retrieved mail template ("
  //            + retrievedMailTemplate.getName()
  //            + ") with ID ("
  //            + retrievedMailTemplate.getId()
  //            + ")");
  //  }

  @Override
  public void updateMailTemplate(String mailTemplateId, MailTemplate mailTemplate)
      throws InvalidArgumentException, MailTemplateNotFoundException, ServiceUnavailableException {
    if (!StringUtils.hasText(mailTemplateId)) {
      throw new InvalidArgumentException("mailTemplateId");
    }

    if (mailTemplate == null) {
      throw new InvalidArgumentException("mailTemplate");
    }

    if (!mailTemplateId.equals(mailTemplate.getId())) {
      throw new InvalidArgumentException("mailTemplate");
    }

    mailService.updateMailTemplate(mailTemplate);
  }
}
