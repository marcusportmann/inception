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

import {AfterViewInit, Component, HostBinding, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {finalize, first} from 'rxjs/operators';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {MailTemplateContentType} from '../../services/mail/mail-template-content-type';
import {MailService} from '../../services/mail/mail.service';
import {MailServiceError} from '../../services/mail/mail.service.errors';
import {MailTemplateSummary} from '../../services/mail/mail-template-summary';

/**
 * The MailTemplatesComponent class implements the mail templates component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'mail-templates.component.html',
  styleUrls: ['mail-templates.component.css']
})
export class MailTemplatesComponent extends AdminContainerView implements AfterViewInit {

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  MailTemplateContentType = MailTemplateContentType;

  dataSource: MatTableDataSource<MailTemplateSummary> = new MatTableDataSource<MailTemplateSummary>();

  displayedColumns = ['name', 'contentType', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator: MatPaginator | null;

  @ViewChild(MatSort, {static: true}) sort: MatSort | null;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n, private mailService: MailService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean => data.name.toLowerCase().includes(filter);
  }

  get title(): string {
    return this.i18n({
      id: '@@mail_mail_templates_component_title',
      value: 'Mail Templates'
    });
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteMailTemplate(mailTemplateId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: this.i18n({
        id: '@@mail_mail_templates_component_confirm_delete_mail_template',
        value: 'Are you sure you want to delete the mail template?'
      })
    });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.mailService.deleteMailTemplate(mailTemplateId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadMailTemplates();
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
      });
  }

  editMailTemplate(mailTemplateId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(mailTemplateId) + '/edit'], {relativeTo: this.activatedRoute});
  }

  loadMailTemplates(): void {
    this.spinnerService.showSpinner();

    this.mailService.getMailTemplateSummaries()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((mailTemplateSummaries: MailTemplateSummary[]) => {
        this.dataSource.data = mailTemplateSummaries;
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof MailService) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  newMailTemplate(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadMailTemplates();
  }
}

