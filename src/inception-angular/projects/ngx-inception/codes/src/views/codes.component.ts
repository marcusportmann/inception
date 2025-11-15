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
  BackNavigation, CoreModule, Error, FilteredPaginatedListView, TableFilterComponent
} from 'ngx-inception/core';
import { EMPTY, Observable } from 'rxjs';
import { catchError, filter, finalize, first, switchMap, takeUntil } from 'rxjs/operators';
import { Code } from '../services/code';
import { CodesService } from '../services/codes.service';

/**
 * The CodesComponent class implements the Codes component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-codes-codes',
  standalone: true,
  templateUrl: 'codes.component.html',
  imports: [CoreModule, TableFilterComponent],
  styleUrls: ['codes.component.css']
})
export class CodesComponent extends FilteredPaginatedListView<Code> {
  readonly codeCategoryId: string;

  readonly displayedColumns: readonly string[] = ['id', 'name', 'actions'];

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey: string;

  readonly title = $localize`:@@codes_codes_title:Codes`;

  constructor(private codesService: CodesService) {
    super();

    // Retrieve the route parameters
    const codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    if (!codeCategoryId) {
      throw new Error('No codeCategoryId route parameter found');
    }
    this.codeCategoryId = decodeURIComponent(codeCategoryId);

    this.listKey = `codes.${this.codeCategoryId}`;
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation(
      $localize`:@@codes_codes_back_navigation:Code Categories`,
      ['../..'],
      { relativeTo: this.activatedRoute }
    );
  }

  deleteCode(codeId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@codes_codes_confirm_delete_code:Are you sure you want to delete the code?`,
      () => this.codesService.deleteCode(this.codeCategoryId, codeId)
    );
  }

  editCode(codeId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(codeId) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newCode(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (data: Code, filter: string) => boolean {
    return (data: Code, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').toLowerCase();
      const id = (data.id ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();
      return id.includes(normalizedFilter) || name.includes(normalizedFilter);
    };
  }

  protected override loadData(): void {
    this.spinnerService.showSpinner();
    this.codesService
      .getCodesForCodeCategory(this.codeCategoryId)
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (codes: Code[]) => {
          this.dataSource.data = codes;

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
              this.codesService.getCodesForCodeCategory(this.codeCategoryId).pipe(
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
        next: (codes) => {
          this.dataSource.data = codes;
        }
      });
  }
}
