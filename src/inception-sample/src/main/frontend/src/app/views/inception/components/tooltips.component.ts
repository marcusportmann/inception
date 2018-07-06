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

import {Component, SecurityContext} from '@angular/core';
import {DomSanitizer} from "@angular/platform-browser";

@Component({
  templateUrl: 'tooltips.component.html'
})
export class TooltipsComponent {

  content: string = 'Vivamus sagittis lacus vel augue laoreet rutrum faucibus.';

  html: string = `<span class="btn btn-danger">Never trust not sanitized HTML!!!</span>`;

  constructor(sanitizer: DomSanitizer) {
    this.html = sanitizer.sanitize(SecurityContext.HTML, this.html)
  }
}
