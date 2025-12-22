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

import { AfterViewInit, Component, inject } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import {
  AdminContainerView, BackNavigation, CoreModule, ISO8601Util, ValidatedFormDirective
} from 'ngx-inception/core';
import { finalize, first } from 'rxjs/operators';
import { GenerateTokenRequest } from '../services/generate-token-request';
import { SecurityService } from '../services/security.service';
import { Token } from '../services/token';
import { TokenClaim } from '../services/token-claim';
import { TokenType } from '../services/token-type';
import { TokenClaimDialogComponent, TokenClaimDialogData } from './token-claim-dialog.component';

/**
 * The NewTokenComponent class implements the new token component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-security-new-token',
  standalone: true,
  imports: [CoreModule, ValidatedFormDirective],
  templateUrl: 'new-token.component.html',
  styleUrls: ['new-token.component.css']
})
export class NewTokenComponent extends AdminContainerView implements AfterViewInit {
  descriptionControl: FormControl;

  existingTokenId: string | null = null;

  expiryDateControl: FormControl;

  nameControl: FormControl;

  newTokenForm: FormGroup;

  readonly title = $localize`:@@security_new_token_title:New Token`;

  tokenClaims: TokenClaim[] = [];

  typeControl: FormControl;

  validFromDateControl: FormControl;

  private readonly matDialog = inject(MatDialog);

  private readonly securityService = inject(SecurityService);

  constructor() {
    super();

    // Retrieve the route parameters
    this.existingTokenId = this.activatedRoute.snapshot.paramMap.get('existingTokenId');

    // Initialize the form controls
    this.expiryDateControl = new FormControl('');
    this.descriptionControl = new FormControl('', [Validators.maxLength(200)]);
    this.nameControl = new FormControl('', [Validators.required, Validators.maxLength(100)]);
    this.typeControl = new FormControl('', [Validators.required]);
    this.validFromDateControl = new FormControl('');

    // Initialize the form
    this.newTokenForm = new FormGroup({
      description: this.descriptionControl,
      expiryDate: this.expiryDateControl,
      name: this.nameControl,
      type: this.typeControl,
      validFromDate: this.validFromDateControl
    });
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@security_new_token_back_navigation:Tokens`, ['.'], {
      relativeTo: this.activatedRoute.parent
    });
  }

  cancel(): void {
    void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
  }

  deleteTokenClaim(existingTokenClaim: TokenClaim): void {
    this.tokenClaims.forEach((tokenClaim, index) => {
      if (tokenClaim.name === existingTokenClaim.name) {
        this.tokenClaims.splice(index, 1);
      }
    });
  }

  editTokenClaim(existingTokenClaim: TokenClaim): void {
    const data: TokenClaimDialogData = {
      name: existingTokenClaim.name,
      readonlyName: true,
      value: existingTokenClaim.value,
      values: existingTokenClaim.values
    };

    const dialogRef: MatDialogRef<TokenClaimDialogComponent, TokenClaim> = this.matDialog.open(
      TokenClaimDialogComponent,
      {
        restoreFocus: false,
        data
      }
    );

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe((tokenClaim: TokenClaim | undefined) => {
        if (tokenClaim) {
          for (const aTokenClaim of this.tokenClaims) {
            if (aTokenClaim.name === tokenClaim.name) {
              aTokenClaim.value = tokenClaim.value;
              aTokenClaim.values = tokenClaim.values;
              return;
            }
          }
        }
      });
  }

  newTokenClaim(): void {
    const data: TokenClaimDialogData = {
      name: '',
      value: '',
      values: [],
      readonlyName: false
    };

    const dialogRef: MatDialogRef<TokenClaimDialogComponent, TokenClaim> = this.matDialog.open(
      TokenClaimDialogComponent,
      {
        restoreFocus: false,
        data
      }
    );

    dialogRef
      .afterClosed()
      .pipe(first())
      .subscribe((tokenClaim: TokenClaim | undefined) => {
        if (tokenClaim) {
          for (const aTokenClaim of this.tokenClaims) {
            if (aTokenClaim.name === tokenClaim.name) {
              this.dialogService.showErrorDialog(new Error('The token claim already exists.'));

              return;
            }
          }

          this.tokenClaims.push(tokenClaim);

          this.tokenClaims.sort((a: TokenClaim, b: TokenClaim) => {
            if ((a.name ? a.name.toLowerCase() : '') < (b.name ? b.name.toLowerCase() : '')) {
              return -1;
            }
            if ((a.name ? a.name.toLowerCase() : '') > (b.name ? b.name.toLowerCase() : '')) {
              return 1;
            }
            return 0;
          });
        }
      });
  }

  ngAfterViewInit(): void {
    if (this.existingTokenId) {
      // Retrieve the existing token and populate the form fields
      this.spinnerService.showSpinner();

      this.securityService
        .getToken(this.existingTokenId)
        .pipe(
          first(),
          finalize(() => this.spinnerService.hideSpinner())
        )
        .subscribe({
          next: (existingToken: Token) => {
            this.nameControl.setValue(existingToken.name);
            this.typeControl.setValue(existingToken.type);
            this.descriptionControl.setValue(existingToken.description);

            if (existingToken.validFromDate) {
              this.validFromDateControl.setValue(ISO8601Util.toDate(existingToken.validFromDate));
            }

            if (existingToken.expiryDate) {
              this.expiryDateControl.setValue(ISO8601Util.toDate(existingToken.expiryDate));
            }

            this.tokenClaims = existingToken.claims;
          },
          error: (error: Error) => this.handleError(error, false)
        });
    }
  }

  ok(): void {
    if (!this.newTokenForm.valid) {
      return;
    }

    const generateTokenRequest: GenerateTokenRequest = new GenerateTokenRequest(
      TokenType.JWT,
      '',
      '',
      [],
      undefined,
      undefined
    );

    generateTokenRequest.name = this.nameControl.value;
    generateTokenRequest.type = this.typeControl.value;

    if (this.descriptionControl.value) {
      generateTokenRequest.description = this.descriptionControl.value;
    }

    if (this.validFromDateControl.value) {
      generateTokenRequest.validFromDate = ISO8601Util.toString(this.validFromDateControl.value);
    }

    if (this.expiryDateControl.value) {
      generateTokenRequest.expiryDate = ISO8601Util.toString(this.expiryDateControl.value);
    }

    generateTokenRequest.claims = this.tokenClaims;

    console.log('generateTokenRequest = ', generateTokenRequest);

    this.spinnerService.showSpinner();

    this.securityService
      .generateToken(generateTokenRequest)
      .pipe(
        first(),
        finalize(() => this.spinnerService.hideSpinner())
      )
      .subscribe({
        next: () => {
          void this.router.navigate(['.'], { relativeTo: this.activatedRoute.parent });
        },
        error: (error: Error) => this.handleError(error, false)
      });
  }
}
