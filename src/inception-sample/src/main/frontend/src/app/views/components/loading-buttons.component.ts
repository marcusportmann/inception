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

import { Component, ViewEncapsulation } from '@angular/core';

@Component({
  templateUrl: 'loading-buttons.component.html',
  encapsulation: ViewEncapsulation.None
})
export class LoadingButtonsComponent {

  contract: boolean = false;

  contractOverlay: boolean = false;

  expandDown: boolean = false;

  expandLeft: boolean = false;

  expandRight: boolean = false;

  expandRightSizes: boolean = false;

  expandUp: boolean = false;

  isLoading: boolean = false;

  slideDown: boolean = false;

  slideLeft: boolean = false;

  slideRight: boolean = false;

  slideUp: boolean = false;

  zoomIn: boolean = false;

  zoomOut: boolean = false;

  reset() {
    this.contract = false;
    this.contractOverlay = false;
    this.expandDown = false;
    this.expandLeft = false;
    this.expandRight = false;
    this.expandRightSizes = false;
    this.expandUp = false;
    this.isLoading = false;
    this.slideDown = false;
    this.slideLeft = false;
    this.slideRight = false;
    this.slideUp = false;
    this.zoomIn = false;
    this.zoomOut = false;
  }

  toggleContract() {
    this.contract = !this.contract;
  }

  toggleContractOverlay() {
    this.contractOverlay = !this.contractOverlay;
  }

  toggleExpandDown() {
    this.expandDown = !this.expandDown;
  }

  toggleExpandLeft() {
    this.expandLeft = !this.expandLeft;
  }

  toggleExpandRight() {
    this.expandRight = !this.expandRight;
  }

  toggleExpandRightSizes() {
    this.expandRightSizes = !this.expandRightSizes;
  }

  toggleExpandUp() {
    this.expandUp = !this.expandUp;
  }

  toggleLoading() {
    this.isLoading = !this.isLoading;
  }

  toggleSlideDown() {
    this.slideDown = !this.slideDown;
  }

  toggleSlideLeft() {
    this.slideLeft = !this.slideLeft;
  }

  toggleSlideRight() {
    this.slideRight = !this.slideRight;
  }

  toggleSlideUp() {
    this.slideUp = !this.slideUp;
  }

  toggleZoomIn() {
    this.zoomIn = !this.zoomIn;
  }

  toggleZoomOut() {
    this.zoomOut = !this.zoomOut;
  }
}
