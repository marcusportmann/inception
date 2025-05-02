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

import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {RouterModule, Routes} from '@angular/router';
import {CoreModule} from 'ngx-inception/core';
import {ActionListTableComponent} from './action-list-table.component';
import {ActionMenuTableComponent} from './action-menu-table.component';
import {BasicTableComponent} from './basic-table.component';
import {FilterableTableComponent} from './filterable-table.component';
import {PaginationTableComponent} from './pagination-table.component';
import {SortableTableComponent} from './sortable-table.component';

const routes: Routes = [
  {
    path: '',
    pathMatch: 'prefix',
    redirectTo: 'action-list-table',
  }, {
    path: 'action-list-table',
    pathMatch: 'prefix',
    component: ActionListTableComponent,
    data: {
      title: 'Action List Table',
    }
  }, {
    path: 'action-menu-table',
    pathMatch: 'prefix',
    component: ActionMenuTableComponent,
    data: {
      title: 'Action Menu Table',
    }
  }, {
    path: 'basic-table',
    pathMatch: 'prefix',
    component: BasicTableComponent,
    data: {
      title: 'Basic Table',
    }
  }, {
    path: 'filterable-table',
    pathMatch: 'prefix',
    component: FilterableTableComponent,
    data: {
      title: 'Filterable Table',
    }
  }, {
    path: 'pagination-table',
    pathMatch: 'prefix',
    component: PaginationTableComponent,
    data: {
      title: 'Pagination Table',
    }
  }, {
    path: 'sortable-table',
    pathMatch: 'prefix',
    component: SortableTableComponent,
    data: {
      title: 'Sortable Table',
    }
  }
];

@NgModule({
  imports: [
    // Angular modules
    CommonModule, FormsModule, RouterModule.forChild(routes),

    // Inception modules
    CoreModule
  ],
  declarations: [
    ActionListTableComponent, ActionMenuTableComponent, BasicTableComponent,
    FilterableTableComponent, PaginationTableComponent, SortableTableComponent
  ],
  providers: []
})
export class TablesModule {
}
