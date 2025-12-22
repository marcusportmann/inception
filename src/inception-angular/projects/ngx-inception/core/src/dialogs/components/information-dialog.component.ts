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
import { MatButton } from '@angular/material/button';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DialogData } from '../services/dialog-data';

/**
 * The InformationDialogComponent class implements the information dialog component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-core-information-dialog',
  standalone: true,
  imports: [MatButton],
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
      @if (data.buttonText) {
        <button mat-flat-button (click)="ok()" tabindex="-1">
          {{ data.buttonText }}
        </button>
      } @else {
        <button
          mat-flat-button
          color="primary"
          (click)="ok()"
          tabindex="-1"
          i18n="@@information_dialog_button_ok">
          OK
        </button>
      }
    </div>
  `,
  host: {
    class: 'information-dialog',
    '(document:keydown.enter)': 'onEnter($event)'
  }
})
export class InformationDialogComponent {
  readonly data = inject<DialogData>(MAT_DIALOG_DATA);

  private readonly dialogRef = inject<MatDialogRef<InformationDialogComponent>>(MatDialogRef);

  ok(): void {
    this.dialogRef.close();
  }

  onEnter(event: Event): void {
    const keyboardEvent = event as KeyboardEvent;
    // Optional: prevent unintended form submissions
    keyboardEvent.preventDefault();
    this.ok();
  }
}
