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

import {AfterViewInit, Component} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, BackNavigation, Base64, DialogService, Error,
  FileValidator, InvalidArgumentError, ServiceUnavailableError, SpinnerService
} from 'ngx-inception/core';
import {finalize, first} from 'rxjs/operators';
import {MailTemplate} from '../services/mail-template';
import {MailTemplateContentType} from '../services/mail-template-content-type';
import {MailService} from '../services/mail.service';

/**
 * The EditMailTemplateComponent class implements the edit mail template component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-mail-template.component.html',
  styleUrls: ['edit-mail-template.component.css'],
  standalone: false
})
export class EditMailTemplateComponent extends AdminContainerView implements AfterViewInit {

  MailTemplateContentType = MailTemplateContentType;

  contentTypeControl: FormControl;

  contentTypes: MailTemplateContentType[] = [
    MailTemplateContentType.Text,
    MailTemplateContentType.HTML];

  editMailTemplateForm: FormGroup;

  getMailTemplateContentTypeDescription = MailService.getMailTemplateContentTypeDescription;

  idControl: FormControl;

  mailTemplate: MailTemplate | null = null;

  mailTemplateId: string;

  nameControl: FormControl;

  templateControl: FormControl;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private mailService: MailService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const mailTemplateId = this.activatedRoute.snapshot.paramMap.get('mailTemplateId');

    if (!mailTemplateId) {
      throw (new Error('No mailTemplateId route parameter found'));
    }

    this.mailTemplateId = decodeURIComponent(mailTemplateId);

    // Initialise the form controls
    this.contentTypeControl = new FormControl('', [Validators.required]);
    this.idControl = new FormControl({
      value: '',
      disabled: true
    }, [Validators.required, Validators.maxLength(100)]);
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.templateControl = new FormControl('',
      [
        Validators.required, FileValidator.minSize(1), FileValidator.maxSize(
        MailService.MAX_TEMPLATE_SIZE)
      ]);

    // Initialise the form
    this.editMailTemplateForm = new FormGroup({
      contentType: this.contentTypeControl,
      id: this.idControl,
      name: this.nameControl,
      template: this.templateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@mail_edit_mail_template_back_navigation:Mail Templates`,
      ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return $localize`:@@mail_edit_mail_template_title:Edit Mail Template`
  }

  cancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    // Retrieve the existing mail template and initialise the form controls
    this.spinnerService.showSpinner();

    this.mailService.getMailTemplate(this.mailTemplateId)
    .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
    .subscribe((mailTemplate: MailTemplate) => {
      this.mailTemplate = mailTemplate;
      this.idControl.setValue(mailTemplate.id);
      this.nameControl.setValue(mailTemplate.name);
      this.contentTypeControl.setValue(mailTemplate.contentType);
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) ||
        (error instanceof ServiceUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error).afterClosed()
        .pipe(first())
        .subscribe(() => {
          this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
        });
      }
    });
  }

  ok(): void {
    if (this.mailTemplate && this.editMailTemplateForm.valid) {

      const fileReader: FileReader = new FileReader();

      fileReader.onloadend = (ev: ProgressEvent) => {
        const template = fileReader.result;

        if (this.mailTemplate && (template instanceof ArrayBuffer)) {

          const base64: string = Base64.encode(template as ArrayBuffer);

          this.mailTemplate.name = this.nameControl.value;
          this.mailTemplate.contentType = this.contentTypeControl.value;
          this.mailTemplate.template = base64;

          this.spinnerService.showSpinner();

          this.mailService.updateMailTemplate(this.mailTemplate)
          .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
          .subscribe(() => {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
          }, (error: Error) => {
            // noinspection SuspiciousTypeOfGuard
            if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) ||
              (error instanceof ServiceUnavailableError)) {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigateByUrl('/error/send-error-report', {state: {error}});
            } else {
              this.dialogService.showErrorDialog(error);
            }
          });
        } else {
          console.log(
            'Failed to read the template file for the report definition (' + fileReader.result + ')');
        }
      };

      fileReader.readAsArrayBuffer(this.templateControl.value[0]);
    }
  }
}
