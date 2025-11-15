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

import { Component, HostBinding } from '@angular/core';
import {
  CoreModule, Error, FilteredPaginatedListView, TableFilterComponent
} from 'ngx-inception/core';
import { EMPTY, Observable } from 'rxjs';
import { catchError, filter, finalize, first, switchMap, takeUntil } from 'rxjs/operators';

import { MailTemplateContentType } from '../services/mail-template-content-type';
import { MailTemplateSummary } from '../services/mail-template-summary';
import { MailService } from '../services/mail.service';

/**
 * The MailTemplatesComponent class implements the Mail Templates component.
 *
 * @author Marcus
 */
@Component({
  selector: 'inception-mail-mail-templates',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'mail-templates.component.html',
  styleUrls: ['mail-templates.component.css']
})
export class MailTemplatesComponent extends FilteredPaginatedListView<MailTemplateSummary> {
  // noinspection JSUnusedGlobalSymbols
  readonly MailTemplateContentType = MailTemplateContentType;

  readonly displayedColumns: readonly string[] = ['name', 'contentType', 'actions'];

  readonly getMailTemplateContentTypeDescription =
    MailService.getMailTemplateContentTypeDescription;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'mail.mail-templates';

  readonly title = $localize`:@@mail_mail_templates_title:Mail Templates`;

  constructor(private mailService: MailService) {
    super();
  }

  deleteMailTemplate(mailTemplateId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@mail_mail_templates_confirm_delete_mail_template:Are you sure you want to delete the mail template?`,
      () => this.mailService.deleteMailTemplate(mailTemplateId)
    );
  }

  editMailTemplate(mailTemplateId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(mailTemplateId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newMailTemplate(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (
    data: MailTemplateSummary,
    filter: string
  ) => boolean {
    return (data: MailTemplateSummary, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();

      return name.includes(normalizedFilter);
    };
  }

  protected override loadData(): void {
    this.spinnerService.showSpinner();

    this.mailService
      .getMailTemplateSummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (mailTemplateSummaries: MailTemplateSummary[]) => {
          this.dataSource.data = mailTemplateSummaries;

          this.restorePageAfterDataLoaded();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private confirmAndProcessAction(
    confirmationMessage: string,
    action: () => Observable<void | boolean>
  ): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: confirmationMessage
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed) => confirmed === true),
        switchMap(() => {
          this.spinnerService.showSpinner();

          return action().pipe(
            catchError((error: Error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            switchMap(() =>
              this.mailService.getMailTemplateSummaries().pipe(
                catchError((error: Error) => {
                  this.handleError(error, false);
                  return EMPTY;
                })
              )
            ),
            finalize(() => this.spinnerService.hideSpinner())
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (mailTemplateSummaries: MailTemplateSummary[]) => {
          this.dataSource.data = mailTemplateSummaries;

          this.restorePageAfterDataLoaded();
        }
      });
  }
}
