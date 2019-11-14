/*
 * Copyright 2019 Marcus Portmann
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
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {MailTemplate} from "../../services/mail/mail-template";
import {MailService} from "../../services/mail/mail.service";
import {MailServiceError} from "../../services/mail/mail.service.errors";
import {MailTemplateContentType} from '../../services/mail/mail-template-content-type';
import {FileValidator} from "../../validators/file-validator";
import {ReportingService} from "../../services/reporting/reporting.service";
import {Base64} from "../../util";

/**
 * The EditMailTemplateComponent class implements the edit mail template component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'edit-mail-template.component.html',
  styleUrls: ['edit-mail-template.component.css'],
})
export class EditMailTemplateComponent extends AdminContainerView implements AfterViewInit {

  MailTemplateContentType = MailTemplateContentType;

  contentTypes: MailTemplateContentType[] = [MailTemplateContentType.Text,
    MailTemplateContentType.HTML, MailTemplateContentType.Unknown
  ];

  mailTemplate?: MailTemplate;

  mailTemplateId: string;

  editMailTemplateForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private mailService: MailService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.mailTemplateId =
      decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('mailTemplateId')!);

    // Initialise the form
    this.editMailTemplateForm = new FormGroup({
      contentType: new FormControl('', [Validators.required]),
      id: new FormControl({
        value: '',
        disabled: true
      }, [Validators.required, Validators.maxLength(100)]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      template: new FormControl('', [Validators.required, FileValidator.minSize(1),
        FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)
      ])
    });
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@mail_edit_mail_template_component_back_title',
      value: 'Mail Templates'
    }), ['../..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@mail_edit_mail_template_component_title',
      value: 'Edit Mail Template'
    })
  }

  ngAfterViewInit(): void {
    this.spinnerService.showSpinner();

    // Retrieve the existing mail template and initialise the form controls
    this.mailService.getMailTemplate(this.mailTemplateId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((mailTemplate: MailTemplate) => {
        this.mailTemplate = mailTemplate;
        this.editMailTemplateForm.get('id')!.setValue(mailTemplate.id);
        this.editMailTemplateForm.get('name')!.setValue(mailTemplate.name);
        this.editMailTemplateForm.get('contentType')!.setValue(mailTemplate.contentType);
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof MailServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.mailTemplate && this.editMailTemplateForm.valid) {

      let fileReader: FileReader = new FileReader();

      fileReader.onloadend = (ev: ProgressEvent) => {
        let template = fileReader.result;

        if (template instanceof ArrayBuffer) {

          let base64: string = Base64.encode(template as ArrayBuffer);

          this.mailTemplate!.name = this.editMailTemplateForm.get('name')!.value;
          this.mailTemplate!.contentType = this.editMailTemplateForm.get('contentType')!.value;
          this.mailTemplate!.template = base64;

          console.log(this.mailTemplate!);

          this.spinnerService.showSpinner();

          this.mailService.updateMailTemplate(this.mailTemplate!)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
            }, (error: Error) => {
              // noinspection SuspiciousTypeOfGuard
              if ((error instanceof MailServiceError) || (error instanceof AccessDeniedError) ||
                (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        } else {
          console.log(
            'Failed to read the template file for the report definition (' + fileReader.result +
            ')');
        }
      };

      fileReader.readAsArrayBuffer(this.editMailTemplateForm.get('template')!.value[0]);
    }
  }
}
