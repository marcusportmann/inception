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
import { CoreModule, FilteredPaginatedListView, TableFilterComponent } from 'ngx-inception/core';
import { EMPTY, Observable } from 'rxjs';
import { catchError, filter, finalize, first, switchMap, takeUntil } from 'rxjs/operators';

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
  styleUrls: ['code-categories.component.css']
})
export class CodeCategoriesComponent extends FilteredPaginatedListView<CodeCategorySummary> {
  readonly displayedColumns: readonly string[] = ['id', 'name', 'actions'];

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'codes.code-categories';

  readonly title = $localize`:@@codes_code_categories_title:Code Categories`;

  constructor(private codesService: CodesService) {
    super();
  }

  codesAdministration(codeCategoryId: string): void {
    this.listStateService.clear('codes.' + codeCategoryId);

    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(codeCategoryId) + '/codes'], {
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
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(codeCategoryId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newCodeCategory(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (
    data: CodeCategorySummary,
    filter: string
  ) => boolean {
    return (data: CodeCategorySummary, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').toLowerCase();
      const id = (data.id ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();

      return id.includes(normalizedFilter) || name.includes(normalizedFilter);
    };
  }

  protected override loadData(): void {
    this.spinnerService.showSpinner();

    this.codesService
      .getCodeCategorySummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (codeCategorySummaries) => {
          this.dataSource.data = codeCategorySummaries;

          this.restorePageAfterDataLoaded();
        },
        error: (error) => this.handleError(error, false)
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
            catchError((error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            switchMap(() =>
              this.codesService.getCodeCategorySummaries().pipe(
                catchError((error) => {
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
        next: (codeCategorySummaries) => {
          this.dataSource.data = codeCategorySummaries;
        }
      });
  }
}
