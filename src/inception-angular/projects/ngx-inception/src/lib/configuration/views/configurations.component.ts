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

import {AfterViewInit, Component, HostBinding, ViewChild} from '@angular/core';
import {MatDialogRef} from '@angular/material/dialog';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';
import {MatTableDataSource} from '@angular/material/table';
import {finalize, first} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {AdminContainerView} from '../../layout/components/admin-container-view';
import {Configuration} from '../services/configuration';
import {ConfigurationService} from '../services/configuration.service';
import {DialogService} from '../../dialog/services/dialog.service';
import {SpinnerService} from '../../layout/services/spinner.service';
import {ConfirmationDialogComponent} from '../../dialog/components/confirmation-dialog.component';
import {AccessDeniedError} from '../../core/errors/access-denied-error';
import {ServiceUnavailableError} from '../../core/errors/service-unavailable-error';
import {Error} from '../../core/errors/error';

/**
 * The ConfigurationsComponent class implements the configurations component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'configurations.component.html',
  styleUrls: ['configurations.component.css']
})
export class ConfigurationsComponent extends AdminContainerView implements AfterViewInit {

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  dataSource = new MatTableDataSource<Configuration>();

  displayedColumns = ['key', 'value', 'actions'];

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private configurationService: ConfigurationService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Set the data source filter
    this.dataSource.filterPredicate = (data, filter): boolean => data.key.toLowerCase().includes(filter);
  }

  get title(): string {
    return $localize`:@@configuration_configurations_title:Configuration`
  }

  applyFilter(filterValue: string): void {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  deleteConfiguration(key: string): void {
    const dialogRef: MatDialogRef<ConfirmationDialogComponent, boolean> = this.dialogService.showConfirmationDialog({
      message: $localize`:@@configuration_configurations_confirm_delete_configuration:Are you sure you want to delete the configuration?`
    });

    dialogRef.afterClosed()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((confirmation: boolean | undefined) => {
        if (confirmation === true) {
          this.spinnerService.showSpinner();

          this.configurationService.deleteConfiguration(key)
            .pipe(first())
            .subscribe(() => {
              this.loadConfigurations();
            }, (error: Error) => {
              // noinspection SuspiciousTypeOfGuard
              if ((error instanceof AccessDeniedError) || (error instanceof ServiceUnavailableError)) {
                // noinspection JSIgnoredPromiseFromCall
                this.router.navigateByUrl('/error/send-error-report', {state: {error}});
              } else {
                this.dialogService.showErrorDialog(error);
              }
            });
        }
      });
  }

  editConfiguration(key: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(key) + '/edit'], {relativeTo: this.activatedRoute});
  }

  loadConfigurations(): void {
    this.spinnerService.showSpinner();

    this.configurationService.getConfigurations()
      .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
      .subscribe((configurations: Configuration[]) => {
        this.dataSource.data = configurations;
      }, (error: Error) => {
        // noinspection SuspiciousTypeOfGuard
        if ((error instanceof AccessDeniedError) || (error instanceof ServiceUnavailableError)) {
          // noinspection JSIgnoredPromiseFromCall
          this.router.navigateByUrl('/error/send-error-report', {state: {error}});
        } else {
          this.dialogService.showErrorDialog(error);
        }
      });
  }

  newConfiguration(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.dataSource.paginator = this.paginator;
    this.dataSource.sort = this.sort;

    this.loadConfigurations();
  }
}

