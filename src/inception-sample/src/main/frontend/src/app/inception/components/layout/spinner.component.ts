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

import {Component, ElementRef} from '@angular/core';

/**
 * The SpinnerComponent class implements the spinner component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'spinner',
  template: `<div class="spinner spinner-oval"></div>`,
  styles: [
    `
      .spinner {
        display: block;
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        z-index: 10000;
        background-color: rgba(0, 0, 0, 0.6);
        background-position: center center;
        background-size: 50px 50px;
        background-repeat: no-repeat, repeat;
      }
      
      .spinner-puff {
        background-image: url("data:image/svg+xml,%3Csvg width='44' height='44' viewBox='0 0 44 44' xmlns='http://www.w3.org/2000/svg' stroke='%23fff'%3E%3Cg fill='none' fill-rule='evenodd' stroke-width='2'%3E%3Ccircle cx='22' cy='22' r='1'%3E%3Canimate attributeName='r' begin='0s' dur='1.8s' values='1; 20' calcMode='spline' keyTimes='0; 1' keySplines='0.165, 0.84, 0.44, 1' repeatCount='indefinite' /%3E%3Canimate attributeName='stroke-opacity' begin='0s' dur='1.8s' values='1; 0' calcMode='spline' keyTimes='0; 1' keySplines='0.3, 0.61, 0.355, 1' repeatCount='indefinite' /%3E%3C/circle%3E%3Ccircle cx='22' cy='22' r='1'%3E%3Canimate attributeName='r' begin='-0.9s' dur='1.8s' values='1; 20' calcMode='spline' keyTimes='0; 1' keySplines='0.165, 0.84, 0.44, 1' repeatCount='indefinite' /%3E%3Canimate attributeName='stroke-opacity' begin='-0.9s' dur='1.8s' values='1; 0' calcMode='spline' keyTimes='0; 1' keySplines='0.3, 0.61, 0.355, 1' repeatCount='indefinite' /%3E%3C/circle%3E%3C/g%3E%3C/svg%3E");
      }
      
      .spinner-oval {
        background-image: url("data:image/svg+xml,%3Csvg width='38' height='38' viewBox='0 0 38 38' xmlns='http://www.w3.org/2000/svg' stroke='%23fff'%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg transform='translate(1 1)' stroke-width='2'%3E%3Ccircle stroke-opacity='.5' cx='18' cy='18' r='18'/%3E%3Cpath d='M36 18c0-9.94-8.06-18-18-18'%3E%3CanimateTransform attributeName='transform' type='rotate' from='0 18 18' to='360 18 18' dur='1s' repeatCount='indefinite'/%3E%3C/path%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
      }
      
      .spinner-tail-spin {
        background-image: url("data:image/svg+xml,%3Csvg width='38' height='38' viewBox='0 0 38 38' xmlns='http://www.w3.org/2000/svg'%3E%3Cdefs%3E%3ClinearGradient x1='8.042%25' y1='0%25' x2='65.682%25' y2='23.865%25' id='a'%3E%3Cstop stop-color='%23fff' stop-opacity='0' offset='0%25'/%3E%3Cstop stop-color='%23fff' stop-opacity='.631' offset='63.146%25'/%3E%3Cstop stop-color='%23fff' offset='100%25'/%3E%3C/linearGradient%3E%3C/defs%3E%3Cg fill='none' fill-rule='evenodd'%3E%3Cg transform='translate(1 1)'%3E%3Cpath d='M36 18c0-9.94-8.06-18-18-18' id='Oval-2' stroke='url(%23a)' stroke-width='2'%3E%3CanimateTransform attributeName='transform' type='rotate' from='0 18 18' to='360 18 18' dur='0.9s' repeatCount='indefinite' /%3E%3C/path%3E%3Ccircle fill='%23fff' cx='36' cy='18' r='1'%3E%3CanimateTransform attributeName='transform' type='rotate' from='0 18 18' to='360 18 18' dur='0.9s' repeatCount='indefinite' /%3E%3C/circle%3E%3C/g%3E%3C/g%3E%3C/svg%3E");
      }
    `
  ]
})
export class SpinnerComponent {

  /**
   * Constructs a new SpinnerComponent.
   *
   * @param elementRef The element reference.
   */
  constructor(private elementRef: ElementRef) {
  }

  show(): void {
    this.elementRef.nativeElement.focus();
  }
}
