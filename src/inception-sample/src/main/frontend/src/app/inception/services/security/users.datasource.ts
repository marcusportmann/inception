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

import {CollectionViewer, DataSource} from '@angular/cdk/collections';
import {BehaviorSubject, Observable} from 'rxjs';
import {SecurityService} from './security.service';
import {SortDirection} from './sort-direction';

import {first} from "rxjs/operators";
import {User} from "./user";
import {Users} from "./users";
import {SessionService} from "../session/session.service";
import {Session} from "../session/session";

/**
 * The UserDatasource class implements the user data source.
 *
 * @author Marcus Portmann
 */
export class UserDatasource implements DataSource<User> {

  private totalSubject = new BehaviorSubject<number>(0);

  private dataSubject = new BehaviorSubject<User[]>([]);

  private loadingSubject = new BehaviorSubject<boolean>(false);

  total = this.totalSubject.asObservable();

  loading = this.loadingSubject.asObservable();

  constructor(private sessionService: SessionService, private securityService: SecurityService) {
  }

  connect(collectionViewer: CollectionViewer): Observable<User[] | ReadonlyArray<User>> {
    return this.dataSubject.asObservable();
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  /**
   * Load the users.
   *
   * @param filter        The optional filter to apply to the users.
   * @param sortDirection The optional sort direction to apply to the users.
   * @param pageIndex     The optional page index.
   * @param pageSize      The optional page size.
   */
  load(filter?: string, sortDirection?: SortDirection, pageIndex?: number,
       pageSize?: number): void {
    this.loadingSubject.next(true);

    this.sessionService.session.pipe(first()).subscribe((session: Session) => {
      if (session) {
        this.securityService.getUsers(session.userDirectoryId, filter, sortDirection, pageIndex, pageSize)
          .pipe(first())
          .subscribe((users: Users) => {
            this.loadingSubject.next(false);

            this.totalSubject.next(users.total);

            this.dataSubject.next(users.users);
          }, (error: Error) => {
            this.loadingSubject.next(false);

            this.totalSubject.next(0);

            this.loadingSubject.error(error);
          });
      }
    });
  }
}
