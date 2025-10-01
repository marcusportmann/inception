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

import {
  AfterViewInit,
  Component,
  HostBinding,
  OnDestroy,
  ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { ActivatedRoute, Router } from '@angular/router';
import {
  AccessDeniedError,
  AdminContainerView,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService
} from 'ngx-inception/core';
import { merge, Observable, Subject, throwError } from 'rxjs';
import {
  catchError,
  debounceTime,
  filter,
  finalize,
  first,
  switchMap,
  takeUntil,
  tap
} from 'rxjs/operators';
import { CodeCategorySummary } from '../services/code-category-summary';
import { CodesService } from '../services/codes.service';

/**
 * The CodeCategoriesComponent class implements the code categories component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'code-categories.component.html',
  styleUrls: ['code-categories.component.css'],
  standalone: false
})
export class CodeCategoriesComponent
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  dataSource: MatTableDataSource<CodeCategorySummary> =
    new MatTableDataSource<CodeCategorySummary>();

  displayedColumns = ['id', 'name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  private destroy$ = new Subject<void>();

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private codesService: CodesService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean =>
      data.id.toLowerCase().includes(filter) ||
      data.name.toLowerCase().includes(filter);
  }

  get title(): string {
    return $localize`:@@codes_code_categories_title:Code Categories`;
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }

  codesAdministration(codeCategoryId: string): void {
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

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
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
            catchError((error) => this.handleError(error)),
            tap(() => this.loadData()),
            finalize(() => this.spinnerService.hideSpinner())
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe();
  }

  private handleError(error: Error): Observable<never> {
    if (
      error instanceof AccessDeniedError ||
      error instanceof InvalidArgumentError ||
      error instanceof ServiceUnavailableError
    ) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {
        state: { error }
      });
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private initializeDataLoaders(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  private loadCodeCategorySummaries(): Observable<CodeCategorySummary[]> {
    return this.codesService
      .getCodeCategorySummaries()
      .pipe(catchError((error) => this.handleError(error)));
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadCodeCategorySummaries()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (codeCategorySummaries) => {
          this.dataSource.data = codeCategorySummaries;
        },
        error: (error) => this.handleError(error)
      });
  }
}
