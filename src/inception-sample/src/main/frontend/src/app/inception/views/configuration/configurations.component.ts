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
import {finalize, first} from 'rxjs/operators';
import {DialogService} from '../../services/dialog/dialog.service';
import {SpinnerService} from '../../services/layout/spinner.service';
import {I18n} from '@ngx-translate/i18n-polyfill';
import {Error} from '../../errors/error';
import {ActivatedRoute, Router} from '@angular/router';
import {ConfirmationDialogComponent} from '../../components/dialogs';
import {SystemUnavailableError} from '../../errors/system-unavailable-error';
import {AccessDeniedError} from '../../errors/access-denied-error';
import {Configuration} from "../../services/configuration/configuration";
import {ConfigurationService} from "../../services/configuration/configuration.service";
import {ConfigurationServiceError} from "../../services/configuration/configuration.service.errors";

/**
 * The ConfigurationsComponent class implements the configurations component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'configurations.component.html',
  styleUrls: ['configurations.component.css'],
  host: {
    'class': 'flex flex-column flex-fill',
  }
})
export class ConfigurationsComponent implements AfterViewInit, OnInit {

  dataSource = new MatTableDataSource<Configuration>();

  displayedColumns: string[] = ['key', 'value', 'actions'];

  @ViewChild(MatPaginator) paginator: MatPaginator;

  @ViewChild(MatSort) sort: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute, private i18n: I18n,
              private configurationService: ConfigurationService,
              private dialogService: DialogService, private spinnerService: SpinnerService) {
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteConfiguration(key: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog(
      {
        message: this.i18n({
          id: '@@configurations_component_confirm_delete_configuration',
          value: 'Are you sure you want to delete the configuration?'
        })
      });

    dialogRef.afterClosed().pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((confirmation: boolean) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.configurationService.deleteConfiguration(key).pipe(first()).subscribe(() => {
            this.loadConfigurations();
          }, (error: Error) => {
            if ((error instanceof ConfigurationServiceError) ||
              (error instanceof AccessDeniedError) || (error instanceof SystemUnavailableError)) {
              // noinspection JSIgnoredPromiseFromCall
              this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
            } else {
              this.dialogService.showErrorDialog(error);
            }
          });
        }
      });
  }

  editConfiguration(key: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([key], {relativeTo: this.activatedRoute});
  }

  loadConfigurations(): void {
    this.spinnerService.showSpinner();

    this.configurationService.getConfigurations()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((configurations: Configuration[]) => {
        this.dataSource.data = configurations;
      }, (error: Error) => {
        if ((error instanceof ConfigurationServiceError) || (error instanceof AccessDeniedError) ||
          (error instanceof SystemUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error: error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });

    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;
    this.dataSource.filterPredicate = function (data, filter): boolean {
      return data.key.toLowerCase().includes(filter);
    };
  }

  newConfiguration(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new-configuration'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.loadConfigurations();
  }

  ngOnInit(): void {
  }
}

