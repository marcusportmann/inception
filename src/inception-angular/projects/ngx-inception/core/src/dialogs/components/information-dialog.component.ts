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
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {DialogData} from '../services/dialog-data';

/**
 * The InformationDialogComponent class implements the information dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'information-dialog',
  template: `
    <div class="header">
      <i class="fas fa-3x fa-exclamation-circle"></i>
    </div>
    <div class="message-holder">
      <span class="message">
        {{ data.message }}
      </span>
    </div>
    <div class="button">
      <button mat-flat-button *ngIf="data.buttonText; else defaultButton"
              (click)="ok()"
              tabindex="-1">{{ data.buttonText }}
      </button>
      <ng-template #defaultButton>
        <button mat-flat-button color="primary" (click)="ok()" tabindex="-1"
                i18n="@@information_dialog_button_ok">
          OK
        </button>
      </ng-template>
    </div>
  `,
  host: {
    'class': 'information-dialog',
    '(document:keydown.enter)': 'onEnter($event)'
  },


  standalone: false
})
export class InformationDialogComponent {

  /**
   * Constructs a new InformationDialogComponent.
   *
   * @param dialogRef The dialog reference.
   * @param data      The dialog data.
   */
  constructor(private dialogRef: MatDialogRef<InformationDialogComponent>,
              @Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }

  ok(): void {
    this.dialogRef.close();
  }

  onEnter(event: KeyboardEvent) {
    event.preventDefault(); // Optional: prevent unintended form submissions
    this.ok();
  }
}
