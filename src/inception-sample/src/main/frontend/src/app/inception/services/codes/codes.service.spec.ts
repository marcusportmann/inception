import { TestBed, inject } from '@angular/core/testing';

import { CodesService } from './codes.service';

describe('CodesService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [CodesService]
    });
  });

  it('should be created', inject([CodesService], (service: CodesService) => {
    expect(service).toBeTruthy();
  }));
});
