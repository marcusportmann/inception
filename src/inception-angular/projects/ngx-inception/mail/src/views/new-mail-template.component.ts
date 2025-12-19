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

import { AfterViewInit, Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {
  AdminContainerView, BackNavigation, Base64, CoreModule, FileValidator, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { MailTemplate } from '../services/mail-template';
import { MailTemplateContentType } from '../services/mail-template-content-type';
import { MailService } from '../services/mail.service';

/**
 * The NewMailTemplateComponent class implements the new mail template component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-mail-new-mail-template',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-mail-template.component.html',
  styleUrls: ['new-mail-template.component.css']
})
export class NewMailTemplateComponent extends AdminContainerView implements AfterViewInit {
  // noinspection JSUnusedGlobalSymbols
  MailTemplateContentType = MailTemplateContentType;

  contentTypeControl: FormControl;

  contentTypes: MailTemplateContentType[] = [
    MailTemplateContentType.Text,
    MailTemplateContentType.HTML
  ];

  getMailTemplateContentTypeDescription = MailService.getMailTemplateContentTypeDescription;

  idControl: FormControl;

  mailTemplate: MailTemplate | null = null;

  nameControl: FormControl;

  newMailTemplateForm: FormGroup;

  templateControl: FormControl;

  readonly title = $localize`:@@mail_new_mail_template_title:New Mail Template`;

  private mailService = inject(MailService);

  constructor() {
    super();

    // Initialize the form controls
    this.contentTypeControl = new FormControl('', [Validators.required]);
    this.idControl = new FormControl('', [Validators.required]);
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.templateControl = new FormControl('', [
      Validators.required,
      FileValidator.minSize(1),
      FileValidator.maxSize(MailService.MAX_TEMPLATE_SIZE)
    ]);

    // Initialize the form
    this.newMailTemplateForm = new FormGroup({
      contentType: this.contentTypeControl,
      id: this.idControl,
      name: this.nameControl,
      template: this.templateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@mail_new_mail_template_back_navigation:Mail Templates`,
      ['..'],
      { relativeTo: this.activatedRoute }
    );
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.mailTemplate = new MailTemplate('', '', MailTemplateContentType.Text, '');
  }

  ok(): void {
    if (!this.mailTemplate || !this.newMailTemplateForm.valid) {
      return;
    }

    const files = this.templateControl.value as File[] | null;

    if (!files || !files[0]) {
      console.log('No template file selected for the mail template.');
      return;
    }

    const mailTemplate = this.mailTemplate;
    const fileReader: FileReader = new FileReader();

    fileReader.onloadend = () => {
      const result = fileReader.result;

      if (!(result instanceof ArrayBuffer)) {
        console.log('Failed to read the template file for the mail template (' + result + ')');
        return;
      }

      const base64: string = Base64.encode(result as ArrayBuffer);

      mailTemplate.id = this.idControl.value;
      mailTemplate.name = this.nameControl.value;
      mailTemplate.contentType = this.contentTypeControl.value;
      mailTemplate.template = base64;

      this.spinnerService.showSpinner();

      this.mailService
        .createMailTemplate(mailTemplate)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['..'], {
              relativeTo: this.activatedRoute
            });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    };

    fileReader.readAsArrayBuffer(files[0]);
  }
}
