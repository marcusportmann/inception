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

import { Overlay, OverlayRef } from '@angular/cdk/overlay';
import { ComponentPortal } from '@angular/cdk/portal';
import { inject, Injectable } from '@angular/core';
import { SpinnerComponent } from '../components/spinner.component';

/**
 * The Spinner Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable({
  providedIn: 'root'
})
export class SpinnerService {
  private counter = 0; // Reference count

  private overlay = inject(Overlay);

  private overlayRef?: OverlayRef;

  private readonly spinnerComponentPortal?: ComponentPortal<SpinnerComponent>;

  constructor() {
    console.log('Initializing the Spinner Service');
    this.spinnerComponentPortal = new ComponentPortal(SpinnerComponent);
  }

  /**
   * Hide the spinner.
   */
  hideSpinner(): void {
    if (this.counter > 0) {
      this.counter--;
    }

    if (this.counter === 0 && this.overlayRef) {
      this.overlayRef.detach();
      this.overlayRef.dispose();
      this.overlayRef = undefined;
    }
  }

  /**
   * Reset the spinner reference counter and hide the spinner.
   */
  // noinspection JSUnusedGlobalSymbols
  resetSpinner(): void {
    this.counter = 0;
    if (this.overlayRef) {
      this.overlayRef.detach();
      this.overlayRef.dispose();
      this.overlayRef = undefined;
    }
  }

  /**
   * Show the spinner.
   */
  showSpinner(): void {
    this.counter++;
    if (!this.overlayRef) {
      this.overlayRef = this.overlay.create();
      this.overlayRef.attach(this.spinnerComponentPortal);
    }
  }
}
