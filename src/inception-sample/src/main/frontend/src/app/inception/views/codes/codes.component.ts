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

import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {Code} from "../../services/codes/code";
import {CodesService} from "../../services/codes/codes.service";
import {DialogService} from "../../services/dialog/dialog.service";
import {SpinnerService} from "../../services/layout/spinner.service";
import {I18n} from "@ngx-translate/i18n-polyfill";
import {Error} from "../../errors/error";
import {ActivatedRoute, Router} from "@angular/router";
import {Observable, of} from "rxjs";
import {first} from "rxjs/operators";
import {TitleService} from "../../services/layout/title.service";

/**
 * The CodesComponent class implements the codes component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'codes.component.html',
  styleUrls: ['codes.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class CodesComponent implements AfterViewInit, OnInit, OnDestroy {

  codeCategoryId: string;

  displayedColumns: string[] = ['id', 'name', 'actions'];

  dataSource = new MatTableDataSource<Code>();

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private codesService: CodesService, private dialogService: DialogService,
              private layoutService: SpinnerService, private titleService: TitleService) {
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteCode(id: string): void {
    console.log('Deleting code: ', id);
  }

  editCode(id: string): void {
    console.log('Editing code: ', id);
  }

  newCode(): void {
    console.log('New code');
  }

  ngOnInit(): void {
    this.codeCategoryId = this.activatedRoute.snapshot.paramMap.get('id');
    this.titleService.setTitle(this.codeCategoryId);
  }

  ngOnDestroy(): void {
    this.titleService.clearTitle();
  }

  ngAfterViewInit(): void {

    this.layoutService.showSpinner();

    this.codesService.getCodeCategoryCodes(this.codeCategoryId).pipe(first()).subscribe((codes: Code[]) => {

      this.layoutService.hideSpinner();

      this.dataSource.data = codes;

    }, (error: Error) => {

      this.layoutService.hideSpinner();

      this.dialogService.showErrorDialog(error);
    });

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = function(data, filter): boolean {
      return data.id.toLowerCase().includes(filter) || data.name.toLowerCase().includes(filter);
    };
  }
}

