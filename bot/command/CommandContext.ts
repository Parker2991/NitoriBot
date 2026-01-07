export class CommandContext {
  public data: any
  constructor (aliases: any, description: any, usages: any) {
    this.data = {};

    this.data.aliases = aliases;

    this.data.description = description;

    this.data.usages = usages;
  }
}