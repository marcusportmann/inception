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
  AdminContainerView, BackNavigation, Base64, CoreModule, Error, FileUploadComponent, FileValidator,
  ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { MailTemplate } from '../services/mail-template';
import { MailTemplateContentType } from '../services/mail-template-content-type';
import { MailService } from '../services/mail.service';

/**
 * The EditMailTemplateComponent class implements the edit mail template component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-mail-edit-mail-template',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective, FileUploadComponent],
  templateUrl: 'edit-mail-template.component.html',
  styleUrls: ['edit-mail-template.component.css']
})
export class EditMailTemplateComponent extends AdminContainerView implements AfterViewInit {
  private mailService = inject(MailService);

  // noinspection JSUnusedGlobalSymbols
  MailTemplateContentType = MailTemplateContentType;

  contentTypeControl: FormControl;

  contentTypes: MailTemplateContentType[] = [
    MailTemplateContentType.Text,
    MailTemplateContentType.HTML
  ];

  editMailTemplateForm: FormGroup;

  getMailTemplateContentTypeDescription = MailService.getMailTemplateContentTypeDescription;

  idControl: FormControl;

  mailTemplate: MailTemplate | null = null;

  mailTemplateId: string;

  nameControl: FormControl;

  templateControl: FormControl;

  readonly title = $localize`:@@mail_edit_mail_template_title:Edit Mail Template`;

  constructor() {
    super();

    // Retrieve the route parameters
    const mailTemplateId = this.activatedRoute.snapshot.paramMap.get('mailTemplateId');

    if (!mailTemplateId) {
      throw new Error('No mailTemplateId route parameter found');
    }

    this.mailTemplateId = decodeURIComponent(mailTemplateId);

    // Initialize the form controls
    this.contentTypeControl = new FormControl('', [Validators.required]);
    this.idControl = new FormControl(
      {
        value: '',
        disabled: true
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.templateControl = new FormControl('', [
      Validators.required,
      FileValidator.minSize(1),
      FileValidator.maxSize(MailService.MAX_TEMPLATE_SIZE)
    ]);

    // Initialize the form
    this.editMailTemplateForm = new FormGroup({
      contentType: this.contentTypeControl,
      id: this.idControl,
      name: this.nameControl,
      template: this.templateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@mail_edit_mail_template_back_navigation:Mail Templates`,
      ['../..'],
      { relativeTo: this.activatedRoute }
    );
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing mail template and initialize the form controls
    this.spinnerService.showSpinner();

    this.mailService
      .getMailTemplate(this.mailTemplateId)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (mailTemplate: MailTemplate) => {
          this.mailTemplate = mailTemplate;
          this.idControl.setValue(mailTemplate.id);
          this.nameControl.setValue(mailTemplate.name);
          this.contentTypeControl.setValue(mailTemplate.contentType);
        },
        error: (error: Error) => this.handleError(error, true, '../..')
      });
  }

  ok(): void {
    if (!this.mailTemplate || !this.editMailTemplateForm.valid) {
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

      mailTemplate.name = this.nameControl.value;
      mailTemplate.contentType = this.contentTypeControl.value;
      mailTemplate.template = base64;

      this.spinnerService.showSpinner();

      this.mailService
        .updateMailTemplate(mailTemplate)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: () => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {
              relativeTo: this.activatedRoute
            });
          },
          error: (error: Error) => this.handleError(error, false)
        });
    };

    fileReader.readAsArrayBuffer(files[0]);
  }
}
