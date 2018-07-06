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

import {Component} from '@angular/core';

@Component({
  templateUrl: 'dropdown-buttons.component.html',
})
export class DropdownButtonsComponent {

  autoClose: boolean = false;

  disabled: boolean = false;

  isDropup: boolean = true;

  items: string[] = [
    'The first choice!',
    'And another choice for you.',
    'but wait! A third!'
  ];

  status: { isOpen: boolean } = {isOpen: false};

  constructor() {
  }

  change(value: boolean): void {
    this.status.isOpen = value;
  }

  isOpenChange(): void {
    console.log('Dropdown state is changed');
  }

  onHidden(): void {
    console.log('Dropdown is hidden');
  }

  onShown(): void {
    console.log('Dropdown is shown');
  }

  toggleDropdown($event: MouseEvent): void {
    $event.preventDefault();
    $event.stopPropagation();
    this.status.isOpen = !this.status.isOpen;
  }
}
