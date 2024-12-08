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

import {AfterViewInit, Component, HostBinding, OnDestroy, ViewChild} from '@angular/core';
import {MatPaginator} from '@angular/material/paginator';
import {MatSelect} from '@angular/material/select';
import {MatSort} from '@angular/material/sort';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SortDirection, SpinnerService, TableFilterComponent
} from 'ngx-inception/core';
import {merge, Observable, Subject, tap, throwError} from 'rxjs';
import {
  catchError, debounceTime, filter, finalize, first, switchMap, takeUntil
} from 'rxjs/operators';
import {SecurityService} from '../services/security.service';
import {TokenStatus} from '../services/token-status';
import {TokenSummaries} from '../services/token-summaries';
import {TokenSummary} from '../services/token-summary';
import {TokenSummaryDataSource} from '../services/token-summary-data-source';
import {TokenType} from '../services/token-type';

/**
 * The TokensComponent class implements the tokens component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'tokens.component.html',
  styleUrls: ['tokens.component.css']
})
export class TokensComponent extends AdminContainerView implements AfterViewInit, OnDestroy {
  dataSource: TokenSummaryDataSource;

  displayedColumns = ['name', 'type', 'status', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  @ViewChild(MatSort, {static: true}) sort!: MatSort;

  @ViewChild(TableFilterComponent, {static: true}) tableFilter!: TableFilterComponent;

  @ViewChild('tokenStatusSelect', {static: true}) tokenStatusSelect!: MatSelect;

  protected readonly TokenStatus = TokenStatus;

  protected readonly TokenType = TokenType;

  private destroy$ = new Subject<void>();

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    this.dataSource = new TokenSummaryDataSource(this.securityService);
  }

  get title(): string {
    return $localize`:@@security_tokens_title:Tokens`
  }

  deleteToken(tokenId: string): void {
    this.confirmAndProcessAction(tokenId,
      $localize`:@@security_tokens_confirm_delete_token:Are you sure you want to delete the token?`,
      () => this.securityService.deleteToken(tokenId));
  }

  getTokenStatusName(tokenSummary: TokenSummary): string {
    switch (tokenSummary.status) {
      case TokenStatus.Active:
        return $localize`:@@security_token_status_active:Active`;
      case TokenStatus.Expired:
        return $localize`:@@security_token_status_expired:Expired`;
      case TokenStatus.Revoked:
        return $localize`:@@security_token_status_revoked:Revoked`;
      case TokenStatus.Pending:
        return $localize`:@@security_token_status_pending:Pending`;
      default:
        return $localize`:@@security_token_status_unknown:Unknown`;
    }
  }

  getTokenTypeName(tokenType: TokenType): string {
    if (tokenType == TokenType.JWT) {
      return $localize`:@@security_token_type_jwt:JWT`;
    } else {
      return $localize`:@@security_token_type_unknown:Unknown`;
    }
  }

  newToken(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], {relativeTo: this.activatedRoute});
  }

  ngAfterViewInit(): void {
    this.initializeDataLoaders();
    this.loadData();
  }

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }

  reinstateToken(tokenId: string): void {
    this.confirmAndProcessAction(tokenId,
      $localize`:@@security_tokens_confirm_reinstate_token:Are you sure you want to reinstate the token?`,
      () => this.securityService.reinstateToken(tokenId));
  }

  reissueToken(tokenId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['reissue/' + encodeURIComponent(tokenId)],
      {relativeTo: this.activatedRoute});
  }

  revokeToken(tokenId: string): void {
    this.confirmAndProcessAction(tokenId,
      $localize`:@@security_tokens_confirm_revoke_token:Are you sure you want to revoke the token?`,
      () => this.securityService.revokeToken(tokenId));
  }

  viewToken(tokenId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tokenId) + '/view'],
      {relativeTo: this.activatedRoute});
  }

  private confirmAndProcessAction(tokenId: string, confirmationMessage: string,
                                  action: () => Observable<void | boolean>): void {
    const dialogRef = this.dialogService.showConfirmationDialog({message: confirmationMessage});

    dialogRef
    .afterClosed()
    .pipe(first(), filter((confirmed) => confirmed === true), switchMap(() => {
      this.spinnerService.showSpinner();
      return action().pipe(catchError((error) => this.handleError(error)),
        tap(() => this.resetTable()), switchMap(
          () => this.loadTokenSummaries().pipe(catchError((error) => this.handleError(error)))),
        finalize(() => this.spinnerService.hideSpinner()));
    }), takeUntil(this.destroy$))
    .subscribe();
  }

  private handleError(error: Error): Observable<never> {
    if (error instanceof AccessDeniedError || error instanceof InvalidArgumentError || error instanceof ServiceUnavailableError) {
      // noinspection JSIgnoredPromiseFromCall
      this.router.navigateByUrl('/error/send-error-report', {state: {error}});
    } else {
      this.dialogService.showErrorDialog(error);
    }
    return throwError(() => error);
  }

  private initializeDataLoaders(): void {
    this.sort.sortChange
    .pipe(takeUntil(this.destroy$))
    .subscribe(() => (this.paginator.pageIndex = 0));

    merge(this.sort.sortChange, this.tokenStatusSelect.selectionChange, this.tableFilter.changed,
      this.paginator.page)
    .pipe(debounceTime(200), takeUntil(this.destroy$))
    .subscribe(() => this.loadData());
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadTokenSummaries()
    .pipe(finalize(() => this.spinnerService.hideSpinner()))
    .subscribe({
      next: () => {
        // Load complete
      },
      error: (error) => this.handleError(error),
    });
  }

  private loadTokenSummaries(): Observable<TokenSummaries> {
    const filter = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active) {
      sortDirection = this.sort.direction === 'asc' ? SortDirection.Ascending :
        SortDirection.Descending;
    }

    return this.dataSource
    .load(this.tokenStatusSelect.value as TokenStatus, filter, sortDirection,
      this.paginator.pageIndex, this.paginator.pageSize)
    .pipe(catchError((error) => this.handleError(error)));
  }

  private resetTable(): void {
    this.tableFilter.reset(false);
    this.paginator.pageIndex = 0;
    this.sort.active = '';
    this.sort.direction = 'asc' as SortDirection;
  }
}

