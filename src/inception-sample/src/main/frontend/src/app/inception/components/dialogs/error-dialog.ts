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

import {Component, Inject} from '@angular/core';
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {Error} from "../../errors/error";

/**
 * The ErrorDialogData interface defines the data that is displayed by an error dialog.
 *
 * @author Marcus Portmann
 */
export interface ErrorDialogData {

  /**
   * The error.
   */
  error: Error;
}

/**
 * The ErrorDialog class implements the error dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'error-dialog',
  template: `

    <div class="header">
      <i class="far fa-3x fa-times-circle"></i>
    </div>
    <div class="message-holder">
      <span class="message">
        {{message}}
      </span>
    </div>
    <div class="button">
      <button mat-flat-button (click)="onOkButtonClick()" tabindex="-1">Ok</button>
    </div>
  `,
  host: {
    'class': 'error-dialog'
  }
})
export class ErrorDialog {

  /**
   * Constructs a new ErrorDialog.
   *
   * @param {MatDialogRef<ErrorDialog>} dialogRef The dialog reference.
   * @param {ErrorDialogData} data                The dialog data.
   */
  constructor(
    private dialogRef: MatDialogRef<ErrorDialog>,
    @Inject(MAT_DIALOG_DATA) private data: ErrorDialogData) {
  }

  get message(): string {
    return this.data.error.message;
  }

  onOkButtonClick(): void {
    this.dialogRef.close();
  }
}

// TODO: Show submit button if we are dealing with anything that is not a CommunicationError or SystemUnavailableError? -- MARCUS