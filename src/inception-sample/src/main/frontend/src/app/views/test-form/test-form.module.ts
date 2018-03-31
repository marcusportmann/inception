// Import Angular decorators
import { NgModule } from '@angular/core';

// Import Inception module
import {InceptionModule} from '../../inception/inception.module';

// Local imports
import {TestFormComponent} from './test-form.component';
import {TestFormRoutingModule} from './test-form-routing.module';

@NgModule({
  imports: [
    InceptionModule,

    TestFormRoutingModule
  ],
  declarations: [
    TestFormComponent
  ],
  exports: [
  ]
})
export class TestFormModule { }
