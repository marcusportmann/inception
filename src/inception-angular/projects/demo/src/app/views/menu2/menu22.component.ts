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

import { AfterViewInit, Component, HostBinding, ViewChild } from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import { MatTableDataSource } from '@angular/material/table';
import { Router } from '@angular/router';
import {
  AccessDeniedError,
  CoreModule,
  DialogService,
  Error,
  InvalidArgumentError,
  ServiceUnavailableError,
  SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { Data } from '../../services/data';
import { DataService } from '../../services/data.service';

/**
 * The Menu22Component class implements the menu 2.2 component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'app-menu22',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'menu22.component.html'
})
export class Menu22Component implements AfterViewInit {
  dataSource = new MatTableDataSource<Data>();

  displayedColumns = ['id', 'stringValue', 'dateValue', 'integerValue'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, { static: true }) paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) sort!: MatSort;

  constructor(
    private router: Router,
    private dataService: DataService,
    private dialogService: DialogService,
    private spinnerService: SpinnerService
  ) {
    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean =>
      data.stringValue.toLowerCase().includes(filter);
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  clickMe(): void {
    this.dataService
      .getData()
      .pipe(first())
      .subscribe({
        next: (data: Data) => {
          console.log('data = ', data);

          // No handlers here, so the empty subscribe() is fine
          this.dataService.validateData(data).pipe(first()).subscribe();
        }
        // You can optionally add error / complete here if needed:
        // error: (err) => { ... },
        // complete: () => { ... }
      });
  }

  loadData(): void {
    this.spinnerService.showSpinner();

    this.dataService
      .getAllData()
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: (data: Data[]) => {
          console.log('data = ', data);

          this.dataSource.data = data;
        },
        error: (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
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
        }
      });
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadData();
  }
}
