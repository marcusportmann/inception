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
import {MatDialogRef, MAT_DIALOG_DATA} from '@angular/material';
import {Error} from "../../errors/error";
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {ErrorService} from "../../services/error/error.service";

@Component({
  selector: 'error-report-dialog',
  template: `
    <h1 mat-dialog-title >Error Report</h1>
    <div mat-dialog-content>
      <mat-form-field class="static">
        <mat-label>Timestamp</mat-label>
        <input matInput disabled value="{{data.timestamp | date:'medium'}}">
      </mat-form-field>
      <mat-form-field class="static">
        <mat-label>Message</mat-label>
        <input matInput disabled value="{{data.message}}">
      </mat-form-field>
      <mat-form-field class="static">
        <mat-label>Detail</mat-label>
        <textarea matInput cdkTextareaAutosize #autosize="cdkTextareaAutosize" cdkAutosizeMinRows="1" cdkAutosizeMaxRows="5" disabled value="{{data.detail}}"></textarea>
      </mat-form-field>
      <mat-form-field class="static">
        <mat-label>Additional Details</mat-label>
        <textarea matInput cdkTextareaAutosize #autosize="cdkTextareaAutosize" cdkAutosizeMinRows="2" cdkAutosizeMaxRows="5" [(ngModel)]="additionalDetail" autofocus></textarea>
      </mat-form-field>
    </div>
    <div mat-dialog-actions>
      <button mat-flat-button color="link" (click)="onCancelClick()">Cancel</button>
      <button mat-flat-button (click)="onSendClick()">Send</button>
    </div>    
  `,
})
export class ErrorReportDialog {

  additionalDetail: string;

  constructor(
    private errorService: ErrorService,
    private dialogRef: MatDialogRef<ErrorReportDialog>,
    @Inject(MAT_DIALOG_DATA) public data: Error) {

    console.log('errorService = ', errorService);

  }

  onCancelClick(): void {
    this.dialogRef.close();
  }

  onSendClick(): void {

    console.log('this.additionalDetail = ', this.additionalDetail);

    this.dialogRef.close();
  }
}
