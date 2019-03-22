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
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgModule } from '@angular/core';

// Import Angular classes
import {RouterModule, Routes} from "@angular/router";

// Import Inception modules
import {InceptionModule} from "../../inception.module";

// Import Inception components
import {SendErrorReportComponent} from './send-error-report.component';

const routes: Routes = [
  {
    path: 'send-error-report',
    component: SendErrorReportComponent,
    data: {
      title: 'Send Error Report'
    }
  }
];

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,

    InceptionModule,

    RouterModule.forChild(routes)
  ],
  exports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule
  ],
  declarations: [
    SendErrorReportComponent
  ]
})
export class ErrorModule { }
