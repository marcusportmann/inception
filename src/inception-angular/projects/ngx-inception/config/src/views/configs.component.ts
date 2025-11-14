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

import { AfterViewInit, Component, HostBinding, OnDestroy, ViewChild } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';

import {
  AdminContainerView, ConfirmationDialogComponent, CoreModule, TableFilterComponent
} from 'ngx-inception/core';

import { EMPTY, merge, Observable, Subject } from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil, tap
} from 'rxjs/operators';
import { Config } from '../services/config';
import { ConfigService } from '../services/config.service';

/**
 * The ConfigsComponent class implements the configs component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-config-configs',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'configs.component.html',
  styleUrls: ['configs.component.css']
})
export class ConfigsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource = new MatTableDataSource<Config>();

  displayedColumns = ['id', 'value', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  readonly title = $localize`:@@config_configs_title:Configs`;

  private destroy$ = new Subject<void>();

  constructor(private configService: ConfigService) {
    super();

    this.dataSource.filterPredicate = (data: Config, filter: string): boolean =>
      data.id.toLowerCase().includes(filter);
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteConfig(id: string): void {
    this.confirmAndProcessAction(
      $localize`:@@config_configs_confirm_delete_config:Are you sure you want to delete the config?`,
      () => this.configService.deleteConfig(id)
    );
  }

  editConfig(id: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(id) + '/edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newConfig(): void {
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
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog({
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
            tap(() => this.loadData()),
            finalize(() => this.spinnerService.hideSpinner())
          );
        }),
        takeUntil(this.destroy$)
      )
      .subscribe();
  }

  private initializeDataLoaders(): void {
    this.sort.sortChange
      .pipe(takeUntil(this.destroy$))
      .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
      .pipe(debounceTime(200), takeUntil(this.destroy$))
      .subscribe(() => this.loadData());
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.configService
      .getConfigs()
      .pipe(finalize(() => this.spinnerService.hideSpinner()))
      .subscribe({
        next: (configs) => {
          this.dataSource.data = configs;
        },
        error: (error) => this.handleError(error, false)
      });
  }
}
