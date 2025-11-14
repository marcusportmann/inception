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

import { AfterViewInit, Component, HostBinding, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import {
  AdminContainerView, ConfirmationDialogComponent, CoreModule, Error, TableFilterComponent
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { MailTemplateContentType } from '../services/mail-template-content-type';
import { MailTemplateSummary } from '../services/mail-template-summary';
import { MailService } from '../services/mail.service';

/**
 * The MailTemplatesComponent class implements the mail templates component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-mail-mail-templates',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'mail-templates.component.html',
  styleUrls: ['mail-templates.component.css']
})
export class MailTemplatesComponent extends AdminContainerView implements AfterViewInit {
  // noinspection JSUnusedGlobalSymbols
  MailTemplateContentType = MailTemplateContentType;

  dataSource: MatTableDataSource<MailTemplateSummary> =
    new MatTableDataSource<MailTemplateSummary>();

  displayedColumns = ['name', 'contentType', 'actions'];

  getMailTemplateContentTypeDescription = MailService.getMailTemplateContentTypeDescription;

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  readonly title = $localize`:@@mail_mail_templates_title:Mail Templates`;

  constructor(private mailService: MailService) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean =>
      data.name.toLowerCase().includes(filter);
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteMailTemplate(mailTemplateId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
        message: $localize`:@@mail_mail_templates_confirm_delete_mail_template:Are you sure you want to delete the mail template?`
      });

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe({
        next: (confirmation: boolean | undefined) => {
          if (confirmation !== true) {
            return;
          }

          this.spinnerService.showSpinner();

          this.mailService
            .deleteMailTemplate(mailTemplateId)
            .pipe(
              first(),
              finalize(() => this.spinnerService.hideSpinner())
            )
            .subscribe({
              next: () => {
                this.loadMailTemplates();
              },
              error: (error: Error) => this.handleError(error, false)
            });
        }
      });
  }

  editMailTemplate(mailTemplateId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(mailTemplateId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  loadMailTemplates(): void {
    this.spinnerService.showSpinner();

    this.mailService
      .getMailTemplateSummaries()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (mailTemplateSummaries: MailTemplateSummary[]) => {
          this.dataSource.data = mailTemplateSummaries;
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  newMailTemplate(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadMailTemplates();
  }
}
