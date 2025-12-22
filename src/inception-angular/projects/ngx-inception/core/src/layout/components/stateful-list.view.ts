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

import { ChangeDetectorRef, Directive, inject, OnDestroy, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';

import { EMPTY, merge, Observable, Subject } from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil
} from 'rxjs/operators';
import { TableFilterComponent } from '../../forms/components/table-filter.component';
import { ListState, ListStateService } from '../services/list-state.service';
import { AdminContainerView } from './admin-container-view';

/**
 * Base view for list screens with persisted state (page, sort, filter, extras).
 */
@Directive()
export abstract class StatefulListView<TExtras = unknown>
  extends AdminContainerView
  implements OnDestroy
{
  /** Unique key for persisting list state (must be provided by subclass). */
  abstract readonly listKey: string;

  protected readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Shared destroy notifier for subscriptions. */
  protected readonly destroy$ = new Subject<void>();

  protected readonly listStateService = inject(ListStateService);

  @ViewChild(MatPaginator, { static: true })
  protected paginator!: MatPaginator;

  /** Guard to ignore events while restoring state. */
  protected restoringState = false;

  @ViewChild(MatSort, { static: true })
  protected sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  protected tableFilter!: TableFilterComponent;

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  /**
   * Show a confirmation dialog, perform an action, then reload the list.
   *
   * @param confirmationMessage The i18n-ready confirmation text.
   * @param actionFn            The function that executes the side effect.
   * @param reloadFn            The function that reloads the list data.
   */
  protected confirmAndProcessAction(
    confirmationMessage: string,
    actionFn: () => Observable<unknown>,
    reloadFn: () => Observable<unknown>
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
              reloadFn().pipe(
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
        error: (error: Error) => this.handleError(error, false)
      });
  }

  /**
   * Subclasses override to provide additional state to persist.
   */
  protected getExtrasState(): TExtras | undefined {
    return undefined;
  }

  /**
   * Call this from the subclass' ngAfterViewInit *after* `ViewChild`s are ready.
   *
   * @param resetStateRequested whether this navigation requested a state reset
   * @param loadDataFn callback that loads data based on current UI state
   * @param extraStateChangingTriggers extra observables that should:
   *   - persist list state when they emit
   *   - optionally reset page index
   *   - trigger data reload (debounced)
   */
  protected initializeStatefulList(
    resetStateRequested: boolean,
    loadDataFn: () => void,
    extraStateChangingTriggers: {
      observable: Observable<unknown>;
      resetPage: boolean;
    }[] = []
  ): void {
    if (resetStateRequested) {
      this.resetTable();
    } else {
      this.restoreStateBeforeData();
    }

    this.initializeDataLoaders(extraStateChangingTriggers, loadDataFn);

    // Initial data load
    loadDataFn();
  }

  /**
   * Common logic when some control changed state:
   * optionally reset page index and persist state.
   */
  protected onStateChangingEvent(resetPage: boolean): void {
    if (this.restoringState) {
      return;
    }

    if (resetPage && this.paginator) {
      this.paginator.firstPage();
    }

    this.saveState();
  }

  /**
   * Subclasses override to reset custom extras to their defaults.
   */
  protected resetExtrasState(): void {
    // Default: no extras
  }

  /**
   * Reset the table to defaults and clear persisted state.
   */
  protected resetTable(): void {
    this.restoringState = true;
    this.listStateService.clear(this.listKey);

    if (this.tableFilter) {
      this.tableFilter.reset(false);
    }

    if (this.paginator) {
      this.paginator.pageIndex = 0;
    }

    if (this.sort) {
      this.sort.active = '';
      this.sort.direction = 'asc';
    }

    this.resetExtrasState();

    this.restoringState = false;

    this.changeDetectorRef.markForCheck();
  }

  /**
   * Subclasses override to restore extras from the persisted state.
   */
  protected restoreExtrasState(extras: TExtras | undefined): void {
    void extras;
  }

  /**
   * Restore the persisted list state before the first data load.
   */
  protected restoreStateBeforeData(): void {
    const state = this.listStateService.get(this.listKey) as ListState<TExtras> | undefined;

    if (state && this.paginator && this.sort) {
      this.restoringState = true;

      if (this.tableFilter) {
        this.tableFilter.value = state.filter ?? '';
      }

      this.paginator.pageSize = state.pageSize;
      this.paginator.pageIndex = state.pageIndex;

      this.sort.active = state.sortActive;
      this.sort.direction = state.sortDirection;

      this.restoreExtrasState(state.extras);

      this.restoringState = false;
    }

    this.changeDetectorRef.markForCheck();
  }

  /**
   * Persist the current list state using ListStateService.
   */
  protected saveState(): void {
    if (!this.paginator || !this.sort) {
      return;
    }

    const state: ListState<TExtras> = {
      pageIndex: this.paginator.pageIndex ?? 0,
      pageSize: this.paginator.pageSize ?? 10,
      sortActive: this.sort.active ?? '',
      sortDirection: (this.sort.direction ?? 'asc') as 'asc' | 'desc' | '',
      filter: this.tableFilter?.filter ?? '',
      extras: this.getExtrasState()
    };

    this.listStateService.set(this.listKey, state);
  }

  private initializeDataLoaders(
    extraStateChangingTriggers: {
      observable: Observable<unknown>;
      resetPage: boolean;
    }[],
    loadDataFn: () => void
  ): void {
    // Sort change: reset to the first page and save state
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.onStateChangingEvent(true));

    // Text filter change: reset to the first page and save state
    this.tableFilter.changed
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.onStateChangingEvent(true));

    // Pagination change: save state only
    this.paginator.page
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => this.onStateChangingEvent(false));

    // Extra triggers (e.g. tokenStatusSelect.selectionChange)
    extraStateChangingTriggers.forEach(({ observable, resetPage }) => {
      observable
        .pipe(takeUntil(this.destroy$))
        .subscribe(() => this.onStateChangingEvent(resetPage));
    });

    // Any of sort / extras / text filter / page changing reloads data
    const reloadTriggers: Observable<unknown>[] = [
      this.sort.sortChange,
      this.tableFilter.changed,
      this.paginator.page,
      ...extraStateChangingTriggers.map((t) => t.observable)
    ];

    merge(...reloadTriggers)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => {
        if (this.restoringState) {
          return;
        }
        loadDataFn();
      });
  }
}
