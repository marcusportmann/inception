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

import {AfterViewInit, Component, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';

export interface User {
  id: string,
  firstNames: string;
  lastName: string;
  email: string;
}

const USER_DATA: User[] = [{
  id: '44f3970c-432c-4834-807a-f59dd56b7d0d',
  firstNames: 'Joe',
  lastName: 'Bloggs',
  email: 'joe@inception.digital'
}, {
  id: 'd19c8a82-12c7-46ab-a5a0-f93c421034ce',
  firstNames: 'Bob',
  lastName: 'Smith',
  email: 'bob@inception.digital'
}, {
  id: '1a0f97a6-778d-409a-b071-84071be162c3',
  firstNames: 'Frank',
  lastName: 'Black',
  email: 'frank@inception.digital'
}, {
  id: 'b318689d-292e-442d-bebe-d7bbc85d0044',
  firstNames: 'Samantha',
  lastName: 'Carter',
  email: 'samantha.carter@stargate.com'
}, {
  id: 'e91207e3-8314-4521-be4d-a3350dc4a13b',
  firstNames: 'Daniel',
  lastName: 'Jackson',
  email: 'daniel.jackson@stargate.com'
}, {
  id: '48240cb9-adee-40f9-8ed7-c15cc444acd6',
  firstNames: 'Jack',
  lastName: 'O\'Neill',
  email: 'jack.oneill@stargate.com'
}, {
  id: '8bc01021-0392-46fb-97ee-9dc5d7015635',
  firstNames: 'Rodney',
  lastName: 'McKay',
  email: 'rodney.mckay@stargate.com'
}, {
  id: 'bbef098a-dc59-453f-8785-690b9533ae15',
  firstNames: 'George',
  lastName: 'Hammon',
  email: 'george.hammond@stargate.com'
}, {
  id: 'a1267e2e-759d-4b64-a919-4f02e87c38c7',
  firstNames: 'Cameron',
  lastName: 'Mitchell',
  email: 'cameron.mitchell@stargate.com'
}, {
  id: '591dce27-ff34-4d49-a7b0-c60b4a9f92a2',
  firstNames: 'Hank',
  lastName: 'Landry',
  email: 'hank.landry@stargate.com'
}, {
  id: 'a010ee21-6789-4d33-b863-5f9ced850da6',
  firstNames: 'Richard',
  lastName: 'Woolsey',
  email: 'richard.woolsey@stargate.com'
}, {
  id: 'ab71f933-0400-432f-aac4-02008eb5857a',
  firstNames: 'Janet',
  lastName: 'Fraiser',
  email: 'janet.fraiser@stargate.com'
}, {
  id: '3df242f8-fa69-42da-b6ed-f3f982bebfad',
  firstNames: 'Aeryn',
  lastName: 'Sun',
  email: 'aeryn.sun@farscape.com'
}, {
  id: '85a11ea2-6eab-454b-a961-cc67530c6fd9',
  firstNames: 'John',
  lastName: 'Crichton',
  email: 'john.crichton@farscape.com'
}, {
  id: '7aa4d757-770b-4963-82f1-90088a34fc74',
  firstNames: 'Mele-On',
  lastName: 'Grayza',
  email: 'mele-on.grayza@farscape.com'
}, {
  id: '64b98cf0-6612-443b-97f2-dbf2056b137a',
  firstNames: 'Bialar',
  lastName: 'Crais',
  email: 'bialar.crais@farscape.com'
}, {
  id: '986dd44e-2ee3-4c96-a781-1514b6b91ab1',
  firstNames: 'Zotoh',
  lastName: 'Zhaan',
  email: 'zotoh.zhaan@farscape.com'
}, {
  id: '3bac3a72-e815-4d6c-83f0-a5e0e40058b1',
  firstNames: 'Luke',
  lastName: 'Skywalker',
  email: 'luke.skywalker@rebellion.com'
}, {
  id: '0497ec49-6a58-484b-960a-4689a5e31e29',
  firstNames: 'Obi-Wan',
  lastName: 'Kenobi',
  email: 'obi-wan.kenobi@rebellion.com'
}, {
  id: '43b24ad7-158e-4891-8410-524c5ae046b6',
  firstNames: 'Han',
  lastName: 'Solo',
  email: 'han.solo@rebellion.com'
}, {
  id: 'df263c85-1066-47ec-a00e-06e65d0a46de',
  firstNames: 'Leia',
  lastName: 'Organa',
  email: 'leial.organa@rebellion.com'
}, {
  id: '47a5a3b7-6c8c-46c9-b53b-587b92308994',
  firstNames: 'Lando',
  lastName: 'Calrissian',
  email: 'lando.calrissian@rebellion.com'
}, {
  id: 'fbec45a5-ae84-493b-9f6e-91111f07a23b',
  firstNames: 'Anakin',
  lastName: 'Skywalker',
  email: 'anakin.skywalker@empire.com'
}, {
  id: 'dfe10137-6f7d-41ff-9be9-ab6754da86e0',
  firstNames: 'Ben',
  lastName: 'Solo',
  email: 'ben.solo@empire.com'
}, {
  id: '827c0246-0466-4af3-aa91-5957961b4b62',
  firstNames: 'Harry',
  lastName: 'Potter',
  email: 'harry.potter@hogwarts.ac.uk'
}, {
  id: '1216b0ec-2135-466f-bf90-a71c1a93a74f',
  firstNames: 'Hermione',
  lastName: 'Granger',
  email: 'hermione.granger@hogwarts.ac.uk'
}, {
  id: '78cc2486-de93-4939-87a3-fd64742b24da',
  firstNames: 'Ron',
  lastName: 'Weasley',
  email: 'ron.weasley@hogwarts.ac.uk'
}, {
  id: '8ce91f1c-a80c-42b8-8383-08f961dc2506',
  firstNames: 'Ginny',
  lastName: 'Weasley',
  email: 'ginny.weasley@hogwarts.ac.uk'
}, {
  id: '46b683e0-87f3-46b1-99fe-a08932503863',
  firstNames: 'Rubeus',
  lastName: 'Hagrid',
  email: 'rebeus.hagrid@hogwarts.ac.uk'
}, {
  id: 'f444edad-1055-44e3-8969-d11d0be0b73e',
  firstNames: 'Albus',
  lastName: 'Dumbledore',
  email: 'albus.dumbledore@hogwarts.ac.uk'
}, {
  id: 'fb752b8b-49c3-4964-9df4-1029f9c5c516',
  firstNames: 'Ian',
  lastName: 'Cormac',
  email: 'ian.cormac@polity.com'
}, {
  id: '1eb97c76-1f54-4b40-b094-f13555a6aaf6',
  firstNames: 'Cheradenine',
  lastName: 'Zakalwe',
  email: 'cheradenine.zakalwe@culture.com'
}, {
  id: '8e2f9430-cf23-407b-9f21-0aba45019b75',
  firstNames: 'Jernau',
  lastName: 'Gurgeh',
  email: 'jernau.gurgeh@culture.com'
}
];

/**
 * The ActionListTableComponent class implements the action list table component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'action-list-table.component.html',
  styleUrls: ['action-list-table.component.css']
})
export class ActionListTableComponent implements AfterViewInit {

  dataSource = new MatTableDataSource<User>(USER_DATA);

  displayedColumns = ['firstNames', 'lastName', 'email', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort?: MatSort;

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  ngAfterViewInit() {
    if (this.paginator) {
      this.dataSource.paginator = this.paginator;
    }

    if (this.sort) {
      this.dataSource.sort = this.sort;
    }
  }
}

