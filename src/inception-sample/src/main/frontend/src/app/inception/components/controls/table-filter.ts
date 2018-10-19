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

import {
  Component,
  EventEmitter,
  Input,
  Output
} from '@angular/core';

@Component({
  selector: 'table-filter',
  template: `

    <div class="table-filter-container">
      <div class="table-filter-icon">
        <i class="fa fa-search"></i>
      </div>
      <input class="table-filter-input" matInput [value]="this.value"  #tableFilterInput (keyup)="updateValue($event.target.value)" placeholder="Search..." autocomplete="off">
      <button class="table-filter-reset" mat-icon-button *ngIf="value" (click)="resetValue()">
        <i class="fa fa-times"></i>
      </button>
    </div>
  `,
  styles: [`

    .table-filter-container {
      display: flex; 
      align-items: center;
      color: rgba(0, 0, 0, 0.42);
      border: 1px solid rgba(0, 0, 0, 0.12); 
      border-radius: 2px; 
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
      width: 28px; height: 28px; 
      line-height: 28px; 
      min-width: 28px; 
      padding: 0; 
      border: none !important;
    }

  `]
})
export class TableFilter {

  @Input()
  value: string = '';

  @Output()
  changed: EventEmitter<string> = new EventEmitter<string>();

  resetValue(): void {
    this.value = '';
    this.changed.emit(this.value);
  }

  updateValue(newValue: string): void {
    this.value = newValue;
    this.changed.emit(this.value);
  }
}
