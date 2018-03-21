


export class AppSession  {

  static appSession: AppSession;

  private constructor() {

  }

  public static getAppSession(): AppSession {

    console.log("Here!!!!!");

    if (!this.appSession)
    {
      console.log("Creating new AppSesion");
      this.appSession = new AppSession();
    }

    return this.appSession;
  }

  public isLoggedIn() : boolean {

    console.log("Invoking AppSession::isLoggedIn()");

    return false;
  }
}
