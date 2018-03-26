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
  NG_VALUE_ACCESSOR, NgControl, NgForm
} from '@angular/forms';


export class ValidatedFormGroup extends FormGroup {



}




@Directive({
  selector: 'form[inceptionValidatedForm]'
//  host: {'(submit)': 'onSubmit($event)', '(reset)': 'onReset()'}
})
export class ValidatedFormDirective {

  constructor(
    private elementRef: ElementRef
  ) {
    console.log('this.elementRef.nativeElement: ', this.elementRef.nativeElement);
    //console.log('this.form: ', this.form);



  }

  onReset(): void {

  }

  onSubmit($event: Event): boolean {


    return false;
  }
}
