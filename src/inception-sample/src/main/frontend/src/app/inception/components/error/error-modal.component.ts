import { Component, Input } from '@angular/core';
import { BsModalRef, BsModalService } from 'ngx-bootstrap/modal';

@Component({
  selector: 'error-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title pull-left">{{title}}</h4>
      <button type="button" class="close float-right" aria-label="Close" (click)="bsModalRef.hide()">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      {{message}}
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="bsModalRef.hide()">Cancel</button>
      <button type="button" class="btn btn-primary" (click)="clickSend()">Send</button>
    </div>`,
  providers: [BsModalService]
})

export class ErrorModalComponent {
  @Input() title: string = 'Modal with component';
  @Input() message: string = 'Message here...';

  constructor(public bsModalRef: BsModalRef) { }

  public clickSend() {




    this.bsModalRef.hide();
  }
}
