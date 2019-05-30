/*
 * Copyright 2019 Marcus Portmann
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

import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {CodesService} from '../../services/codes/codes.service';
import {finalize, first} from 'rxjs/operators';
import {CodesServiceError} from '../../services/codes/codes.service.errors';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {CodeCategorySummary} from '../../services/codes/code-category-summary';
import {AccessDeniedError} from '../../errors/access-denied-error';

/**
 * The CodeCategoriesComponent class implements the code categories component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'code-categories.component.html',
  styleUrls: ['code-categories.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class CodeCategoriesComponent implements AfterViewInit, OnInit {

  dataSource = new MatTableDataSource<CodeCategorySummary>();

  displayedColumns: string[] = ['id', 'name', 'actions'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  codesAdministration(codeCategoryId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([codeCategoryId + '/codes'], {relativeTo: this.activatedRoute});
  }

  deleteCodeCategory(codeCategoryId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@code_categories_component_confirm_delete_code_category',
          value: 'Are you sure you want to delete the code category?'
        })
      });

    dialogRef.afterClosed()
      .pipe(first())
      .subscribe((confirmation: boolean) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.codesService.deleteCodeCategory(codeCategoryId)
            .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
            .subscribe(() => {
              this.loadCodeCategorySummaries();
            }, (error: Error) => {
              if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) ||
                (error instanceof SystemUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        }
      });
  }

  editCodeCategory(codeCategoryId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([codeCategoryId], {relativeTo: this.activatedRoute});
  }

  loadCodeCategorySummaries(): void {
    this.spinnerService.showSpinner();

    this.codesService.getCodeCategorySummaries()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((codeCategorySummaries: CodeCategorySummary[]) => {
        this.dataSource.data = codeCategorySummaries;
      }, (error: Error) => {
        if ((error instanceof CodesServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = function (data, filter): boolean {
      return data.id.toLowerCase().includes(filter) || data.name.toLowerCase().includes(filter);
    };
  }

  newCodeCategory(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new-code-category'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.loadCodeCategorySummaries();
  }

  ngOnInit(): void {
  }
}

