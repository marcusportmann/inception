/*
 * Copyright Marcus Portmann
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

import { Component } from '@angular/core';
import { CoreModule } from 'ngx-inception/core';

/** Shape of a grid tile */
interface Tile {
  color: string;
  cols: number;
  rows: number;
  text: string;
}

/**
 * The GridListComponent class implements the grid list component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'app-grid-list',
  standalone: true,
  imports: [CoreModule],
  templateUrl: 'grid-list.component.html'
})
export class GridListComponent {
  tiles: Tile[] = [
    {
      text: 'One',
      cols: 3,
      rows: 1,
      color: 'lightblue'
    },
    {
      text: 'Two',
      cols: 1,
      rows: 2,
      color: 'lightgreen'
    },
    {
      text: 'Three',
      cols: 1,
      rows: 1,
      color: 'lightpink'
    },
    {
      text: 'Four',
      cols: 2,
      rows: 1,
      color: '#DDBDF1'
    }
  ];
}
