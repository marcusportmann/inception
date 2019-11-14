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
import {MailTemplateContentType} from "../../services/mail/mail-template-content-type";
import {MailServiceError} from "../../services/mail/mail.service.errors";
import {v4 as uuid} from "uuid";
import {FileValidator} from "../../validators/file-validator";
import {Base64} from "../../util";
import {ReportingService} from "../../services/reporting/reporting.service";

/**
 * The NewMailTemplateComponent class implements the new mail template component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'new-mail-template.component.html',
  styleUrls: ['new-mail-template.component.css'],
})
export class NewMailTemplateComponent extends AdminContainerView implements AfterViewInit {

  MailTemplateContentType = MailTemplateContentType;

  contentTypes: MailTemplateContentType[] = [MailTemplateContentType.Text,
    MailTemplateContentType.HTML, MailTemplateContentType.Unknown
  ];

  mailTemplate?: MailTemplate;

  newMailTemplateForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private mailService: MailService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Initialise the form
    this.newMailTemplateForm = new FormGroup({
      contentType: new FormControl('', [Validators.required]),
      id: new FormControl(uuid(), [Validators.required]),
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      template: new FormControl('', [Validators.required, FileValidator.minSize(1),
        FileValidator.maxSize(ReportingService.MAX_TEMPLATE_SIZE)])
    })
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@mail_new_mail_template_component_back_title',
      value: 'Mail Templates'
    }), ['..'], {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return this.i18n({
      id: '@@mail_new_mail_template_component_title',
      value: 'New Mail Template'
    })
  }

  ngAfterViewInit(): void {
    this.mailTemplate = new MailTemplate('', '', MailTemplateContentType.Text, '');
  }

  onCancel(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['..'], {relativeTo: this.activatedRoute});
  }

  onOK(): void {
    if (this.mailTemplate && this.newMailTemplateForm.valid) {

      let fileReader: FileReader = new FileReader();

      fileReader.onloadend = (ev: ProgressEvent) => {
        let template = fileReader.result;

        if (template instanceof ArrayBuffer) {

          let base64: string = Base64.encode(template as ArrayBuffer);

          this.mailTemplate!.id = this.newMailTemplateForm.get('id')!.value;
          this.mailTemplate!.name = this.newMailTemplateForm.get('name')!.value;
          this.mailTemplate!.contentType = this.newMailTemplateForm.get('contentType')!.value;
          this.mailTemplate!.template = base64;

          console.log(this.mailTemplate!);

          this.spinnerService.showSpinner();

          this.mailService.createMailTemplate(this.mailTemplate!)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigate(['..'], {relativeTo: this.activatedRoute});
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
          console.log('Failed to read the template file for the report definition (' + fileReader.result + ')');
        }
      };

      fileReader.readAsArrayBuffer(this.newMailTemplateForm.get('template')!.value[0]);
    }
  }
}
