
export class Session {

  username: string;

  scopes: string[];

  functions: string[];

  expiry: Date;

  accessToken: string;

  refreshToken: string;

  constructor(username: string, scopes: string[], functions: string[], expiry:number, accessToken: string, refreshToken: string) {
    this.username = username;
    this.scopes = scopes;
    this.functions = functions;
    this.expiry = new Date(expiry * 1000);
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }
}
