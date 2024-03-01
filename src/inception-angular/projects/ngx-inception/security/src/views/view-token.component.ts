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

import {AfterViewInit, Component} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {
  AccessDeniedError, AdminContainerView, BackNavigation, DialogService, Error, InvalidArgumentError,
  ServiceUnavailableError, SpinnerService
} from 'ngx-inception/core';
import {finalize, first} from 'rxjs/operators';
import {SecurityService} from '../services/security.service';
import {Token} from '../services/token';
import {TokenType} from '../services/token-type';

/**
 * The ViewTokenComponent class implements the view token component.
 *
 * @author Marcus Portmann
 */
@Component({
  templateUrl: 'view-token.component.html',
  styleUrls: ['view-token.component.css'],
})
export class ViewTokenComponent extends AdminContainerView implements AfterViewInit {

  token: Token | null = null;

  tokenId: string;

  constructor(private router: Router, private activatedRoute: ActivatedRoute,
              private securityService: SecurityService, private dialogService: DialogService,
              private spinnerService: SpinnerService) {
    super();

    // Retrieve the route parameters
    const tokenId = this.activatedRoute.snapshot.paramMap.get('tokenId');

    if (!tokenId) {
      throw (new Error('No tokenId route parameter found'));
    }

    this.tokenId = decodeURIComponent(tokenId);
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_view_token_back_navigation:Tokens`, ['../..'],
      {relativeTo: this.activatedRoute});
  }

  get title(): string {
    return $localize`:@@security_view_token_title:View Token`
  }

  back(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
  }

  getTokenTypeName(tokenType: TokenType | undefined): string {
    if (TokenType.JWT == tokenType) {
      return $localize`:@@security_token_type_jwt:JWT`;
    } else {
      return $localize`:@@security_token_type_unknown:Unknown`;
    }
  }

  ngAfterViewInit(): void {
    // Retrieve the existing code category and initialise the form controls
    this.spinnerService.showSpinner();

    this.securityService.getToken(this.tokenId)
    .pipe(first(), finalize(() => this.spinnerService.hideSpinner()))
    .subscribe((token: Token) => {
      this.token = token;
    }, (error: Error) => {
      // noinspection SuspiciousTypeOfGuard
      if ((error instanceof AccessDeniedError) || (error instanceof InvalidArgumentError) || (error instanceof ServiceUnavailableError)) {
        // noinspection JSIgnoredPromiseFromCall
        this.router.navigateByUrl('/error/send-error-report', {state: {error}});
      } else {
        this.dialogService.showErrorDialog(error).afterClosed()
        .pipe(first())
        .subscribe(() => {
          this.router.navigate(['../..'], {relativeTo: this.activatedRoute});
        });
      }
    });
  }
}
