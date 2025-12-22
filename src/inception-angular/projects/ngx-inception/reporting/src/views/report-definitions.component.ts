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

import { ChangeDetectionStrategy, Component, HostBinding, inject } from '@angular/core';
import { CoreModule, FilteredPaginatedListView, TableFilterComponent } from 'ngx-inception/core';
import { Observable } from 'rxjs';

import { ReportDefinitionSummary } from '../services/report-definition-summary';
import { ReportingService } from '../services/reporting.service';

/**
 * The ReportDefinitionsComponent class implements the Report Definitions component.
 *
 * @author Marcus
 */
@Component({
  selector: 'inception-reporting-report-definitions',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'report-definitions.component.html',
  styleUrls: ['report-definitions.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ReportDefinitionsComponent extends FilteredPaginatedListView<ReportDefinitionSummary> {
  readonly displayedColumns = ['name', 'actions'] as const;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'reporting.report-definitions';

  readonly title = $localize`:@@reporting_report_definitions_title:Report Definitions`;

  private readonly reportingService = inject(ReportingService);

  deleteReportDefinition(reportDefinitionId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@reporting_report_definitions_confirm_delete_report_definition:Are you sure you want to delete the report definition?`,
      () => this.reportingService.deleteReportDefinition(reportDefinitionId)
    );
  }

  editReportDefinition(reportDefinitionId: string): void {
    void this.router.navigate([reportDefinitionId, 'edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newReportDefinition(): void {
    void this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (
    data: ReportDefinitionSummary,
    filter: string
  ) => boolean {
    return (data: ReportDefinitionSummary, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').trim().toLowerCase();
      const name = (data.name ?? '').toLowerCase();

      return name.includes(normalizedFilter);
    };
  }

  protected override fetchData(): Observable<ReportDefinitionSummary[]> {
    return this.reportingService.getReportDefinitionSummaries();
  }
}
