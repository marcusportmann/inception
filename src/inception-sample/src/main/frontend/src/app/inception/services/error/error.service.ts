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

//import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { ErrorReportDialog } from "../../components/error/index";

import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

import {Error} from "../../errors/error";

/**
 * The ErrorService class provides the capability to process an application error or back-end error
 * and submit an error report.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ErrorService {

  /**
   * Constructs a new ErrorService.
   *
   * @param {BsModalService} bsModalService The Bootstrap Modal Service.
   */
  constructor(public dialog: MatDialog) {
  }

  /**
   * Show the specified error using the error report modal
   * .
   * @param {Error} error The error.
   */
  showErrorReport(error: Error) {

    console.log('Error: ', error);

    const dialogRef = this.dialog.open(ErrorReportDialog, {
      width: '250px',
      data: error
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      //this.animal = result;
    });


    // let bsModalRef:BsModalRef = this.bsModalService.show(ErrorReportModalComponent, { animated: true, keyboard: true, backdrop: true, ignoreBackdropClick: false });
    //
    // (<ErrorReportModalComponent>bsModalRef.content).timestamp = error.timestamp;
    // (<ErrorReportModalComponent>bsModalRef.content).message = error.message;
    //
    // if (error.detail) {
    //   (<ErrorReportModalComponent>bsModalRef.content).detail = error.detail;
    // }
    //
    // if (error.stackTrace) {
    //   (<ErrorReportModalComponent>bsModalRef.content).stackTrace = error.stackTrace;
    // }
  }
}
