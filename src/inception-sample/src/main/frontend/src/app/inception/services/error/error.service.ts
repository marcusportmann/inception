import { Injectable } from "@angular/core";

import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

import { ErrorModalComponent } from "../../components/layout/error-modal";


@Injectable()
export class ErrorService {
  constructor(private bsModalService: BsModalService) {
  }
  showConfirm(title?: string, message?: string) {
    let bsModalRef = this.bsModalService.show(ErrorModalComponent, { animated: true, keyboard: true, backdrop: true, ignoreBackdropClick: false });
    console.log("bsModalRef: ", bsModalRef);
  }
}
