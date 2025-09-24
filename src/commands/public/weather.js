const { request } = require("undici");
const CommandContext = require("../../commandUtil/CommandContext");
const trustLevel = require("../../commandUtil/CommandTrustLevel");

class weather extends CommandContext {
  constructor() {
    super(
      "weather",
      [], 
      "check the weather in locations", 
      trustLevel.public, 
      ["<weather/zip code>"],
    );
  }
  async execute(context) {
    const bot = context.bot;
    const config = context.config;
    const args = context.arguments;
    const source = context.source;
    const translations = bot.translations;

    let component = [];

    try {
      const url = await request(
        `https://api.weatherapi.com/v1/current.json?key=${config.weatherApiKey}&q=${args.join(" ").replaceAll(" ", "%20")}`,
      );
      const info = await url.body.json();

      if (info.error) source.sendFeedback({
        translate: "fnfboyfriendbot.command.weather.unknown_location",
        fallback: translations["fnfboyfriendbot.command.weather.unknown_location"],
        color: "red",
        with: [
          { text: `${args.join(' ')}`}
        ]
      })

      component.push({
        translate: "%s\n%s\n%s\n%s\n%s\n%s\n%s",
        color: config.colors.commands.tertiary,
        with: [
          {
            translate: "fnfboyfriendbot.command.weather.location",
            fallback: translations["fnfboyfriendbot.command.weather.location"],
            color: config.colors.commands.primary,
            with: [
              { text: `${info.location.name}`, color: config.colors.commands.secondary },
              {
                text: `${info.location.region}`,
                color: config.colors.commands.secondary,
              },
              {
                text: `${info.location.country}`,
                color: config.colors.commands.secondary,
              },
            ]
          },
          {
            translate: "fnfboyfriendbot.command.weather.coords",
            fallback: translations["fnfboyfriendbot.command.weather.coords"],
            color: config.colors.commands.primary,
            with: [
              { text: `${info.location.lat}`, color: config.colors.integer },
              { text: `${info.location.lon}`, color: config.colors.integer }
            ]
          },
          {
            translate: "fnfboyfriendbot.command.weather.time",
            fallback: translations["fnfboyfriendbot.command.weather.time"],
            color: config.colors.commands.primary,
            with: [
              {
                text: `${new Date().toLocaleTimeString("en-US", { timeZone: info.location.tz_id })}`,
                color: config.colors.commands.secondary,
              },
              {
                text: `${info.location.tz_id}`,
                color: config.colors.commands.secondary,
              },
            ]
          },
          {
            translate: "fnfboyfriendbot.command.weather.temp",
            fallback: translations["fnfboyfriendbot.command.weather.temp"],
            color: config.colors.commands.primary,
            with: [
              {
                text: `${Math.floor(info.current.temp_c)}`,
                color: config.colors.integer,
              },
              {
                text: `${Math.floor(info.current.temp_f)}`,
                color: config.colors.integer,
              },
            ]
          },
          {
            translate: "fnfboyfriendbot.command.weather.wind_speed",
            fallback: translations["fnfboyfriendbot.command.weather.wind_speed"],
            color: config.colors.commands.primary,
            with: [
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
            ]
          },

          {
            translate: "fnfboyfriendbot.command.weather.condition",
            fallback: translations["fnfboyfriendbot.command.weather.condition"],
            color: config.colors.commands.primary,
            with: [
              {
                text: `${info.current.condition.text}`,
                color: config.colors.commands.secondary,
              },
            ]
          },
          {
            translate: "fnfboyfriendbot.command.weather.humidity",
            fallback: translations["fnfboyfriendbot.command.weather.humidity"],
            color: config.colors.commands.primary,
            with: [
              {
                text: `${Math.floor(info.current.humidity)}`,
                color: config.colors.integer,
              },
            ]
          }
        ]
      })

      source.sendFeedback(component)
    } catch (e) {
      if (e.toString() === "TypeError: Cannot read properties of undefined (reading 'name')") return
      else source.sendFeedback(e.toString())
    }
  }
}

module.exports = weather;
