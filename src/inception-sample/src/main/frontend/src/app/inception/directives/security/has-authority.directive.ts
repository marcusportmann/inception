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

import {
  AfterContentInit,
  Directive,
  ElementRef, Input,
  TemplateRef,
  ViewContainerRef
} from '@angular/core';
import {SessionService} from "../../services/session/session.service";

/**
 * The HasAuthorityDirective class implements the has authority directive.
 *
 * @author Marcus Portmann
 */
@Directive({
  // tslint:disable-next-line
  selector: '[hasAuthority]'
})
export class HasAuthorityDirective  {

  /**
   * Constructs a new HasAuthorityDirective.
   *
   * @param templateRef    The template reference.
   * @param viewContainer  The view container for the element this directive is attached to.
   * @param sessionService The session service.
   */
  constructor(private templateRef: TemplateRef<any>,
        private viewContainer: ViewContainerRef,
        private sessionService: SessionService) {
  }

  @Input() set hasAuthority(authority: any) {


           // if (this.securityService.hasClaim(claimType)) {
           //    // Add template to DOM
           //    this.viewContainer.createEmbeddedView(this.templateRef);
           //  } else {
           //    // Remove template from DOM
           //     this.viewContainer.clear();
           //   }
        }
}
