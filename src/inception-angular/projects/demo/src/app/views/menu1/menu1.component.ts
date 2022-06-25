/*
 * Copyright 2022 Marcus Portmann
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
import {combineLatest, debounce, interval, timer} from 'rxjs';
import {debounceTime, first, map} from 'rxjs/operators';
import {Data} from '../../services/data';
import {DataService} from '../../services/data.service';

/**
 * The Menu1Component class implements the menu 1 component.
 *
 * @author Marcus Portmann
 */
@Component({
  template: `
    <mat-card class="flex-grow-1">
      <mat-card-content>
        <button (click)="clickMe()" mat-flat-button>Click Me!</button>
      </mat-card-content>
    </mat-card>
  `
})
export class Menu1Component {

  constructor(private dataService: DataService) {
  }

  clickMe(): void {

    this.dataService.getData().pipe(first()).subscribe((data: Data) => {
      console.log('data = ', data);

      this.dataService.validateData(data).pipe(first()).subscribe();
    });


    // let nullArray: string[] | null = null;
    //
    // let emptyArray: string[] = [];
    //
    // console.log('nullArray = ', nullArray);
    // console.log('!nullArray = ', (!nullArray));
    // console.log('!!nullArray = ', (!!nullArray));
    //
    // console.log('emptyArray = ', emptyArray);
    // console.log('!emptyArray = ', (!emptyArray));
    // console.log('!!emptyArray = ', (!!emptyArray));
    //
    //
    // const timer1$ = timer(0, 1500);
    //
    // const timer2$ = timer(1000, 5000);



    // combineLatest([timer1$, timer2$]).pipe(debounceTime(1000), map(values => ({
    //   value1: values[0],
    //   value2: values[1]
    // }))).subscribe(timerValues => {
    //   console.log('timerValues.value1 = ', timerValues.value1);
    // });



  }
}
