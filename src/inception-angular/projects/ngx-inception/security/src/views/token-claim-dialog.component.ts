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

import { Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CoreModule, ValidatedFormDirective } from 'ngx-inception/core';
import { TokenClaim } from '../services/token-claim';

/**
 * The TokenClaimDialogData interface defines the data displayed by a token claim dialog.
 *
 * @author Marcus Portmann
 */
export interface TokenClaimDialogData {
  /**
   * The name of the token claim.
   */
  name: string;

  /**
   * Is the name of the token claim readonly?
   */
  readonlyName: boolean;

  /**
   * The single value for the token claim.
   */
  value: string;

  /**
   * The values for the token claim.
   */
  values: string[];
}

/**
 * The TokenClaimDialogComponent class implements the token claim dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-token-claim-dialog',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  template: `
    <div class="mat-mdc-dialog-title"><span>Token Claim</span></div>
    <div class="mat-mdc-dialog-content">
      <form
        [formGroup]="tokenClaimForm"
        autocomplete="off"
        class="d-flex flex-column flex-fill"
        validatedForm>
        <div class="row">
          <div class="col-sm">
            <mat-form-field>
              <mat-label i18n="@@security_token_claim_dialog_component_label_name">
                Name
              </mat-label>
              <input type="text" matInput formControlName="name" required="true" />
              @if (nameControl.errors && !nameControl.untouched) {
                <mat-error>
                  @if (nameControl.errors['required']) {
                    <span i18n="@@security_token_claim_dialog_component_error_name_required">
                      A name is required.
                    </span>
                  }
                  @if (nameControl.errors['maxlength']) {
                    <span i18n="@@security_token_claim_dialog_component_error_name_maxlength">
                      Name must not exceed 100 characters.
                    </span>
                  }
                </mat-error>
              }
            </mat-form-field>
          </div>
        </div>
        <div class="row">
          <div class="col-sm">
            <mat-form-field>
              <mat-label i18n="@@security_token_claim_dialog_component_label_value">
                Value
              </mat-label>
              <input type="text" matInput formControlName="value" />
              @if (valueControl.errors && !valueControl.untouched) {
                <mat-error>
                  @if (valueControl.errors['maxlength']) {
                    <span i18n="@@security_token_claim_dialog_component_error_value_maxlength">
                      Value must not exceed 4000 characters.
                    </span>
                  }
                </mat-error>
              }
            </mat-form-field>
          </div>
        </div>
        <div class="row">
          <div class="col-sm">
            <mat-form-field>
              <mat-label i18n="@@security_token_claim_dialog_component_label_values">
                Values
              </mat-label>
              <textarea
                matInput
                formControlName="values"
                cdkTextareaAutosize
                #autosize="cdkTextareaAutosize"
                cdkAutosizeMinRows="5"
                cdkAutosizeMaxRows="5"></textarea>
              @if (valuesControl.errors && !valuesControl.untouched) {
                <mat-error>
                  @if (valuesControl.errors['maxlength']) {
                    <span i18n="@@security_token_claim_dialog_component_error_values_maxlength">
                      Value must not exceed 4000 characters.
                    </span>
                  }
                </mat-error>
              }
            </mat-form-field>
          </div>
        </div>
      </form>
    </div>
    <div class="mat-mdc-dialog-actions">
      <button
        type="button"
        mat-flat-button
        color="link"
        (click)="cancel()"
        tabindex="-1"
        i18n="@@security_token_claim_dialog_component_button_cancel">
        Cancel
      </button>
      <button
        type="button"
        mat-flat-button
        color="primary"
        [disabled]="!tokenClaimForm.valid"
        (click)="ok()"
        tabindex="-1"
        i18n="@@security_token_claim_dialog_component_button_ok">
        OK
      </button>
    </div>
  `
})
export class TokenClaimDialogComponent {
  nameControl: FormControl;

  tokenClaimForm: FormGroup;

  valueControl: FormControl;

  valuesControl: FormControl;

  private dialogRef = inject<MatDialogRef<TokenClaimDialogComponent>>(MatDialogRef);

  /**
   * Constructs a new TokenClaimDialogComponent.
   *
   * @param dialogRef The dialog reference.
   * @param data      The dialog data.
   */
  constructor() {
    const data = inject<TokenClaimDialogData>(MAT_DIALOG_DATA);

    // Initialize the form controls
    this.nameControl = new FormControl(
      {
        value: data.name,
        disabled: data.readonlyName
      },
      [Validators.required, Validators.maxLength(100)]
    );
    this.valueControl = new FormControl(data.value, [Validators.maxLength(4000)]);
    this.valuesControl = new FormControl(data.values.join(','), [Validators.maxLength(4000)]);

    // Initialize the form
    this.tokenClaimForm = new FormGroup({
      name: this.nameControl,
      value: this.valueControl,
      values: this.valuesControl
    });
  }

  cancel(): void {
    this.dialogRef.close(undefined);
  }

  ok(): void {
    const tokenClaim: TokenClaim = new TokenClaim(
      this.nameControl.value,
      this.valueControl.value,
      this.valuesControl.value.split(',')
    );

    this.dialogRef.close(tokenClaim);
  }
}
