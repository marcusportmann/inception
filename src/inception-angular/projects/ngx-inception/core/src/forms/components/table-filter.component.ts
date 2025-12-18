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

import {
  Component, ElementRef, EventEmitter, Input, OnDestroy, OnInit, Output, ViewChild
} from '@angular/core';
import { MatIconButton } from '@angular/material/button';
import { MatInput } from '@angular/material/input';
import { fromEvent, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';

@Component({
  selector: 'inception-core-table-filter',
  standalone: true,
  imports: [MatIconButton, MatInput],
  template: `
    <div class="table-filter-container">
      <div class="table-filter-icon">
        <i class="fa fa-search"></i>
      </div>
      <input
        class="table-filter-input"
        matInput
        #tableFilterInput
        placeholder="Search..."
        autocomplete="off" />
      @if (filter) {
        <button class="table-filter-reset" mat-icon-button (click)="reset(true)">
          <i class="fa fa-times"></i>
        </button>
      }
    </div>
  `,
  styles: [
    `
      .table-filter-container {
        display: flex;
        align-items: center;
        color: rgba(0, 0, 0, 0.42);
      }

      .table-filter-icon {
        display: flex;
        width: 28px;
        height: 28px;
        align-items: center;
        justify-content: center;
        border: none;
      }

      .table-filter-input {
        -webkit-appearance: none;
        border-radius: 0;
        width: 12em;
        height: 28px;
        padding-left: 2px;
        padding-right: 28px;
        border: none;
        color: inherit;
        background-color: transparent;
      }

      .table-filter-reset {
        position: absolute;
        right: 16px;
        width: 28px;
        height: 28px;
        line-height: 28px;
        min-width: 28px;
        padding: 0;
        border: none !important;
      }
    `
  ]
})
export class TableFilterComponent implements OnInit, OnDestroy {
  /** Emitted when the user changes the filter (or clicks reset with emitEvent=true). */
  @Output() changed: EventEmitter<string> = new EventEmitter<string>();

  filter = '';

  @ViewChild('tableFilterInput', { static: true })
  tableFilterInput!: ElementRef<HTMLInputElement>;

  private tableFilterInputSubscription?: Subscription;

  @Input()
  get value(): string {
    return this.filter;
  }

  set value(val: string) {
    this.filter = val ?? '';
    this.syncInput();
  }

  ngOnDestroy(): void {
    this.tableFilterInputSubscription?.unsubscribe();
  }

  ngOnInit(): void {
    this.tableFilterInputSubscription = fromEvent<KeyboardEvent>(
      this.tableFilterInput.nativeElement,
      'keyup'
    )
      .pipe(debounceTime(250), distinctUntilChanged())
      .subscribe(() => {
        this.filter = this.tableFilterInput.nativeElement.value;
        this.changed.emit(this.filter);
      });

    this.syncInput();
  }

  reset(emitEvent: boolean): void {
    this.filter = '';
    this.syncInput();

    if (emitEvent) {
      this.changed.emit(this.filter);
    }
  }

  private syncInput(): void {
    if (this.tableFilterInput?.nativeElement) {
      this.tableFilterInput.nativeElement.value = this.filter;
    }
  }
}
