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

import {Component, Input, OnInit} from '@angular/core';

@Component({
  selector: 'spinner',
  template: `<div class="spinner" [class.spinner-shown]="shown">

    <!-- Loader 3 -->

    <svg version="1.1" id="L3" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" x="0px" y="0px"
  viewBox="0 0 100 100" enable-background="new 0 0 0 0" xml:space="preserve">
<circle fill="none" stroke="#fff" stroke-width="4" cx="50" cy="50" r="44" style="opacity:0.5;"/>
  <circle fill="#fff" stroke="#e74c3c" stroke-width="3" cx="8" cy="54" r="6" >
    <animateTransform
      attributeName="transform"
      dur="2s"
      type="rotate"
      from="0 50 48"
      to="360 50 52"
      repeatCount="indefinite" />
    
  </circle>
</svg>



  </div>`
})
export class SpinnerComponent implements OnInit {

  @Input()
  shown: boolean = false;

  constructor() {
  }

  ngOnInit() {
  }
}
