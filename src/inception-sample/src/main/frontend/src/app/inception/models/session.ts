


export class Session  {

  static appSession: Session;

  private constructor() {

  }

  public static getSession(): Session {

    console.log('Here!!!!!');

    if (!this.appSession) {
      console.log('Creating new AppSesion');
      this.appSession = new Session();
    }

    return this.appSession;
  }

  public isLoggedIn(): boolean {

    console.log('Invoking Session::isLoggedIn()');

    return false;
  }
}
