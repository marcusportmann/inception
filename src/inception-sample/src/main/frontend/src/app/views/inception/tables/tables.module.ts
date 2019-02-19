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

// Import Angular modules
import {CommonModule} from '@angular/common';
import {FormsModule} from "@angular/forms";
import {NgModule} from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

// Import Inception module
import {InceptionModule} from '../../../inception/inception.module';

// Action List Table component
import {ActionListTableComponent} from "./action-list-table.component";

// Action Menu Table component
import {ActionMenuTableComponent} from "./action-menu-table.component";

// Basic Table component
import {BasicTableComponent} from "./basic-table.component";

// Filterable Table component
import {FilterableTableComponent} from "./filterable-table.component";

// Pagination Table component
import {PaginationTableComponent} from "./pagination-table.component";

// Sortable Table component
import {SortableTableComponent} from "./sortable-table.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'action-list-table',
    pathMatch: 'full',
  },
  {
    path: 'action-list-table',
    component: ActionListTableComponent,
    data: {
      title: 'Action List Table',
    }
  },
  {
    path: 'action-menu-table',
    component: ActionMenuTableComponent,
    data: {
      title: 'Action Menu Table',
    }
  },
  {
    path: 'basic-table',
    component: BasicTableComponent,
    data: {
      title: 'Basic Table',
    }
  },
  {
    path: 'filterable-table',
    component: FilterableTableComponent,
    data: {
      title: 'Filterable Table',
    }
  },
  {
    path: 'pagination-table',
    component: PaginationTableComponent,
    data: {
      title: 'Pagination Table',
    }
  },
  {
    path: 'sortable-table',
    component: SortableTableComponent,
    data: {
      title: 'Sortable Table',
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  declarations: [ActionListTableComponent, ActionMenuTableComponent, BasicTableComponent, FilterableTableComponent, PaginationTableComponent, SortableTableComponent],
  providers: [
  ]
})
export class TablesModule {
}
