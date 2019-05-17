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

import {CollectionViewer, DataSource} from "@angular/cdk/collections";
import {Organization} from "./organization";
import {BehaviorSubject, Observable, of} from "rxjs";
import {SecurityService} from "./security.service";
import {catchError, finalize} from "rxjs/operators";


export class OrganizationDatasource implements DataSource<Organization> {

  private dataSubject = new BehaviorSubject<Organization[]>([]);
  private loadingSubject = new BehaviorSubject<boolean>(false);

  loading = this.loadingSubject.asObservable();

  constructor(private securityService: SecurityService) {}

  connect(collectionViewer: CollectionViewer): Observable<Organization[] | ReadonlyArray<Organization>> {
    return this.dataSubject.asObservable();;
  }

  disconnect(collectionViewer: CollectionViewer): void {
    this.dataSubject.complete();
    this.loadingSubject.complete();
  }

  load() {

    this.loadingSubject.next(true);

    this.securityService.getOrganizations().pipe(
      catchError(() => of([])),
      finalize(() => this.loadingSubject.next(false))
    )
      .subscribe((organizations: Organization[]) => this.dataSubject.next(organizations));






    // this.securityService.getOrganizations().pipe(first()).subscribe((organizations: Organization[]) => {
    //   this.spinnerService.hideSpinner();
    //
    //   this.dataSource.data = organizations;
    // }, (error: Error) => {
    //   this.spinnerService.hideSpinner();
    //
    //   if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
    //     // noinspection JSIgnoredPromiseFromCall
    //     this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
    //   } else {
    //     this.dialogService.showErrorDialog(error);
    //   }
    // });


  }

}
