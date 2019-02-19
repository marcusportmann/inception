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

import {Injectable, ViewContainerRef} from "@angular/core";
import {SpinnerComponent} from "../../components/layout";
import {Overlay, OverlayRef} from "@angular/cdk/overlay";
import {ComponentPortal} from "@angular/cdk/portal";

/**
 * The SpinnerService class provides the capability to show and hide the spinner used to indicate
 * to a user that the application is busy processing.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class SpinnerService {

  private overlayRef: OverlayRef;

  private spinnerPortal: ComponentPortal<SpinnerComponent>;


  /**
   * Constructs a new ErrorReportingService.
   *
   * @param {dialog} dialog The material dialog.
   */
  constructor(private overlay: Overlay) {
  }

  /**
   * Show the spinner.
   *
   * @param {ViewContainerRef} viewContainerRef The reference to the view container to associate
   *                                            the spinner with.
   */
  show() {

    if (!this.overlayRef) {

      // Returns an OverlayRef (which is a PortalHost)
      this.overlayRef = this.overlay.create();

      // Create ComponentPortal that can be attached to a PortalHost
      this.spinnerPortal = new ComponentPortal(SpinnerComponent);

      // Attach ComponentPortal to PortalHost
      this.overlayRef.attach(this.spinnerPortal);
    }
  }

  /**
   * Hide the spinner.
   */
  hide() {
    if (this.overlayRef) {
      this.overlayRef.dispose();

      this.overlayRef = null;
    }
  }
}
