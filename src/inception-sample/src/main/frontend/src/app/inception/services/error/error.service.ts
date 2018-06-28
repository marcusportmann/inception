/*
 * Copyright 2018 Marcus Portmann
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

import { Injectable } from "@angular/core";

import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { ErrorModalComponent } from "../../components/error/index";

/**
 * The ErrorService class provides the capability to process an application error or back-end error
 * and submit an error report.
 *
 * @author Marcus Portmann
 */
@Injectable()
export class ErrorService {

  /**
   * Constructs a new ErrorService.
   *
   * @param {BsModalService} bsModalService The Bootstrap Modal Service.
   */
  constructor(private bsModalService: BsModalService) {
  }

  showConfirm(title?: string, message?: string) {
    let bsModalRef = this.bsModalService.show(ErrorModalComponent, { animated: true, keyboard: true, backdrop: true, ignoreBackdropClick: false });
    console.log("bsModalRef: ", bsModalRef);
  }
}
