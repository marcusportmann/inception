/*
 * Copyright 2021 Marcus Portmann
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
import {ModuleWithProviders, NgModule} from '@angular/core';
import {CoreModule} from '../core/core.module';
import {ConfirmationDialogComponent} from './components/confirmation-dialog.component';
import {ErrorDialogComponent} from './components/error-dialog.component';
import {InformationDialogComponent} from './components/information-dialog.component';
import {WarningDialogComponent} from './components/warning-dialog.component';
import {DialogService} from "./services/dialog.service";

/**
 * The DialogModule class implements the Inception Dialog Module.
 *
 * @author Marcus Portmann
 */
@NgModule({
  declarations: [
    // Components
    ConfirmationDialogComponent, ErrorDialogComponent, InformationDialogComponent, WarningDialogComponent
  ],
  imports: [
    // Angular modules
    CommonModule,

    // Inception modules
    CoreModule
  ],
  exports: [],
  entryComponents: [
    // Components
    ConfirmationDialogComponent, ErrorDialogComponent, InformationDialogComponent,
    WarningDialogComponent
  ]
})
export class DialogModule {
  constructor() {
  }

  static forRoot(): ModuleWithProviders<DialogModule> {
    return {
      ngModule: DialogModule,
      providers: [
        DialogService
      ]
    };
  }
}
