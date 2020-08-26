/*
 * Copyright 2020 Marcus Portmann
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

import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {debounceTime, first, map, startWith} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {BehaviorSubject, Subscription} from 'rxjs';
import {ReferenceService} from "ngx-inception";
import {AddressType} from "../../../../../../ngx-inception/src/lib/reference/services/address-type";

/**
 * The ReferenceFormComponent class implements the example form component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'reference-form.component.html'
})
export class ReferenceFormComponent implements OnInit, OnDestroy {

  referenceForm: FormGroup;

  filteredAddressTypes$: BehaviorSubject<AddressType[]> = new BehaviorSubject<AddressType[]>([]);
  private subscriptions: Subscription = new Subscription();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private referenceService: ReferenceService) {

    this.referenceForm = this.formBuilder.group({
      // hideRequired: false,
      // floatLabel: 'auto',
      // tslint:disable-next-line
      addressType: ['', Validators.required] // Validators.required
    });
  }

  displayAddressType(addressType: AddressType): string {
    console.log('[displayAddressType] addressType = ', addressType);
    if (!!addressType) {
      return addressType.name;
    } else {
      return '';
    }
  }

  ngOnInit(): void {
    this.referenceService.getAddressTypes().pipe(first()).subscribe((addressTypes: AddressType[]) => {
      const addressTypeControl = this.referenceForm.get('addressType');

      if (addressTypeControl) {
        this.subscriptions.add(addressTypeControl.valueChanges.pipe(
          startWith(''),
          debounceTime(500),
          map((value: string | AddressType) => {
            if (typeof (value) === 'string') {
              console.log('string value = ', value);
              this.filteredAddressTypes$.next(addressTypes.filter(
                addressType => addressType.name.toLowerCase().indexOf(value.toLowerCase()) === 0));
            } else {
              console.log('AddressType value = ', value);
              this.filteredAddressTypes$.next(addressTypes.filter(
                addressType => addressType.name.toLowerCase().indexOf(value.name.toLowerCase()) === 0));
            }
          })).subscribe());
      }
    });
  }

  ok(): void {
    console.log('Address type = ', this.referenceForm.get('addressType')!.value)
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  // private filterAddressTypes(value: string): string[] {
  //   const filterValue = value.toLowerCase();
  //
  //   this.addressTypes.pipe(first()).subscribe((addressTypes: AddressType[]) => {
  //     return addressTypes.filter(addressType => addressType.name.toLowerCase().indexOf(filterValue) === 0);
  //   });
  // }
}
