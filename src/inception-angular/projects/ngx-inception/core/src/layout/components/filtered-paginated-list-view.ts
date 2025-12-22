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
  AfterViewInit, ChangeDetectorRef, Directive, inject, OnDestroy, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { EMPTY, Observable, Subject } from 'rxjs';
import { catchError, filter, finalize, first, switchMap, takeUntil } from 'rxjs/operators';
import { ListStateService } from '../services/list-state.service';
import { AdminContainerView } from './admin-container-view';

@Directive()
export abstract class FilteredPaginatedListView<T>
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  readonly dataSource = new MatTableDataSource<T>();

  readonly defaultPageSize = 10;

  abstract readonly defaultSortActive: string;

  readonly defaultSortDirection: 'asc' | 'desc' = 'asc';

  /** Current filter text (drives both the datasource and the UI filter component). */
  filterValue = '';

  /** Unique key for persisting list state (must be provided by subclass). */
  abstract readonly listStateKey: string;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  protected readonly changeDetectorRef = inject(ChangeDetectorRef);

  protected readonly destroy$ = new Subject<void>();

  protected readonly listStateService = inject(ListStateService);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  /** Guard to ignore the sortChange triggered while restoring state. */
  private restoringState = false;

  constructor() {
    super();

    // Subclasses can override this for their own filter logic
    this.dataSource.filterPredicate = this.createFilterPredicate();

    // Read the reset flag from the current navigation (if any)
    const nav = this.router.currentNavigation();

    this.resetStateRequested = !!nav?.extras.state?.['resetState'];
  }

  applyFilter(filterValue: string): void {
    this.filterValue = filterValue ?? '';

    this.dataSource.filter = (filterValue ?? '').trim().toLowerCase();

    if (this.dataSource.paginator) {
      this.dataSource.paginator.firstPage();
    }

    this.saveState();
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.initTableSubscriptions();

    // Restore filter / sort / pageSize before we load data
    this.restoreStateBeforeData();

    // Subclasses will populate dataSource.data asynchronously
    this.loadData();

    // Stabilize view after mutating sort/paginator
    this.changeDetectorRef.detectChanges();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Displays a confirmation dialog, executes an action when confirmed, and reloads the list data.
   *
   * This method shows a confirmation dialog with the provided message. If the user confirms,
   * it runs {@link actionFn}, then calls {@link fetchData} to retrieve the updated items.
   * On success, the resulting items are assigned to {@link dataSource}.data and the current
   * page index is restored via {@link restorePageAfterDataLoaded}. Errors are reported using
   * {@link handleError}, and the global spinner is shown and hidden automatically.
   *
   * @param confirmationMessage - Localized message displayed in the confirmation dialog.
   * @param actionFn - Function that performs the side effect (e.g., delete or update) and returns an observable.
   */
  protected confirmAndProcessAction(
    confirmationMessage: string,
    actionFn: () => Observable<unknown>
  ): void {
    const dialogRef = this.dialogService.showConfirmationDialog({
      message: confirmationMessage
    });

    dialogRef
      .afterClosed()
      .pipe(
        first(),
        filter((confirmed: boolean | undefined) => confirmed === true),
        switchMap(() => {
          this.spinnerService.showSpinner();

          return actionFn().pipe(
            catchError((error: Error) => {
              this.handleError(error, false);
              return EMPTY;
            }),
            switchMap(() =>
              this.fetchData().pipe(
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
        next: (items: T[]) => {
          this.dataSource.data = items;
          this.restorePageAfterDataLoaded();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  /**
   * Subclasses can override this to customize filtering logic.
   */
  protected createFilterPredicate(): (data: T, filter: string) => boolean {
    // Default: match JSON string
    return (data: T, filter: string): boolean =>
      JSON.stringify(data ?? {})
        .toLowerCase()
        .includes((filter ?? '').toLowerCase());
  }

  /**
   * Subclasses must implement this to fetch the raw list items.
   */
  protected abstract fetchData(): Observable<T[]>;

  /**
   * Loads data using {@link fetchData}, handles spinner, errors, and page restoration.
   */
  protected loadData(): void {
    this.spinnerService.showSpinner();

    this.fetchData()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: (items: T[]) => {
          this.dataSource.data = items;
          this.restorePageAfterDataLoaded();
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  protected restorePageAfterDataLoaded(): void {
    const state = this.listStateService.get(this.listStateKey);

    if (state && this.paginator) {
      const length = this.dataSource.filteredData?.length ?? this.dataSource.data?.length ?? 0;
      const maxPageIndex = length === 0 ? 0 : Math.floor((length - 1) / this.paginator.pageSize);

      this.paginator.pageIndex = Math.min(state.pageIndex, maxPageIndex);

      // Force MatTableDataSource to recompute the page slice
      this.dataSource.paginator = this.paginator;

      this.saveState();
    }

    this.changeDetectorRef.markForCheck();
  }

  protected saveState(): void {
    if (!this.paginator || !this.sort) {
      return;
    }

    this.listStateService.set(this.listStateKey, {
      pageIndex: this.paginator.pageIndex ?? 0,
      pageSize: this.paginator.pageSize ?? 10,
      sortActive: this.sort.active ?? '',
      sortDirection: this.sort.direction,
      filter: this.filterValue ?? ''
    });
  }

  private initTableSubscriptions(): void {
    this.sort.sortChange.pipe(takeUntil(this.destroy$)).subscribe(() => {
      // Ignore the sortChange events triggered while we're restoring state
      if (this.restoringState) {
        return;
      }

      if (this.paginator) {
        this.paginator.firstPage();
      }

      this.saveState();
    });

    this.paginator.page.pipe(takeUntil(this.destroy$)).subscribe(() => {
      this.saveState();
    });
  }

  private restoreStateBeforeData(): void {
    // Always start with known defaults (so the template doesn’t need to)
    if (this.paginator) {
      this.paginator.pageSize = this.defaultPageSize;
      this.paginator.pageIndex = 0;
    }
    if (this.sort) {
      this.sort.active = this.defaultSortActive;
      this.sort.direction = this.defaultSortDirection;
    }
    this.filterValue = '';
    this.dataSource.filter = '';

    // If the navigation asked for a reset, keep defaults and bail
    if (this.resetStateRequested) {
      this.listStateService.clear(this.listStateKey);
      return;
    }

    // Restore the saved state if present
    const state = this.listStateService.get(this.listStateKey);
    if (!state || !this.paginator || !this.sort) {
      return;
    }

    this.restoringState = true;

    this.filterValue = state.filter ?? '';
    this.dataSource.filter = (state.filter ?? '').trim().toLowerCase();

    this.paginator.pageSize = state.pageSize ?? this.defaultPageSize;
    this.paginator.pageIndex = state.pageIndex ?? 0;

    this.sort.active = state.sortActive ?? this.defaultSortActive;
    this.sort.direction = state.sortDirection ?? this.defaultSortDirection;

    this.restoringState = false;
  }
}
