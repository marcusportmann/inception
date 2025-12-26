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

import { CodeCategorySummary } from '../services/code-category-summary';
import { CodesService } from '../services/codes.service';

/**
 * The CodeCategoriesComponent class implements the Code Categories component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-codes-code-categories',
  standalone: true,
  templateUrl: 'code-categories.component.html',
  imports: [CoreModule, TableFilterComponent],
  styleUrls: ['code-categories.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CodeCategoriesComponent extends FilteredPaginatedListView<CodeCategorySummary> {
  readonly defaultSortActive = 'id';

  readonly displayedColumns = ['id', 'name', 'actions'] as const;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listStateKey = 'codes.code-categories';

  readonly title = $localize`:@@codes_code_categories_title:Code Categories`;

  private readonly codesService = inject(CodesService);

  codesAdministration(codeCategoryId: string): void {
    this.listStateService.clear(`codes.code-category.${encodeURIComponent(codeCategoryId)}`);

    void this.router.navigate([codeCategoryId, 'codes'], {
      relativeTo: this.activatedRoute
    });
  }

  deleteCodeCategory(codeCategoryId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@codes_code_categories_confirm_delete_code_category:Are you sure you want to delete the code category?`,
      () => this.codesService.deleteCodeCategory(codeCategoryId)
    );
  }

  editCodeCategory(codeCategoryId: string): void {
    void this.router.navigate([codeCategoryId, 'edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newCodeCategory(): void {
    void this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (
    data: CodeCategorySummary,
    filter: string
  ) => boolean {
    return (data: CodeCategorySummary, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').trim().toLowerCase();
      const id = (data.id ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();

      return id.includes(normalizedFilter) || name.includes(normalizedFilter);
    };
  }

  protected override fetchData(): Observable<CodeCategorySummary[]> {
    return this.codesService.getCodeCategorySummaries();
  }
}
