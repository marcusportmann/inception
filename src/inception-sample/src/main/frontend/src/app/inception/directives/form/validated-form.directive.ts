import {
  AfterViewInit,
  Directive,
  Host,
  Optional,
  Self, ViewContainerRef
} from '@angular/core'

import {
  FormGroupDirective,
} from '@angular/forms';



@Directive({
  selector: 'form[validatedForm]',
  host: {'(submit)': 'onSubmit($event)', '(reset)': 'onReset()'}
})
export class ValidatedFormDirective implements AfterViewInit {

  constructor(
    private viewContainer: ViewContainerRef,
    @Host() @Self() @Optional() private formGroupDirective: FormGroupDirective) {
  }

  ngAfterViewInit() {
  }

  public onReset(): void {
  }

  public onSubmit($event: Event): boolean {

    // Mark all controls as touched
    if (this.formGroupDirective && this.formGroupDirective.control && this.formGroupDirective.control.controls) {

      let form = this.formGroupDirective.control;

      for (let controlName in form.controls) {

        form.controls[controlName].markAsTouched();
      }
    }

    // Find the first invalid form control and set focus to it
    if (this.checkForInvalidFormControlAndSetFocus(this.viewContainer.element.nativeElement)) {
      return false;
    }

    return true;
  }

  private checkForInvalidFormControlAndSetFocus(nativeElement: any): boolean {

    if (nativeElement.children && (nativeElement.children.length > 0)) {
      for (let i = 0; i < nativeElement.children.length; i++) {

        let nativeChildElement = nativeElement.children[i];

        if (nativeChildElement && nativeChildElement.nodeName && this.isFormElement(nativeChildElement.nodeName)) {

          if (this.formGroupDirective && this.formGroupDirective.control && this.formGroupDirective.control.controls) {

            let formControl = this.formGroupDirective.control.controls[nativeChildElement.name];

            if (formControl && formControl.invalid) {

              nativeChildElement.focus();

              return true;
            }
          }
        }

        if (this.checkForInvalidFormControlAndSetFocus(nativeChildElement)) {
          return true;
        }
      }
    }

    return false;
  }

  private isFormElement(nodeName: string): boolean {

    switch(nodeName) {
      case 'INPUT': {
        return true;
      }

      default: {
        return false;
      }
    }
  }
}
