export default function GameMode (bot: any, config: any) {
  bot.on('packet.game_state_change', (packet: any) => {
    if (packet.reason === "change_game_mode") {
      bot.selfcare.gamemode = packet.gameMode
    } if (packet.reason === "win_game") {
      bot._client.write('client_command', { action: 0 })
    }
  })
}
