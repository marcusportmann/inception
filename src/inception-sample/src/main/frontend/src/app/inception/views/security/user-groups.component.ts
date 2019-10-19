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

import {AfterViewInit, Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {finalize, first, map, startWith} from 'rxjs/operators';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {AdminContainerView} from '../../components/layout/admin-container-view';
import {BackNavigation} from '../../components/layout/back-navigation';
import {User} from '../../services/security/user';
import {SecurityServiceError} from '../../services/security/security.service.errors';
import {SecurityService} from '../../services/security/security.service';
import {combineLatest, ReplaySubject, Subject, Subscription} from 'rxjs';
import {UserDirectoryType} from '../../services/security/user-directory-type';
import {MatTableDataSource} from "@angular/material/table";
import {CodeCategorySummary} from "../../services/codes/code-category-summary";
import {CodesServiceError} from "../../services/codes/codes.service.errors";
import {Organization} from "../../services/security/organization";

/**
 * The UserGroupsComponent class implements the user groups component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'user-groups.component.html',
  styleUrls: ['user-groups.component.css']
})
export class UserGroupsComponent extends AdminContainerView implements AfterViewInit, OnDestroy {

  private subscriptions: Subscription = new Subscription();

  allGroupNames: string[] = [];

  availableGroupNames: Subject<string[]> = new ReplaySubject<string[]>();

  dataSource = new MatTableDataSource<string>([]);

  displayedColumns = ['name', 'actions'];

  selectedGroupName: string = '';

  userDirectoryId: number;

  username: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve parameters
    this.userDirectoryId = Number(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
    this.username = decodeURIComponent(this.activatedRoute.snapshot.paramMap.get('username')!);
  }

  get backNavigation(): BackNavigation {
    return new BackNavigation(this.i18n({
      id: '@@user_groups_component_back_title',
      value: 'Back'
    }), ['../../..'], {
      relativeTo: this.activatedRoute,
      state: {userDirectoryId: this.userDirectoryId}
    });
  }

  get isGroupSelected(): boolean {
    return false;
  }

  get title(): string {
    return this.i18n({
      id: '@@user_groups_component_title',
      value: 'User Groups'
    })
  }

  loadGroupNamesForUser(): void {
    this.spinnerService.showSpinner();

    this.securityService.getGroupNamesForUser(this.userDirectoryId, this.username)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((groupNamesForUser: string[]) => {
        this.dataSource.data = groupNamesForUser;
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  ngAfterViewInit(): void {
    // Retrieve the existing user and initialise the form fields
    this.spinnerService.showSpinner();

    this.securityService.getGroupNames(this.userDirectoryId)
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((groupNames: string[]) => {
        this.allGroupNames = groupNames;

        this.loadGroupNamesForUser();

        this.availableGroupNames.next(this.calculateAvailableGroupNames(this.allGroupNames, this.dataSource.data));
      }, (error: Error) => {
          // noinspection SuspiciousTypeOfGuard
          if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
            (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });



    // combineLatest([this.securityService.getGroupNames(this.userDirectoryId),
    //   this.securityService.getGroupNamesForUser(this.userDirectoryId, this.username)
    // ])
    //   .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
    //   .subscribe((results: [string[], string[]]) => {
    //     this.allGroupNames = results[0];
    //     this.dataSource.data = results[1];
    //
    //     console.log('allGroupNames = ', results[0]);
    //     console.log('groupNamesForUser = ', results[1]);
    //
    //     this.availableGroupNames.next(this.calculateAvailableGroupNames(this.allGroupNames, this.dataSource.data));
    //   }, (error: Error) => {
    //     // noinspection SuspiciousTypeOfGuard
    //     if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
    //       (error instanceof SystemUnavailableError)) {
    //       // noinspection JSIgnoredPromiseFromCall
    //       this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    //     } else {
    //       this.dialogService.showErrorDialog(error);
    //     }
    //   });
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  onAdd(): void {
    if (!!this.selectedGroupName) {

      // this.user.lastName = this.editUserForm.get('lastName')!.value;
      // this.user.mobileNumber = this.editUserForm.get('mobileNumber')!.value;
      // this.user.phoneNumber = this.editUserForm.get('phoneNumber')!.value;
      // this.user.email = this.editUserForm.get('email')!.value;

      // this.spinnerService.showSpinner();
      //
      // this.securityService.updateUser(this.user, this.editUserForm.contains('expirePassword') ?
      //   this.editUserForm.get('expirePassword')!.value : false,
      //   this.editUserForm.contains('lockUser') ? this.editUserForm.get('lockUser')!.value : false)
      //   .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      //   .subscribe(() => {
      //     const userDirectoryId = Number(this.activatedRoute.snapshot.paramMap.get('userDirectoryId')!);
      //
      //     // noinspection JSIgnoredPromiseFromCall
      //     this.router.navigate(['../../..'], {
      //       relativeTo: this.activatedRoute,
      //       state: {userDirectoryId}
      //     });
      //   }, (error: Error) => {
      //     // noinspection SuspiciousTypeOfGuard
      //     if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) ||
      //       (error instanceof SystemUnavailableError)) {
      //       // noinspection JSIgnoredPromiseFromCall
      //       this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      //     } else {
      //       this.dialogService.showErrorDialog(error);
      //     }
      //   });
    }
  }

  onRemove(groupName: string) {
    console.log('Removing group name = ', groupName);

  }

  private calculateAvailableGroupNames(allGroupNames: string[], existingGroupNames: string[]): string[] {

    let availableGroupNames: string[] = [];

    for (let i = 0; i < allGroupNames.length; i++) {
      let foundExistingGroup: boolean = false;

      for (let j = 0; j < existingGroupNames.length; j++) {
        if (allGroupNames[i] == existingGroupNames[j]) {
          foundExistingGroup = true;
          break;
        }
      }

      if (!foundExistingGroup) {
        availableGroupNames.push(allGroupNames[i]);
      }
    }

    console.log('availableGroupNames', availableGroupNames);

    return availableGroupNames;
  }
}
