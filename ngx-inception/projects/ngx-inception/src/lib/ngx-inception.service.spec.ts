import { TestBed } from '@angular/core/testing';

import { NgxInceptionService } from './ngx-inception.service';

describe('NgxInceptionService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: NgxInceptionService = TestBed.get(NgxInceptionService);
    expect(service).toBeTruthy();
  });
});
