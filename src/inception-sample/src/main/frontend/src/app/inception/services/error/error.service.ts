import {
  Injectable,
  Injector,
  ComponentFactoryResolver,
  EmbeddedViewRef,
  ApplicationRef,
  ComponentRef
} from '@angular/core';
import {ErrorModalComponent} from "../../components/layout/error-modal";


@Injectable()
export class ErrorService {

  private componentRef: ComponentRef;

  constructor(
    private componentFactoryResolver: ComponentFactoryResolver,
    private appRef: ApplicationRef,
    private injector: Injector) {

    console.log('Initialising ErrorService...');

    this.appendComponentToBody(ErrorModalComponent);

  }

  appendComponentToBody(component: any) {
    // Create a component reference from the component
    this.componentRef = this.componentFactoryResolver
      .resolveComponentFactory(component)
      .create(this.injector);

    // Attach component to the appRef so that it's inside the ng component tree
    this.appRef.attachView(this.componentRef.hostView);

    // Get DOM element from component
    const domElem = (this.componentRef.hostView as EmbeddedViewRef<any>)
      .rootNodes[0] as HTMLElement;

    // Append DOM element to the body
    document.body.appendChild(domElem);

    // Wait some time and remove it from the component tree and from the DOM
    /*
    setTimeout(() => {
      this.appRef.detachView(componentRef.hostView);
      componentRef.destroy();
    }, 3000);
    */
  }


  show() {

    console.log('Showing error modal...');

    (<ErrorModalComponent>this.componentRef.instance).show();




  }


}
