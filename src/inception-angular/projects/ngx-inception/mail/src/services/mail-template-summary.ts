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

import {MailTemplateContentType} from './mail-template-content-type';

/**
 * The MailTemplateSummary class holds the summary information for a mail template.
 *
 * @author Marcus Portmann
 */
export class MailTemplateSummary {

  /**
   * The content type for the mail template.
   */
  contentType: MailTemplateContentType;

  /**
   * The ID for the mail template.
   */
  id: string;

  /**
   * The name of the mail template.
   */
  name: string;

  /**
   * Constructs a new MailTemplateSummary.
   *
   * @param id          The ID for the mail template.
   * @param name        The name of the mail template.
   * @param contentType The content type for the mail template.
   */
  constructor(id: string, name: string, contentType: MailTemplateContentType) {
    this.id = id;
    this.name = name;
    this.contentType = contentType;
  }
}
