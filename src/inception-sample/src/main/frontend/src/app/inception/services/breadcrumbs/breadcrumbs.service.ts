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

import {AfterViewInit, Injectable} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router} from '@angular/router';
import {BehaviorSubject, Observable} from 'rxjs/index';
import {combineAll, filter} from 'rxjs/operators';
import {BreadcrumbTitleProvider} from "./breadcrumb-title-provider";

/**
 * The BreadcrumbsService class provides the Breadcrumbs Service implementation.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class BreadcrumbsService {

  breadcrumbs: Observable<Array<Object>>;

  private _breadcrumbs: BehaviorSubject<Array<Object>>;

  constructor(private router: Router, private route: ActivatedRoute) {

    this._breadcrumbs = new BehaviorSubject<Object[]>(new Array<Object>());

    this.breadcrumbs = this._breadcrumbs.asObservable();

    this.router.events.pipe(filter(event => event instanceof NavigationEnd)).subscribe((event) => {
      const breadcrumbs = [];
      let currentRoute = this.route.root,
        url = '';
      do {
        const childrenRoutes = currentRoute.children;
        currentRoute = null;
        // tslint:disable-next-line:no-shadowed-variable
        childrenRoutes.forEach(route => {
          if (route.outlet === 'primary') {
            const routeSnapshot = route.snapshot;

            if (routeSnapshot.component && canProvideBreadcrumbTitle(routeSnapshot.component)) {
              console.log('Found component that can provide a breadcrumb title =', routeSnapshot.component);
            }

            if (routeSnapshot.url.length > 0) {
              url += '/' + routeSnapshot.url.map(segment => segment.path).join('/');

              if (routeSnapshot.data.title) {
                breadcrumbs.push({
                  label: routeSnapshot.data.title,
                  url: url
                });
              }
            }
            currentRoute = route;
          }
        });
      } while (currentRoute);

      this._breadcrumbs.next(Object.assign([], breadcrumbs));

      return breadcrumbs;
    });
  }

}

function canProvideBreadcrumbTitle(arg: any): arg is BreadcrumbTitleProvider {

  //console.log('arg = ', arg);

  let result:boolean = (arg.getTitle !== undefined);

  return result;
}


