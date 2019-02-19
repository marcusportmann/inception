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

import { Injectable } from "@angular/core";

import {
  ErrorDialog,
  InformationDialog,
  WarningDialog
} from "../../components/dialogs/index";

import {MatDialog, MatDialogRef} from '@angular/material';

import {Error} from "../../errors/error";
import {DialogData} from "../../components/dialogs/dialog-data";

/**
 * The DialogService class provides the capability to show different standard dialogs.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class DialogService {

  /**
   * Constructs a new DialogService.
   *
   * @param {dialog} dialog The material dialog.
   */
  constructor(public dialog: MatDialog) {
  }

  /**
   * Show an error using the error dialog.
   *
   * @param {Error} error The error.
   */
  showErrorDialog(error: Error): MatDialogRef<ErrorDialog> {

    const dialogRef = this.dialog.open(ErrorDialog, {
      width: '360px',
      data: {error: error}
    });

    return dialogRef;
  }

  /**
   * Show an informational message using the information dialog.
   *
   * @param {DialogData} data The data.
   */
  showInformationDialog(data: DialogData): MatDialogRef<InformationDialog> {

    const dialogRef = this.dialog.open(InformationDialog, {
      width: '360px',
      data: data
    });

    return dialogRef;
  }

  /**
   * Show a warning using the warning dialog.
   *
   * @param {DialogData} data The data.
   */
  showWarningDialog(data: DialogData): MatDialogRef<WarningDialog> {

    const dialogRef = this.dialog.open(WarningDialog, {
      width: '360px',
      data: data
    });

    return dialogRef;
  }
}
