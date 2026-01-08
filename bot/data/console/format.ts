import translations from '../../resources/Translations.json';

export default function format (
  bot: any,
  time: any,
  dayName: any,
  month: any,
  dayNumber: any,
  year: any,
  type: any,
  server: any,
  message: any,
) {
  let component = [
    {
      translate: translations["console.format.text"],
      color: "light_purple",
      with: [
        { text: `${time}`, color: "blue" },
        {
          translate: translations["console.format.date"],
          color: "aqua",
          with: [
            dayName,
            month,
            dayNumber,
            year
          ]
        },
        type,
        { text: `${server}`, color: "aqua" },
      ]

    },
    bot.getMessageAsPrismarine(message)?.toAnsi()
  ]
  return component;
}
