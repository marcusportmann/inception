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

import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {MatDialogRef, MatPaginator, MatSort, MatTableDataSource} from '@angular/material';
import {first} from 'rxjs/operators';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {Organization} from "../../services/security/organization";
import {SecurityService} from "../../services/security/security.service";
import {SecurityServiceError} from "../../services/security/security.service.errors";

/**
 * The OrganizationsComponent class implements the organizations component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'organizations.component.html',
  styleUrls: ['organizations.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class OrganizationsComponent implements AfterViewInit, OnInit {

  dataSource = new MatTableDataSource<Organization>();

  displayedColumns: string[] = ['name', 'actions'];

  @ViewChild(MatPaginator)
  paginator: MatPaginator;

  @ViewChild(MatSort)
  sort: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteOrganization(organizationId: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> =
      this.dialogService.showConfirmationDialog(
        {message: this.i18n({id: '@@organizations_component_confirm_delete_organization',
            value: 'Are you sure you want to delete the organization?'})});

    dialogRef.afterClosed().pipe(first()).subscribe((confirmation: boolean) => {
      if (confirmation === true) {
        this.spinnerService.showSpinner();

        this.securityService.deleteOrganization(organizationId).pipe(first()).subscribe(() => {
          this.spinnerService.hideSpinner();

          this.ngAfterViewInit();
        }, (error: Error) => {
          this.spinnerService.hideSpinner();

          if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
            // noinspection JSIgnoredPromiseFromCall
            this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
          } else {
            this.dialogService.showErrorDialog(error);
          }
        });
      }
    });
  }

  editOrganization(organizationId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([organizationId], {relativeTo: this.activatedRoute});
  }

  loadOrganizations(): void {
    this.spinnerService.showSpinner();

    this.securityService.getOrganizations().pipe(first()).subscribe((organizations: Organization[]) => {
      this.spinnerService.hideSpinner();

      this.dataSource.data = organizations;
    }, (error: Error) => {
      this.spinnerService.hideSpinner();

      if ((error instanceof SecurityServiceError) || (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
      } else {
        this.dialogService.showErrorDialog(error);
      }
    });

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = function(data, filter): boolean {
      return data.name.toLowerCase().includes(filter);
    };
  }

  newOrganization(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new-organization'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.loadOrganizations();
  }

  ngOnInit(): void {
  }
}

