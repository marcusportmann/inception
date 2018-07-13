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

import {Component, Inject} from '@angular/core';
import {MatDialog, MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';

@Component({
  selector: 'error-report-dialog',
  template: `
    <h1 mat-dialog-title>Error Report</h1>
    <div mat-dialog-content>
      <mat-form-field>
        <input matInput placeholder="Timestamp" disabled value="{{data.timestamp | date:'medium'}}">
      </mat-form-field>    
    </div>
    <div mat-dialog-actions>
      <button mat-button (click)="onCancelClick()">Cancel</button>
      <button mat-button (click)="onSendClick()" cdkFocusInitial>Send</button>
    </div>    
  `,
})
export class ErrorReportDialog {

  constructor(
    public dialogRef: MatDialogRef<ErrorReportDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Error) {}

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSendClick(): void {
    this.dialogRef.close();
  }
}
