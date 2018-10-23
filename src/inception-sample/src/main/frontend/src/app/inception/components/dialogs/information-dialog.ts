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
import {ErrorReportingService} from "../../services/error-reporting/error-reporting.service";
import {DialogData} from "./dialog-data";

@Component({
  selector: 'information-dialog',
  template: `

    <div class="header">
      <i class="material-icons md-48">announcement</i>
    </div>
    <div class="message">
      {{data.title}}
    </div>
    <div class="description-holder">
      <span class="description">
        {{data.description}}
      </span>
    </div>
    <div class="button">
      <button mat-flat-button (click)="onButtonClick()" tabindex="-1">{{ data.buttonText ? data.buttonText : 'Ok'}}</button>
    </div>
  `,
  host: {
    'class': 'information-dialog'
  }
})
export class InformationDialog {

  constructor(
    private dialogRef: MatDialogRef<InformationDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  onButtonClick(): void {
    this.dialogRef.close();
  }
}
