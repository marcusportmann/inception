import {Error} from "../error/error";


export class LoginError extends Error {

  public code: number;

  constructor(code: number) {
    super();

    this.code = code;
  }
}
