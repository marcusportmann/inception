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

import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {CodeCategory} from "../../services/codes/code-category";
import {Observable} from "rxjs";
import {CodesService} from "../../services/codes/codes.service";
import {catchError, map} from "rxjs/operators";
import {CodesServiceError} from "../../services/codes/codes.service.errors";

@Component({
  templateUrl: 'code-category-administration.component.html',
  styleUrls: ['code-category-administration.component.css']
})
export class CodeCategoryAdministrationComponent implements AfterViewInit {
  displayedColumns: string[] = ['name', 'actions'];

  dataSource = new MatTableDataSource<CodeCategory>();

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;


  constructor(private codesService: CodesService)
  {

  }


  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  ngAfterViewInit() {
    this.codesService.getCodeCategories().subscribe((codeCategories: CodeCategory[]) => {

      this.dataSource.data = codeCategories;

    }, (error: CodesServiceError) => {

      console.log('Error: ', error);

    });


    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
  }

  editCodeCategory(id: string) {
    console.log('Editing code category: ', id);
  }

  codeAdministration(id: string) {
    console.log('Administering codes for code category: ', id);
  }

  deleteCodeCategory(id: string) {
    console.log('Deleting code category: ', id);
  }

}

