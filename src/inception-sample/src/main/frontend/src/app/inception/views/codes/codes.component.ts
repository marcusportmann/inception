/*
 * Copyright 2018 Marcus Portmann
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
import {Code} from "../../services/codes/code";
import {Observable} from "rxjs";
import {CodesService} from "../../services/codes/codes.service";
import {catchError, map} from "rxjs/operators";
import {CodesServiceError} from "../../services/codes/codes.service.errors";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {Error} from "../../errors/error";

@Component({
  templateUrl: 'codes.component.html',
  styleUrls: ['codes.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class CodesComponent implements AfterViewInit {

  displayedColumns: string[] = ['name', 'actions'];

  dataSource = new MatTableDataSource<Code>();

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private dialogService: DialogService, private spinnerService: SpinnerService, private i18n: I18n, private codesService: CodesService)
  {

  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteCode(id: string) {
    console.log('Deleting code: ', id);
  }

  editCode(id: string) {
    console.log('Editing code: ', id);
  }

  newCode() {
    console.log('New code');
  }

  ngAfterViewInit() {

    /*
    this.spinnerService.show();

    this.codesSubscription = this.codesService.getCodes().subscribe((codes: Code[]) => {

      this.spinnerService.hide();

      this.dataSource.data = codes;

    }, (error: Error) => {

      this.spinnerService.hide();

      this.dialogService.showErrorDialog(error);
    });
    */

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }
}

