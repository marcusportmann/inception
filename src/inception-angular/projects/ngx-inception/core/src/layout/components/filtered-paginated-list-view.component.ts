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
import { AdminContainerView } from 'ngx-inception/core';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';
import { ListStateService } from '../services/list-state.service';

@Directive()
export abstract class FilteredPaginatedListView<T>
  extends AdminContainerView
  implements AfterViewInit, OnDestroy
{
  readonly dataSource = new MatTableDataSource<T>();

  /** Unique key for this list (per route / context). */
  abstract readonly listKey: string;

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;
  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  protected readonly changeDetectorRef = inject(ChangeDetectorRef);

  protected readonly destroy$ = new Subject<void>();

  protected readonly listStateService = inject(ListStateService);

  /** Guard to ignore the sortChange triggered while restoring state. */
  private restoringState = false;

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  protected constructor() {
    super();

    // Subclasses can override this for their own filter logic
    this.dataSource.filterPredicate = this.createFilterPredicate();

    // Read the reset flag from the current navigation (if any)
    const nav = this.router.getCurrentNavigation();
    this.resetStateRequested = !!nav?.extras.state?.['resetState'];
  }

  applyFilter(filterValue: string): void {
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
   * Subclasses must implement this to fetch data and assign `this.dataSource.data`.
   */
  protected abstract loadData(): void;

  /**
   * Call this **after** setting `this.dataSource.data` in your component's `loadData()` subscription.
   */
  protected restorePageAfterDataLoaded(): void {
    const state = this.listStateService.get(this.listKey);
    if (!state || !this.paginator) {
      return;
    }

    const length = this.dataSource.filteredData?.length ?? this.dataSource.data?.length ?? 0;
    const maxPageIndex = length === 0 ? 0 : Math.floor((length - 1) / this.paginator.pageSize);

    this.paginator.pageIndex = Math.min(state.pageIndex, maxPageIndex);

    // Force MatTableDataSource to recompute the page slice
    this.dataSource.paginator = this.paginator;

    this.saveState();
    this.changeDetectorRef.detectChanges();
  }

  protected saveState(): void {
    if (!this.paginator || !this.sort) {
      return;
    }

    this.listStateService.set(this.listKey, {
      pageIndex: this.paginator.pageIndex ?? 0,
      pageSize: this.paginator.pageSize ?? 10,
      sortActive: this.sort.active ?? '',
      sortDirection: this.sort.direction,
      filter: this.dataSource.filter ?? ''
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
    // If the navigation asked for a reset, clear the state and use defaults
    if (this.resetStateRequested) {
      this.listStateService.clear(this.listKey);

      // Make sure the local UI is in a clean state as well
      this.dataSource.filter = '';
      if (this.paginator) {
        this.paginator.pageIndex = 0;
        // pageSize stays whatever default you configured in the template
      }
      if (this.sort) {
        this.sort.active = '';
        this.sort.direction = '';
      }
      return;
    }

    const state = this.listStateService.get(this.listKey);
    if (!state || !this.paginator || !this.sort) {
      return;
    }

    this.restoringState = true;

    this.dataSource.filter = state.filter;
    this.paginator.pageSize = state.pageSize;
    this.sort.active = state.sortActive;
    this.sort.direction = state.sortDirection;

    this.restoringState = false;
  }
}
