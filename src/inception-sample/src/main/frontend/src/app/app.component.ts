import {Component, OnDestroy, OnInit} from '@angular/core';
import { Router, NavigationEnd } from '@angular/router';
import {Subject} from "rxjs";
import {takeUntil} from "rxjs/operators";

@Component({
  // tslint:disable-next-line
  selector: 'body',
  template: `<router-outlet></router-outlet>`
})
export class AppComponent implements OnInit, OnDestroy {

  private unsubscribe: Subject<any> = new Subject();

  constructor(private router: Router) { }

  ngOnDestroy(): void {
    this.unsubscribe.next();
    this.unsubscribe.complete();
  }

  ngOnInit(): void {
    this.router.events.pipe(
      takeUntil(this.unsubscribe)).subscribe((evt) => {
      if (!(evt instanceof NavigationEnd)) {
        return;
      }
      window.scrollTo(0, 0);
    });
  }
}
