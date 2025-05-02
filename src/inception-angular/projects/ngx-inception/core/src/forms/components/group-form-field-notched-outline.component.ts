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
  AfterViewInit, ChangeDetectionStrategy, Component, ElementRef, Input, NgZone, ViewChild,
  ViewEncapsulation,
} from '@angular/core';

/**
 * Internal component that creates an instance of the MDC notched-outline component.
 *
 * The component sets up the HTML structure and styles for the notched-outline. It provides
 * inputs to toggle the notch state and width.
 */
@Component({
  // eslint-disable-next-line @angular-eslint/component-selector
  selector: 'div[groupFormFieldNotchedOutline]',
  templateUrl: './group-form-field-notched-outline.component.html', // eslint-disable-next-line
                                                                    // @angular-eslint/no-host-metadata-property
  host: {
    'class': 'mdc-notched-outline', // Besides updating the notch state through the MDC component,
                                    // we toggle this class through
    // a host binding in order to ensure that the notched-outline renders correctly on the server.
    '[class.mdc-notched-outline--notched]': 'groupFormFieldNotchedOutlineOpen',
  },
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None,
  standalone: false
})
export class GroupFormFieldNotchedOutlineComponent implements AfterViewInit {
  // @ts-ignore
  @ViewChild('notch') _notch: ElementRef;

  /** Whether the notch should be opened. */
  @Input() groupFormFieldNotchedOutlineOpen: boolean = false;

  constructor(private _elementRef: ElementRef<HTMLElement>, private _ngZone: NgZone) {
  }

  _setNotchWidth(labelWidth: number) {
    if (!this.groupFormFieldNotchedOutlineOpen || !labelWidth) {
      this._notch.nativeElement.style.width = '';
    } else {
      const NOTCH_ELEMENT_PADDING = 8;
      const NOTCH_ELEMENT_BORDER = 1;
      this._notch.nativeElement.style.width = `calc(${labelWidth}px * var(--mat-mdc-form-field-floating-label-scale, 0.75) + ${NOTCH_ELEMENT_PADDING + NOTCH_ELEMENT_BORDER}px)`;
    }
  }

  ngAfterViewInit(): void {
    const label = this._elementRef.nativeElement.querySelector<HTMLElement>('.mdc-floating-label');
    if (label) {
      this._elementRef.nativeElement.classList.add('mdc-notched-outline--upgraded');

      if (typeof requestAnimationFrame === 'function') {
        label.style.transitionDuration = '0s';
        this._ngZone.runOutsideAngular(() => {
          requestAnimationFrame(() => (label.style.transitionDuration = ''));
        });
      }
    } else {
      this._elementRef.nativeElement.classList.add('mdc-notched-outline--no-label');
    }
  }
}
