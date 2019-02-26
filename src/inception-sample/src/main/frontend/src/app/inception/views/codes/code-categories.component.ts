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

import {AfterViewInit, Component, OnDestroy, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {CodeCategory} from "../../services/codes/code-category";
import {Observable} from "rxjs";
import {CodesService} from "../../services/codes/codes.service";
import {catchError, map} from "rxjs/operators";
import {CodesServiceError} from "../../services/codes/codes.service.errors";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {Error} from "../../errors/error";
import {ActivatedRoute, Router} from "@angular/router";

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
export class CodeCategoriesComponent implements AfterViewInit {

  displayedColumns: string[] = ['id', 'name', 'actions'];

  dataSource = new MatTableDataSource<CodeCategory>();

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private layoutService: SpinnerService)
  {}

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  codesAdministration(id: string): void {
    this.router.navigate([id + '/codes'], {relativeTo: this.activatedRoute});
  }

  deleteCodeCategory(id: string): void {
    console.log('Deleting code category: ', id);
  }

  editCodeCategory(id: string): void {
    console.log('Editing code category: ', id);
  }

  newCodeCategory(): void {
    this.router.navigate(['../new-code-category'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {

    this.layoutService.showSpinner();

    this.codesService.getCodeCategories().subscribe((codeCategories: CodeCategory[]) => {

      this.layoutService.hideSpinner();

      this.dataSource.data = codeCategories;

    }, (error: Error) => {

      this.layoutService.hideSpinner();

      this.dialogService.showErrorDialog(error);
    });

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}

