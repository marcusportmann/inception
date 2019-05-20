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
import {Replace} from '../../shared';
import {BreadcrumbsService} from '../../services/layout/breadcrumbs.service';
import {Observable} from 'rxjs';
import {TitleService} from '../../services/layout/title.service';

/**
 * The BreadcrumbsComponent class implements the breadcrumbs component.
 *
 * @author Marcus Portmann
 */
@Component({
  // tslint:disable-next-line
  selector: 'breadcrumbs',
  template: `
    <ol *ngIf="title | async as title; else noTitle" class="breadcrumb">
      <ng-template ngFor let-breadcrumb [ngForOf]="breadcrumbs | async">
        <li class="breadcrumb-item"
            *ngIf="(breadcrumb.label)">
          <a [routerLink]="breadcrumb.url">{{breadcrumb.label}}</a>
        </li>
      </ng-template>
      <li class="breadcrumb-item">
        <span>{{title}}</span>
      </li>
    </ol>

    <ng-template #noTitle>
      <ol class="breadcrumb">
        <ng-template ngFor let-breadcrumb [ngForOf]="breadcrumbs | async" let-last=last>
          <li class="breadcrumb-item"
              *ngIf="(breadcrumb.label)"
              [ngClass]="{active: last}">
            <a *ngIf="!last" [routerLink]="breadcrumb.url">{{breadcrumb.label}}</a>
            <span *ngIf="last" [routerLink]="breadcrumb.url">{{breadcrumb.label}}</span>
          </li>
        </ng-template>
      </ol>
    </ng-template>
  `
})
export class BreadcrumbsComponent implements OnInit {

  breadcrumbs: Observable<Array<Object>>;

  @Input() fixed: boolean;

  /**
   * Constructs a new BreadcrumbsComponent.
   *
   * @param elementRef         The element reference.
   * @param breadcrumbsService The Breadcrumbs Service.
   * @param titleService       The Title Service.
   */
  constructor(private elementRef: ElementRef, private breadcrumbsService: BreadcrumbsService,
              private titleService: TitleService) {
  }

  /**
   * The current title from the Title Service.
   */
  get title(): Observable<string> {
    return this.titleService.title;
  }

  ngOnInit(): void {
    Replace(this.elementRef);

    if (this.fixed) {
      document.querySelector('body').classList.add('breadcrumbs-fixed');
    }

    this.breadcrumbs = this.breadcrumbsService.breadcrumbs;
  }
}
