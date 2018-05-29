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

// Import Inception components
import {Menu11Component} from "./menu11.component";

const routes: Routes = [
  {
    path: '',
    component: Menu11Component,
    data: {
      title: 'Menu 1.1'
    }
  }
];

@NgModule({
  imports: [
    CommonModule,

    RouterModule.forChild(routes)
  ],
  declarations: [Menu11Component]
})
export class Menu11Module {
}