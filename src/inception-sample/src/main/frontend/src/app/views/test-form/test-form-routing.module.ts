import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import {TestFormComponent} from './test-form.component';

const routes: Routes = [
  {
    path: '',
    component: TestFormComponent,
    data: {
      title: 'Test Form'
    }
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class TestFormRoutingModule {}
