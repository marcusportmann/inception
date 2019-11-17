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
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import {FormControl, FormGroup, Validators} from "@angular/forms";
import {v4 as uuid} from "uuid/interfaces";

/**
 * The JobParameterDialogData interface defines the data that is displayed by a job parameter
 * dialog.
 *
 * @author Marcus Portmann
 */
export interface JobParameterDialogData {

  /**
   * The name of the job parameter.
   */
  name: string;

  /**
   * The value of the job parameter.
   */
  value: string;
}

/**
 * The JobParameterDialogComponent class implements the job parameter dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'job-parameter-dialog',
  template: `
    <div class="header"><span>Job Parameter</span></div>
    <div class="content">
      <form [formGroup]="jobParameterForm" autocomplete="off" class="d-flex flex-column flex-fill" validatedForm>
        <div class="row">
          <div class="col-sm">
            <mat-form-field>
              <mat-label i18n="@@scheduler_job_parameter_dialog_component_label_name">Name</mat-label>
              <input type="text" matInput formControlName="name" required="true">
              <mat-error *ngIf="jobParameterForm.get('name').errors && !jobParameterForm.get('name').untouched">
                <span *ngIf="jobParameterForm.get('name').errors?.required" i18n="@@scheduler_job_parameter_dialog_component_error_name_required">A name is required.</span>
                <span *ngIf="jobParameterForm.get('name').errors?.maxlength" i18n="@@scheduler_job_parameter_dialog_component_error_name_maxlength">Name must not exceed 100 characters.</span>
              </mat-error>
            </mat-form-field>
          </div>
        </div>
        <div class="row">
          <div class="col-sm">
            <mat-form-field>
              <mat-label i18n="@@scheduler_job_parameter_dialog_component_label_value">Value</mat-label>
              <input type="text" matInput formControlName="value">
              <mat-error *ngIf="jobParameterForm.get('value').errors && !jobParameterForm.get('value').untouched">
                <span *ngIf="jobParameterForm.get('value').errors?.maxlength" i18n="@@scheduler_job_parameter_dialog_component_error_value_maxlength">Value must not exceed 100 characters.</span>
              </mat-error>
            </mat-form-field>
          </div>
        </div>
      </form>
    </div>
    <div class="button">
      <button type="button" mat-flat-button color="link" (click)="onCancel()" tabindex="-1" i18n="@@scheduler_job_parameter_dialog_component_button_cancel">Cancel</button>
      <button type="button" mat-flat-button color="primary" [disabled]="!jobParameterForm.valid"  (click)="onOk()" tabindex="-1" i18n="@@scheduler_job_parameter_dialog_component_button_ok">OK</button>
    </div>
  `, // tslint:disable-next-line
  host: {
    'class': 'application-dialog'
  }
})
export class JobParameterDialogComponent {

  jobParameterForm: FormGroup;

  /**
   * Constructs a new JobParameterDialogComponent.
   *
   * @param dialogRef The dialog reference.
   * @param data      The dialog data.
   */
  constructor(private dialogRef: MatDialogRef<JobParameterDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: JobParameterDialogData) {

    // Initialise the form
    this.jobParameterForm = new FormGroup({
      name: new FormControl('', [Validators.required, Validators.maxLength(100)]),
      value: new FormControl('', [Validators.maxLength(100)])
    });
  }

  onCancel(): void {
    this.dialogRef.close(false);
  }

  onOk(): void {
    this.dialogRef.close(true);
  }
}


