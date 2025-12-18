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

import { Component, HostBinding, inject } from '@angular/core';
import { CoreModule, FilteredPaginatedListView, TableFilterComponent } from 'ngx-inception/core';
import { Observable } from 'rxjs';

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

  private mailService = inject(MailService);

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

  protected override fetchData(): Observable<MailTemplateSummary[]> {
    return this.mailService.getMailTemplateSummaries();
  }
}
