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

import { Injectable } from "@angular/core";

import {ErrorDialog, ErrorReportDialog, WarningDialog} from "../../components/dialogs/index";

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
   * @param {DialogData} data The data.
   */
  showErrorDialog(data: DialogData): MatDialogRef<ErrorDialog> {

    const dialogRef = this.dialog.open(ErrorDialog, {
      width: '300px',
      data: data
    });

    return dialogRef;
  }

  /**
   * Show the specified error using the error report dialog.
   *
   * @param {Error} error The error.
   */
  showErrorReportDialog(error: Error): MatDialogRef<ErrorReportDialog> {

    const dialogRef = this.dialog.open(ErrorReportDialog, {
      width: '300px',
      data: error
    });

    return dialogRef;

    // dialogRef.afterClosed().subscribe(result => {
    //   console.log('The dialog was closed = ', result);
    //   //this.animal = result;
    // });
  }

  /**
   * Show a warning using the warning dialog.
   *
   * @param {DialogData} data The data.
   */
  showWarningDialog(data: DialogData): MatDialogRef<WarningDialog> {

    const dialogRef = this.dialog.open(WarningDialog, {
      width: '300px',
      data: data
    });

    return dialogRef;
  }
}
