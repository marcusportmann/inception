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
import {Menu321Component} from "./menu321.component";
import {Menu322Component} from "./menu322.component";

const routes: Routes = [
  {
    path: '',
    redirectTo: 'menu311',
    pathMatch: 'full',
  },
  {
    path: 'menu321',
    component: Menu321Component,
    data: {
      title: 'Menu 3.2.1',
    }
  },
  {
    path: 'menu322',
    component: Menu322Component,
    data: {
      title: 'Menu 3.2.2',
    }
  }
];

/**
 * The Menu32Module class implements a module that contains two components that can be loaded into
 * the AdminContainerComponent component when the corresponding menu option is selected.
 *
 * @author Marcus Portmann
 */
@NgModule({
  imports: [
    CommonModule,

    RouterModule.forChild(routes)
  ],
  declarations: [Menu321Component, Menu322Component]
})
export class Menu32Module {
}
