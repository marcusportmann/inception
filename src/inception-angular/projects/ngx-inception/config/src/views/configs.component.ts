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

import {AfterViewInit, Component, HostBinding, OnDestroy, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {ActivatedRoute, Router} from '@angular/router';

import {
  AccessDeniedError, AdminContainerView, ConfirmationDialogComponent, DialogService, Error,
  InvalidArgumentError, ServiceUnavailableError, SpinnerService,
} from 'ngx-inception/core';

import {merge, Observable, Subject, throwError} from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil, tap,
} from 'rxjs/operators';
import {Config} from '../services/config';
import {ConfigService} from '../services/config.service';

/**
 * The ConfigsComponent class implements the configs component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'configs.component.html',
  styleUrls: ['configs.component.css'],
  standalone: false
})
export class ConfigsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource = new MatTableDataSource<Config>();

  displayedColumns = ['id', 'value', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  private destroy$ = new Subject<void>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private configService: ConfigService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.dataSource.filterPredicate = (data: Config,
                                       filter: string): boolean => data.id.toLowerCase().includes(
      filter);
  }

  get title(): string {
    return $localize`:@@config_configs_title:Configs`;
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim().toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteConfig(id: string): void {
    this.confirmAndProcessAction(
      $localize`:@@config_configs_confirm_delete_config:Are you sure you want to delete the config?`,
      () => this.configService.deleteConfig(id));
  }

  editConfig(id: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(id) + '/edit'], {relativeTo: this.activatedRoute});
  }

  newConfig(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
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

  private confirmAndProcessAction(confirmationMessage: string,
                                  action: () => Observable<void | boolean>): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {message: confirmationMessage});

    dialogRef
    .afterClosed()
    .pipe(first(), filter((confirmed) => confirmed === true), switchMap(() => {
      this.spinnerService.showSpinner();
      return action().pipe(catchError((error) => this.handleError(error)),
        tap(() => this.loadData()), finalize(() => this.spinnerService.hideSpinner()));
    }), takeUntil(this.destroy$))
    .subscribe();
  }

  private handleError(error: Error): Observable<never> {
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private initializeDataLoaders(): void {
    this.sort.sortChange.pipe(takeUntil(this.destroy$)).subscribe(
      () => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.paginator.page)
    .pipe(debounceTime(200), takeUntil(this.destroy$))
    .subscribe(() => this.loadData());
  }

  private loadConfigs(): Observable<Config[]> {
    return this.configService.getConfigs().pipe(catchError((error) => this.handleError(error)));
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadConfigs()
    .pipe(finalize(() => this.spinnerService.hideSpinner()))
    .subscribe({
      next: (configs) => {
        this.dataSource.data = configs;
      },
      error: (error) => this.handleError(error),
    });
  }
}
