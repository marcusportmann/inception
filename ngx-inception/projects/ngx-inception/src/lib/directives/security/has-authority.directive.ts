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

import {Directive, Input, OnDestroy, OnInit, TemplateRef, ViewContainerRef} from '@angular/core';
import {SessionService} from '../../services/session/session.service';
import {Subscription} from 'rxjs';
import {Session} from '../../services/session/session';
import {first} from 'rxjs/operators';

/**
 * The HasAuthorityDirective class implements the has authority directive.
 *
 * @author Marcus Portmann
 */
@Directive({
  // tslint:disable-next-line
  selector: '[hasAuthority]'
})
export class HasAuthorityDirective implements OnDestroy, OnInit {

  private subscriptions: Subscription = new Subscription();

  @Input('hasAuthority') requiredAuthorities: string[] | undefined;

  /**
   * Constructs a new HasAuthorityDirective.
   *
   * @param templateRef    The template reference.
   * @param viewContainer  The view container for the element this directive is attached to.
   * @param sessionService The session service.
   */
  // tslint:disable-next-line
  constructor(private templateRef: TemplateRef<any>, private viewContainer: ViewContainerRef, private sessionService: SessionService) {
  }


  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  ngOnInit(): void {
    if (this.requiredAuthorities && (this.requiredAuthorities.length > 0)) {
      this.subscriptions.add(this.sessionService.session$.pipe(first()).subscribe((session: (Session | null)) => {
        if (session) {
          let foundAuthority = false;

          for (const requiredAuthority of this.requiredAuthorities) {
            if (session.hasAuthority(requiredAuthority)) {
              foundAuthority = true;
              break;
            }
          }

          if (foundAuthority) {
            this.viewContainer.createEmbeddedView(this.templateRef);
          } else {
            this.viewContainer.clear();
          }
        }
      }));
    } else {
      this.viewContainer.createEmbeddedView(this.templateRef);
    }
  }
}
