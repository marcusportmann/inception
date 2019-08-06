// /*
//  * Copyright 2019 Marcus Portmann
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *   http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//
// import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
// import {MatDialogRef} from '@angular/material/dialog';
// import {MatPaginator} from '@angular/material/paginator';
// import {MatSort} from '@angular/material/sort';
// import {finalize, first, tap} from 'rxjs/operators';
// import {DialogService} from '../../services/dialog/dialog.service';
// import {SpinnerService} from '../../services/layout/spinner.service';
// import {I18n} from '@ngx-translate/i18n-polyfill';
// import {Error} from '../../errors/error';
// import {ActivatedRoute, Router} from '@angular/router';
// import {ConfirmationDialogComponent} from '../../components/dialogs';
// import {SystemUnavailableError} from '../../errors/system-unavailable-error';
// import {AccessDeniedError} from '../../errors/access-denied-error';
// import {SecurityService} from '../../services/security/security.service';
// import {SecurityServiceError} from '../../services/security/security.service.errors';
// import {SortDirection} from '../../services/security/sort-direction';
// import {merge, Subscription} from 'rxjs';
// import {TableFilter} from '../../components/controls';
// import {UserDirectorySummaryDatasource} from '../../services/security/user-directory-summary.datasource';
// import {AdminContainerView} from '../../components/layout/admin-container-view';
//
// /**
//  * The UserDirectoriesComponent class implements the user directories component.
//  *
//  * @author Marcus Portmann
//  */
// @Component({
//   templateUrl: 'user-directories.component.html',
//   styleUrls: ['user-directories.component.css'],
//   host: {
//     'class': 'flex flex-column flex-fill',
//   }
// })
// export class UserDirectoriesComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
//
//   private subscriptions: Subscription = new Subscription();
//
//   dataSource: UserDirectorySummaryDatasource;
//
//   displayedColumns = ['name', 'actions'];
//
//   @ViewChild(MatPaginator, {static: true}) paginator?: MatPaginator;
//
//   @ViewChild(MatSort, {static: true}) sort?: MatSort;
//
//   @ViewChild(TableFilter, {static: true}) tableFilter?: TableFilter;
//
//   constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
//               private securityService: SecurityService, private dialogService: DialogService,
//               private spinnerService: SpinnerService) {
//     super();
//
//     this.dataSource = new UserDirectorySummaryDatasource(this.securityService);
//   }
//
//   deleteUserDirectory(userDirectoryId: string): void {
//     const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
//       {
//         message: this.i18n({
//           id: '@@user_directories_component_confirm_delete_organization',
//           value: 'Are you sure you want to delete the user directory?'
//         })
//       });
//
//     dialogRef.afterClosed()
//       .pipe(first())
//       .subscribe((confirmation: boolean | undefined) => {
//         if (confirmation === true) {
//           this.spinnerService.showSpinner();
//
//           this.securityService.deleteUserDirectory(userDirectoryId)
//             .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
//             .subscribe(() => {
//               this.dataSource.load('', SortDirection.Ascending, 0, 10);
//             }, (error: Error) => {
//               // noinspection SuspiciousTypeOfGuard
//               if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
//                 (error instanceof SystemUnavailableError)) {
//                 // noinspection JSIgnoredPromiseFromCall
//                 this.router.navigateByUrl('/error/send-error-report', {state: {error}});
//               } else {
//                 this.dialogService.showErrorDialog(error);
//               }
//             });
//         }
//       });
//   }
//
//   editUserDirectory(userDirectoryId: string): void {
//     // noinspection JSIgnoredPromiseFromCall
//     this.router.navigate([userDirectoryId], {relativeTo: this.activatedRoute});
//   }
//
//   loadUserDirectorySummaries(): void {
//     let filter = '';
//
//     if (this.tableFilter) {
//       if (!!this.tableFilter.filter) {
//         filter = this.tableFilter.filter;
//         filter = filter.trim();
//         filter = filter.toLowerCase();
//       }
//     }
//
//     let sortDirection = SortDirection.Ascending;
//
//     if (this.sort) {
//       sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
//     }
//
//     let pageIndex = 0;
//     let pageSize = 10;
//
//     if (this.paginator) {
//       pageIndex = this.paginator.pageIndex;
//       pageSize = this.paginator.pageSize;
//     }
//
//     this.dataSource.load(filter, sortDirection, pageIndex, pageSize);
//   }
//
//   newUserDirectory(): void {
//     // noinspection JSIgnoredPromiseFromCall
//     this.router.navigate(['new-user-directory'], {relativeTo: this.activatedRoute});
//   }
//
//   ngAfterViewInit(): void {
//     this.subscriptions.add(this.dataSource.loading.subscribe((next: boolean) => {
//       if (next) {
//         this.spinnerService.showSpinner()
//       } else {
//         this.spinnerService.hideSpinner();
//       }
//     }, (error: Error) => {
//       // noinspection SuspiciousTypeOfGuard
//       if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
//         (error instanceof SystemUnavailableError)) {
//         // noinspection JSIgnoredPromiseFromCall
//         this.router.navigateByUrl('/error/send-error-report', {state: {error}});
//       } else {
//         this.dialogService.showErrorDialog(error);
//       }
//     }));
//
//     if (this.sort && this.tableFilter && this.paginator) {
//       this.subscriptions.add(
//         this.sort.sortChange.subscribe(() => {
//           if (this.paginator) {
//             this.paginator.pageIndex = 0
//           }
//         }));
//
//       this.subscriptions.add(
//         this.tableFilter.changed.subscribe(() => {
//           if (this.paginator) {
//             this.paginator.pageIndex = 0
//           }
//         }));
//
//       this.subscriptions.add(
//         merge(this.sort.sortChange, this.tableFilter.changed, this.paginator.page)
//           .pipe(tap(() => {
//             this.loadUserDirectorySummaries();
//           })).subscribe());
//     } else {
//       console.log('One or more view children are invalid for the UserDirectoriesComponent');
//     }
//
//     this.loadUserDirectorySummaries();
//   }
//
//   ngOnDestroy(): void {
//     this.subscriptions.unsubscribe();
//   }
// }
//
