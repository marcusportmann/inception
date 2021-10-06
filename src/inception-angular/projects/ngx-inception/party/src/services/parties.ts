// /*
//  * Copyright 2021 Marcus Portmann
//  *
//  * Licensed under the Apache License, Version 2.0 (the "License");
//  * you may not use this file except in compliance with the License.
//  * You may obtain a copy of the License at
//  *
//  *   http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * Unless required by applicable law or agreed to in writing, software
//  * distributed under the License is distributed on an "AS IS" BASIS,
//  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the License for the specific language governing permissions and
//  * limitations under the License.
//  */
//
// import {SortDirection} from 'ngx-inception/core';
// import {PartySortBy} from "./party-sorty-by";
//
// /**
//  * The Parties class holds the results of a request to retrieve a list of parties.
//  *
//  * @author Marcus Portmann
//  */
// export class Parties {
//
//   /**
//    * The optional filter that was applied to the parties.
//    */
//   filter?: string;
//
//   /**
//    * The optional page index.
//    */
//   pageIndex?: number;
//
//   /**
//    * The optional page size.
//    */
//   pageSize?: number;
//
//   /**
//    * The parties.
//    */
//   parties: Party[];
//
//   /**
//    * The optional method used to sort the parties e.g. by name.
//    */
//   sortBy?: PartySortBy;
//
//   /**
//    * The optional sort direction that was applied to the parties.
//    */
//   sortDirection?: SortDirection;
//
//   /**
//    * The Universally Unique Identifier (UUID) for the tenant the parties are associated with.
//    */
//   tenantId: string;
//
//   /**
//    * The total number of parties.
//    */
//   total: number;
//
//   /**
//    * Constructs a new Parties.
//    *
//    * @param tenantId      The Universally Unique Identifier (UUID) for the tenant the parties are
//    *                      associated with.
//    * @param parties       The parties.
//    * @param total         The total number of parties.
//    * @param filter        The optional filter that was applied to the parties.
//    * @param sortBy        The optional method used to sort the parties e.g. by name.
//    * @param sortDirection The optional sort direction that was applied to the parties.
//    * @param pageIndex     The optional page index.
//    * @param pageSize      The optional page size.
//    */
//   constructor(tenantId: string, parties: Party[], total: number, filter?: string, sortBy?: PartySortBy,
//               sortDirection?: SortDirection, pageIndex?: number, pageSize?: number) {
//     this.tenantId = tenantId;
//     this.parties = parties;
//     this.total = total;
//     this.filter = filter;
//     this.sortBy = sortBy;
//     this.sortDirection = sortDirection;
//     this.pageIndex = pageIndex;
//     this.pageSize = pageSize;
//   }
// }
