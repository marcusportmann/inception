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

import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {patternValidator} from '../../../inception/validators/pattern-validator';
import {NavigationService} from "../../../inception/services/navigation/navigation.service";

import * as moment from 'moment';
import {Observable} from "rxjs";
import {map, startWith} from 'rxjs/operators';
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  templateUrl: 'new-code-category.component.html',
  styleUrls: ['new-code-category.component.css'],
})
export class NewCodeCategoryComponent implements OnInit {

  newCodeCategoryForm: FormGroup;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private formBuilder: FormBuilder, private navigationService: NavigationService) {

    this.newCodeCategoryForm = this.formBuilder.group({
      //hideRequired: false,
      //floatLabel: 'auto',
      // tslint:disable-next-line
      id: ['', Validators.required],
      name: ['', Validators.required],
      //title: ['', Validators.required],
      //dateOfBirth: [moment(), Validators.required],
    });
  }

  ngOnInit() {
  }

  onCancel() {
    this.router.navigate(['../code-categories'], {relativeTo: this.activatedRoute});
  }

  onOK() {
  }
}
