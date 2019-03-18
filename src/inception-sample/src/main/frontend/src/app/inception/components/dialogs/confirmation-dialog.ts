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
 * The ConfirmationDialogData interface defines the data that is displayed by a confirmation dialog.
 *
 * @author Marcus Portmann
 */
export interface ConfirmationDialogData {

  /**
   * The message.
   */
  message: string;
}

/**
 * The ConfirmationDialog class implements the confirmation dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'confirmation-dialog',
  template: `
    <div class="header">
      <i class="far fa-3x fa-question-circle"></i>
    </div>
    <div class="message-holder">
      <span class="message">
        {{message}}
      </span>
    </div>
    <div class="button">
      <button mat-flat-button (click)="onNoButtonClick()" tabindex="-1" i18n="@@confirmation_dialog_button_no">No</button>
      <button mat-flat-button (click)="onYesButtonClick()" tabindex="-1" i18n="@@confirmation_dialog_button_yes">Yes</button>
    </div>
  `,
  host: {
    'class': 'confirmation-dialog'
  }
})
export class ConfirmationDialog {

  /**
   * Constructs a new ConfirmationDialog.
   *
   * @param {MatDialogRef<ConfirmationDialog>} dialogRef The dialog reference.
   * @param {ConfirmationDialogData} data                The dialog data.
   */
  constructor(
    private dialogRef: MatDialogRef<ConfirmationDialog>,
    @Inject(MAT_DIALOG_DATA) private data: ConfirmationDialogData) {
  }

  get message(): string {
    return this.data.message;
  }

  onNoButtonClick(): void {
    this.dialogRef.close(false);
  }

  onYesButtonClick(): void {
    this.dialogRef.close(true);
  }
}


