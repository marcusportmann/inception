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

import { Component, HostBinding, inject } from '@angular/core';
import { CoreModule, FilteredPaginatedListView, TableFilterComponent } from 'ngx-inception/core';
import { Observable } from 'rxjs';

import { Config } from '../services/config';
import { ConfigService } from '../services/config.service';

/**
 * The ConfigsComponent class implements the Configs component.
 *
 * @author Marcus
 */
@Component({
  selector: 'inception-config-configs',
  standalone: true,
  imports: [CoreModule, TableFilterComponent],
  templateUrl: 'configs.component.html',
  styleUrls: ['configs.component.css']
})
export class ConfigsComponent extends FilteredPaginatedListView<Config> {
  readonly displayedColumns = ['id', 'value', 'actions'] as const;

  @HostBinding('class') readonly hostClass = 'flex flex-column flex-fill';

  readonly listKey = 'config.configs';

  readonly title = $localize`:@@config_configs_title:Configs`;

  private configService = inject(ConfigService);

  deleteConfig(id: string): void {
    this.confirmAndProcessAction(
      $localize`:@@config_configs_confirm_delete_config:Are you sure you want to delete the config?`,
      () => this.configService.deleteConfig(id)
    );
  }

  editConfig(id: string): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate([encodeURIComponent(id), 'edit'], {
      relativeTo: this.activatedRoute
    });
  }

  newConfig(): void {
    // noinspection JSIgnoredPromiseFromCall
    this.router.navigate(['new'], { relativeTo: this.activatedRoute });
  }

  protected override createFilterPredicate(): (data: Config, filter: string) => boolean {
    return (data: Config, filter: string): boolean => {
      const normalizedFilter = (filter ?? '').trim().toLowerCase();
      const id = (data.id ?? '').toLowerCase();
      return id.includes(normalizedFilter);
    };
  }

  protected override fetchData(): Observable<Config[]> {
    return this.configService.getConfigs();
  }
}
