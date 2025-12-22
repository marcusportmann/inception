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

import {
  AfterViewInit, ChangeDetectionStrategy, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSort } from '@angular/material/sort';
import {
  CoreModule, SortDirection, StatefulListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { UserDirectorySummaries } from '../services/user-directory-summaries';
import { UserDirectorySummaryDataSource } from '../services/user-directory-summary-data-source';

/**
 * The UserDirectoriesComponent class implements the user directories component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-user-directories',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'user-directories.component.html',
  styleUrls: ['user-directories.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class UserDirectoriesComponent extends StatefulListView implements AfterViewInit {
  readonly dataSource: UserDirectorySummaryDataSource;

  readonly defaultSortActive = 'name';

  readonly displayedColumns = ['name', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listStateKey = 'security.user-directories';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_user_directories_title:User Directories`;

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    const nav = this.router.currentNavigation();

    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    this.dataSource = new UserDirectorySummaryDataSource(this.securityService);
  }

  deleteUserDirectory(userDirectoryId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_user_directories_confirm_delete_user_directory:Are you sure you want to delete the user directory?`,
      () => this.securityService.deleteUserDirectory(userDirectoryId),
      () => this.loadUserDirectorySummaries()
    );
  }

  editUserDirectory(userDirectoryId: string): void {
    this.saveState();

    void this.router.navigate([userDirectoryId], {
      relativeTo: this.activatedRoute
    });
  }

  newUserDirectory(): void {
    this.saveState();

    void this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData());

    // Stabilize view after paginator/sort mutations
    this.changeDetectorRef.detectChanges();
  }

  private loadData(): void {
    this.spinnerService.showSpinner();

    this.loadUserDirectorySummaries()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }

  private loadUserDirectorySummaries(): Observable<UserDirectorySummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    return this.dataSource.load(
      filter,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
