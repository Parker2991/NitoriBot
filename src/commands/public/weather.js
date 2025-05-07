const CommandError = require("../../command_util/command_error");
const { request } = require("undici");
const CommandContext = require("../../command_util/command_context");

class WeatherCommand extends CommandContext {
  constructor() {
    super("weather", [], "check the weather in locations", 0, [
      "<weather/zip code>",
    ]);
  }
  async execute(context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const source = context.source;
    let component = [];

    try {
      const url = await request(
        `https://api.weatherapi.com/v1/current.json?key=${config.weatherApiKey}&q=${args.join(" ").replaceAll(" ", "%20")}`,
      );
      const info = await url.body.json();

      component.push({
        translate: "%s\n%s: %s, %s\n%s: %s (%s)\n%s\n%s\n%s: %s\n%s: %s",
        color: config.colors.commands.tertiary,
        with: [
          {
            translate: "%s: %s, %s %s",
            with: [
              { text: "Location", color: config.colors.commands.primary },
              {
                text: `${info.location.name}`,
                color: config.colors.commands.secondary,
              },
              {
                text: `${info.location.region}`,
                color: config.colors.commands.secondary,
              },
              {
                text: `${info.location.country}`,
                color: config.colors.commands.secondary,
              },
            ],
          },
          { text: "Coords", color: config.colors.commands.primary },
          { text: `${info.location.lat}`, color: config.colors.integer },
          { text: `${info.location.lon}`, color: config.colors.integer },
          { text: "Time", color: config.colors.commands.primary },
          {
            text: `${new Date().toLocaleTimeString("en-US", { timeZone: info.location.tz_id })}`,
            color: config.colors.commands.secondary,
          },
          {
            text: `${info.location.tz_id}`,
            color: config.colors.commands.secondary,
          },
          {
            translate: "%s: %s (%s), %s (%s)",
            with: [
              { text: "Temp", color: config.colors.commands.primary },
              {
                text: `${Math.floor(info.current.temp_c)}`,
                color: config.colors.integer,
              },
              { text: "\u00b0C", color: config.colors.commands.secondary },
              {
                text: `${Math.floor(info.current.temp_f)}`,
                color: config.colors.integer,
              },
              { text: "\u00b0F", color: config.colors.commands.secondary },
            ],
          },
          {
            translate: "%s: %s (%s, %s), %s (%s, %s)",
            with: [
              { text: "Wind Speed", color: config.colors.commands.primary },
              {
                text: `${Math.floor(info.current.wind_kph)}`,
                color: config.colors.integer,
              },
              { text: "kph", color: config.colors.commands.secondary },
              {
                text: `${info.current.wind_dir}`,
                color: config.colors.commands.secondary,
              },
              {
                text: `${Math.floor(info.current.wind_mph)}`,
                color: config.colors.integer,
              },
              { text: "mph", color: config.colors.commands.secondary },
              {
                text: `${info.current.wind_dir}`,
                color: config.colors.commands.secondary,
              },
            ],
          },
          { text: "Condition", color: config.colors.commands.primary },
          {
            text: `${info.current.condition.text}`,
            color: config.colors.commands.secondary,
          },
          { text: "Humidity", color: config.colors.commands.primary },
          {
            text: `${Math.floor(info.current.humidity)}`,
            color: config.colors.integer,
          },
        ],
      });

      if (source.sources.console) {
        bot.console.info(bot.getMessageAsPrismarine(component)?.toAnsi());
      } else {
        bot.tellraw("@a", component);
      }
    } catch (e) {
      bot.chat.message(e.toString());
    }
  }
}

module.exports = WeatherCommand;
