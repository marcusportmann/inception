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

import { Component, Input, ViewEncapsulation  } from '@angular/core';

@Component({
  templateUrl: 'pagination.component.html',
  styles: ['.pager li.btn:active { box-shadow: none; }'],
  encapsulation: ViewEncapsulation.None
})
export class PaginationComponent {

  bigCurrentPage: number = 1;

  bigTotalItems: number = 675;

  currentPage: number   = 4;

  currentPager: number   = 4;

  maxSize: number = 5;

  numPages: number = 0;

  smallnumPages: number = 0;

  totalItems: number = 64;

  constructor() { }

  pageChanged(event: any): void {
    console.log('Page changed to: ' + event.page);
    console.log('Number items per page: ' + event.itemsPerPage);
  }

  setPage(pageNo: number): void {
    this.currentPage = pageNo;
  }
}
