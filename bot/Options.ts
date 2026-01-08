import { UsernameGen } from './util/UsernameGen';

export default class options {
  public host: string = "localhost"
  public port: number = 25565;
  public username = UsernameGen();
  public serverName: string = "localhost";
  public hideErrors: boolean = true;
  public scInterval: number = 1000;
  public version: '1.21.8';
  public reconnectDelay: number = 3000
}
