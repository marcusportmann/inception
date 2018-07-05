import {Component, Input} from '@angular/core';
import {BsModalRef, BsModalService} from 'ngx-bootstrap/modal';
import {Error} from "../../errors/error";

@Component({
  selector: 'error-report-modal',
  template: `
    <div class="modal-header">
      <h4 class="modal-title pull-left">Error Report</h4>
      <button type="button" class="close float-right" aria-label="Close" (click)="bsModalRef.hide()">
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
    <div class="modal-body">
      <div class="form-group row">
        <label class="col-sm-12 col-form-label">Timestamp</label>
        <div class="col-sm-12">
          <span class="form-control-static">{{timestamp | date:'medium'}}</span>
        </div>
      </div>
      <div class="form-group row">
        <label class="col-sm-12 col-form-label">Message</label>
        <div class="col-sm-12">
          <span class="form-control-static">{{message}}</span>
        </div>
      </div>
      <div *ngIf="detail" class="form-group row">
        <label class="col-sm-12 col-form-label">Detail</label>
        <div class="col-sm-12">
          <span class="form-control-static">{{detail}}</span>
        </div>
      </div>      
    </div>
    <div class="modal-footer">
      <button type="button" class="btn btn-secondary" (click)="bsModalRef.hide()">Cancel</button>
      <button type="button" class="btn btn-primary" (click)="clickSend()">Send</button>
    </div>`,
  providers: [BsModalService]
})

export class ErrorReportModalComponent {

  /**
   * The date and time the error occurred.
   */
  @Input() timestamp: Date;

  /**
   * The message.
   */
  @Input() message: string;

  /**
   * The optional detail.
   */
  @Input() detail?: string;

  /**
   * The optional stack trace.
   */
  @Input() stackTrace?: string;

  constructor(public bsModalRef: BsModalRef) { }

  public clickSend() {

    this.bsModalRef.hide();
  }
}
