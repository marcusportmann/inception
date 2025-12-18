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
  AfterViewInit, ChangeDetectorRef, Component, HostBinding, inject, ViewChild
} from '@angular/core';
import { MatPaginator } from '@angular/material/paginator';
import { MatSelect } from '@angular/material/select';
import { MatSort } from '@angular/material/sort';
import { CoreModule, SortDirection, StatefulListView, TableFilterComponent } from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { finalize, takeUntil } from 'rxjs/operators';
import { SecurityService } from '../services/security.service';
import { TokenStatus } from '../services/token-status';
import { TokenSummaries } from '../services/token-summaries';
import { TokenSummary } from '../services/token-summary';
import { TokenSummaryDataSource } from '../services/token-summary-data-source';
import { TokenType } from '../services/token-type';

interface TokenListExtras {
  tokenStatus: TokenStatus;
}

/**
 * The TokensComponent class implements the Tokens component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-tokens',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'tokens.component.html',
  styleUrls: ['tokens.component.css']
})
export class TokensComponent extends StatefulListView<TokenListExtras> implements AfterViewInit {
  readonly dataSource: TokenSummaryDataSource;

  displayedColumns = ['name', 'type', 'status', 'actions'];

  @HostBinding('class') hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'security.tokens';

  @ViewChild(MatPaginator, { static: true }) override paginator!: MatPaginator;

  @ViewChild(MatSort, { static: true }) override sort!: MatSort;

  @ViewChild(TableFilterComponent, { static: true })
  override tableFilter!: TableFilterComponent;

  readonly title = $localize`:@@security_tokens_title:Tokens`;

  @ViewChild('tokenStatusSelect', { static: true })
  tokenStatusSelect!: MatSelect;

  protected readonly TokenStatus = TokenStatus;

  protected readonly TokenType = TokenType;

  private readonly changeDetectorRef = inject(ChangeDetectorRef);

  /** Whether this navigation requested a state reset (from the sidebar). */
  private readonly resetStateRequested: boolean;

  constructor(private securityService: SecurityService) {
    super();

    // Read the reset flag from the current navigation (if any)
    const nav = this.router.getCurrentNavigation();
    this.resetStateRequested = !!nav?.extras.state?.['resetState'];

    this.dataSource = new TokenSummaryDataSource(this.securityService);
  }

  deleteToken(tokenId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_tokens_confirm_delete_token:Are you sure you want to delete the token?`,
      () => this.securityService.deleteToken(tokenId),
      () => this.loadTokenSummaries()
    );
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
    if (tokenType === TokenType.JWT) {
      return $localize`:@@security_token_type_jwt:JWT`;
    } else {
      return $localize`:@@security_token_type_unknown:Unknown`;
    }
  }

  newToken(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  ngAfterViewInit(): void {
    // Initialize list state + data loaders (includes tokenStatus as extra trigger)
    this.initializeStatefulList(this.resetStateRequested, () => this.loadData(), [
      {
        observable: this.tokenStatusSelect.selectionChange,
        resetPage: true
      }
    ]);

    // Stabilize view after mutating sort/paginator
    this.changeDetectorRef.detectChanges();
  }

  reinstateToken(tokenId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_tokens_confirm_reinstate_token:Are you sure you want to reinstate the token?`,
      () => this.securityService.reinstateToken(tokenId),
      () => this.loadTokenSummaries()
    );
  }

  reissueToken(tokenId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['reissue', encodeURIComponent(tokenId)], {
      relativeTo: this.activatedRoute
    });
  }

  revokeToken(tokenId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@security_tokens_confirm_revoke_token:Are you sure you want to revoke the token?`,
      () => this.securityService.revokeToken(tokenId),
      () => this.loadTokenSummaries()
    );
  }

  viewToken(tokenId: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(tokenId), 'view'], {
      relativeTo: this.activatedRoute
    });
  }

  protected override getExtrasState(): TokenListExtras {
    return {
      tokenStatus: (this.tokenStatusSelect?.value as TokenStatus) ?? TokenStatus.Active
    };
  }

  protected override resetExtrasState(): void {
    this.tokenStatusSelect.value = TokenStatus.Active;
  }

  protected override restoreExtrasState(extras: TokenListExtras | undefined): void {
    if (!extras) {
      this.tokenStatusSelect.value = TokenStatus.Active;
      return;
    }

    this.tokenStatusSelect.value = extras.tokenStatus ?? TokenStatus.Active;
  }

  private loadData(): void {
    this.spinnerService.showSpinner();
    this.loadTokenSummaries()
      .pipe(
        finalize(() => this.spinnerService.hideSpinner()),
        takeUntil(this.destroy$)
      )
      .subscribe({
        next: () => {
          // Load complete
        },
        error: (error) => this.handleError(error, false)
      });
  }

  private loadTokenSummaries(): Observable<TokenSummaries> {
    const filterValue = this.tableFilter.filter?.trim().toLowerCase() || '';

    let sortDirection = SortDirection.Descending;

    if (this.sort.active && this.sort.direction) {
      sortDirection =
        this.sort.direction === 'asc' ? SortDirection.Ascending : SortDirection.Descending;
    }

    const tokenStatus = (this.tokenStatusSelect?.value as TokenStatus) ?? TokenStatus.Active;

    return this.dataSource.load(
      tokenStatus,
      filterValue,
      sortDirection,
      this.paginator.pageIndex,
      this.paginator.pageSize
    );
  }
}
