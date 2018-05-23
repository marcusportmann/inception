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

// Import Angular modules
import {CommonModule} from '@angular/common';
import {NgModule} from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";
import {Menu21Component} from "./menu21.component";
import {Menu22Component} from "./menu22.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'menu21',
    pathMatch: 'full',
  },
  {
    path: 'menu21',
    component: Menu21Component,
    data: {
      title: 'Menu 2.1',
      icon: 'icon-doc',
      sidebarNav: true
    }
  },
  {
    path: 'menu22',
    component: Menu22Component,
    data: {
      title: 'Menu 2.2',
      icon: 'icon-doc',
      sidebarNav: true
    }
  }
];

@NgModule({
  imports: [
    CommonModule,

    RouterModule.forChild(routes)
  ],
  declarations: [Menu21Component, Menu22Component]
})
export class Menu2Module {
}
