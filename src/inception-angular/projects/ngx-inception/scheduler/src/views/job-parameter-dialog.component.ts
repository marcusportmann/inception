/*
 * Copyright Marcus Portmann
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
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {JobParameter} from '../services/job-parameter';

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
   * Is the name of the job parameter readonly?
   */
  readonlyName: boolean;

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
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'job-parameter-dialog',
  template: `
      <div class="mat-mdc-dialog-title"><span>Job Parameter</span></div>
      <div class="mat-mdc-dialog-content">
          <form [formGroup]="jobParameterForm" autocomplete="off"
                class="d-flex flex-column flex-fill"
                validatedForm>
              <div class="row">
                  <div class="col-sm">
                      <mat-form-field>
                          <mat-label i18n="@@scheduler_job_parameter_dialog_component_label_name">
                              Name
                          </mat-label>
                          <input type="text" matInput formControlName="name" required="true">
                          <mat-error *ngIf="nameControl.errors && !nameControl.untouched">
                <span *ngIf="nameControl.errors?.['required']"
                      i18n="@@scheduler_job_parameter_dialog_component_error_name_required">
                  A name is required.
                </span>
                              <span *ngIf="nameControl.errors?.['maxlength']"
                                    i18n="@@scheduler_job_parameter_dialog_component_error_name_maxlength">
                  Name must not exceed 100 characters.
                </span>
                          </mat-error>
                      </mat-form-field>
                  </div>
              </div>
              <div class="row">
                  <div class="col-sm">
                      <mat-form-field>
                          <mat-label i18n="@@scheduler_job_parameter_dialog_component_label_value">
                              Value
                          </mat-label>
                          <input type="text" matInput formControlName="value">
                          <mat-error *ngIf="valueControl.errors && !valueControl.untouched">
                <span *ngIf="valueControl.errors?.['maxlength']"
                      i18n="@@scheduler_job_parameter_dialog_component_error_value_maxlength">
                  Value must not exceed 100 characters.
                </span>
                          </mat-error>
                      </mat-form-field>
                  </div>
              </div>
          </form>
      </div>
      <div class="mat-mdc-dialog-actions">
          <button type="button" mat-flat-button color="link" (click)="cancel()" tabindex="-1"
                  i18n="@@scheduler_job_parameter_dialog_component_button_cancel">Cancel
          </button>
          <button type="button" mat-flat-button color="primary" [disabled]="!jobParameterForm.valid"
                  (click)="ok()" tabindex="-1"
                  i18n="@@scheduler_job_parameter_dialog_component_button_ok">OK
          </button>
      </div>
  `
})
export class JobParameterDialogComponent {

  jobParameterForm: FormGroup;

  nameControl: FormControl;

  valueControl: FormControl;

  /**
   * Constructs a new JobParameterDialogComponent.
   *
   * @param dialogRef The dialog reference.
   * @param data      The dialog data.
   */
  constructor(private dialogRef: MatDialogRef<JobParameterDialogComponent>,
              @Inject(MAT_DIALOG_DATA) private data: JobParameterDialogData) {

    // Initialise the form controls
    this.nameControl = new FormControl({
      value: data.name,
      disabled: data.readonlyName
    }, [Validators.required, Validators.maxLength(100)]);
    this.valueControl = new FormControl(data.value, [Validators.maxLength(100)]);

    // Initialise the form
    this.jobParameterForm = new FormGroup({
      name: this.nameControl,
      value: this.valueControl
    });
  }

  cancel(): void {
    this.dialogRef.close(undefined);
  }

  ok(): void {
    const jobParameter: JobParameter = new JobParameter(this.nameControl.value,
      this.valueControl.value);

    this.dialogRef.close(jobParameter);
  }
}


