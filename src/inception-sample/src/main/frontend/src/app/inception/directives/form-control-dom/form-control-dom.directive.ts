import {
  Directive,
  ElementRef,
  forwardRef,
  Inject,
  Input,
  Optional,
  Renderer2
} from '@angular/core'
import {
  COMPOSITION_BUFFER_MODE,
  ControlValueAccessor, FormControl, FormGroup,
  NG_VALUE_ACCESSOR, NgControl
} from '@angular/forms';






@Directive({
  selector: 'input[formControlName],input[formControl],input[ngModel],select[formControlName],select[formControl],select[ngModel]',
  providers: [

  ],
  host: {
    '(focus)': 'handleFocus()',
    '(blur)': 'handleBlur()'
  }
})
export class FormControlDomDirective {

  constructor(
    private elementRef: ElementRef,
    private control : NgControl
  ) {
    //console.log('DOM of formControl: ',this.elementRef.nativeElement);
    //console.log('formControl: ',this.control);

    if (control.control)
    {

      //(<any>control.control).nativeElement =  this.elementRef.nativeElement;
    }


  }

  private handleFocus(): void {

  }

  private handleBlur(): void {

  }

}
