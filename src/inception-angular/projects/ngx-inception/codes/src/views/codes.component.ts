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

import { ChangeDetectionStrategy, Component, HostBinding, inject } from '@angular/core';
import {
  BackNavigation, CoreModule, FilteredPaginatedListView, TableFilterComponent
} from 'ngx-inception/core';
import { Observable } from 'rxjs';
import { Code } from '../services/code';
import { CodesService } from '../services/codes.service';

/**
 * The CodesComponent class implements the Codes component.
 *
 * @author Marcus Portmann
 */
@Component({
  selector: 'inception-codes-codes',
  standalone: true,
  templateUrl: 'codes.component.html',
  imports: [CoreModule, TableFilterComponent],
  styleUrls: ['codes.component.css'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CodesComponent extends FilteredPaginatedListView<Code> {
  readonly codeCategoryId: string;

  readonly defaultSortActive = 'id';

  readonly displayedColumns = ['id', 'name', 'actions'] as const;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listStateKey: string;

  readonly title = $localize`:@@codes_codes_title:Codes`;

  private readonly codesService = inject(CodesService);

  constructor() {
    super();

    // Retrieve the route parameters
    const codeCategoryId = this.activatedRoute.snapshot.paramMap.get('codeCategoryId');
    if (!codeCategoryId) {
      throw new globalThis.Error('No codeCategoryId route parameter found');
    }
    this.codeCategoryId = codeCategoryId;

    this.listStateKey = `codes.${encodeURIComponent(this.codeCategoryId)}`;
  }

  override get backNavigation(): BackNavigation {
    return new BackNavigation($localize`:@@codes_codes_back_navigation:Code Categories`, ['.'], {
      relativeTo: this.activatedRoute.parent?.parent?.parent
    });
  }

  deleteCode(codeId: string): void {
    this.confirmAndProcessAction(
      $localize`:@@codes_codes_confirm_delete_code:Are you sure you want to delete the code?`,
      () => this.codesService.deleteCode(this.codeCategoryId, codeId)
    );
  }

  editCode(codeId: string): void {
    void this.router.navigate([codeId, 'edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newCode(): void {
    void this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (data: Code, filter: string) => boolean {
    return (data: Code, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').trim().toLowerCase();
      const id = (data.id ?? '').toLowerCase();
      const name = (data.name ?? '').toLowerCase();
      return id.includes(normalizedFilter) || name.includes(normalizedFilter);
    };
  }

  protected override fetchData(): Observable<Code[]> {
    return this.codesService.getCodesForCodeCategory(this.codeCategoryId);
  }
}
