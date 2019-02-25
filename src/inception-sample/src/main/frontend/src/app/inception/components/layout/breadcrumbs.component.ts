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

import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Replace} from '../../shared/index';
import {BreadcrumbsService} from "../../services/layout/breadcrumbs.service";

@Component({
  selector: 'breadcrumbs',
  template: `
    <ol class="breadcrumb">
      <ng-template ngFor let-breadcrumb [ngForOf]="breadcrumbs | async" let-last = last>
        <li class="breadcrumb-item"
            *ngIf="(breadcrumb.label)"
            [ngClass]="{active: last}">
          <a *ngIf="!last" [routerLink]="breadcrumb.url">{{breadcrumb.label}}</a>
          <span *ngIf="last" [routerLink]="breadcrumb.url">{{breadcrumb.label}}</span>
        </li>
      </ng-template>
    </ol>
  `
})
export class BreadcrumbsComponent implements OnInit {
  @Input() fixed: boolean;
  public breadcrumbs;

  constructor(public breadcrumbsService: BreadcrumbsService, public el: ElementRef) { }

  public ngOnInit(): void {
    Replace(this.el);
    this.isFixed(this.fixed);
    this.breadcrumbs = this.breadcrumbsService.breadcrumbs;
  }

  isFixed(fixed: boolean): void {
    if (this.fixed) { document.querySelector('body').classList.add('breadcrumbs-fixed'); }
  }
}
