import { Bot } from '../Bot';
export class Position {
  public x: any;

  public y: any;

  public z: any;

  constructor () {
    const bot = Bot;
    const listener = bot.listener;

    listener.on("packet.position", (packet: any) => {
      this.x = packet.x;
      this.y = packet.y;
      this.z = packet.z;

      bot._client.write("teleport_confirm", { teleportId: packet.teleportId });

      listener.emit("move");
    });

    listener.on("end", () => {
      this.x = null;
      this.y = null;
      this.z = null;
    })
  }
}