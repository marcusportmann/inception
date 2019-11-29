import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NgxInceptionComponent } from './ngx-inception.component';

describe('NgxInceptionComponent', () => {
  let component: NgxInceptionComponent;
  let fixture: ComponentFixture<NgxInceptionComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NgxInceptionComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NgxInceptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
