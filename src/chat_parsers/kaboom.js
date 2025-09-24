const util = require('util')

function kaboom (message, data) {
  if (message === null || typeof message !== 'object') return

  if (message.text !== '' || !Array.isArray(message.extra) || message.extra.length < 3) return

  const children = message.extra

  const prefix = children[0]
  let displayName = data.senderName ?? { text: '' }
  let contents = { text: '' }
  //console.log(data.players.find((e) => e.uuid ==))
/*
player chat:
  senderUuid: '85f5b68d-a567-3877-9701-3cd7404bc9d9',
  networkName: { extra: [ [Object] ], text: '' },

diguised chat:
  senderName: { extra: [ [Object] ], text: '' },
*/
  if (isSeparatorAt(children, 1)) { // Missing/blank display name
    if (children.length > 3) contents = children[3]
  } else if (isSeparatorAt(children, 2)) {
    displayName = children[1]
    if (children.length > 4) contents = children[4]
  } else {
    return undefined
  }

  const playerListDisplayName = { extra: [prefix, displayName], text: '' }
 // console.log(JSON.stringify(playerListDisplayName))
  //console.log(JSON.stringify())
  //console.log(`Player list display name from Chat Parser ${data.getMessageAsPrismarine(playerListDisplayName)?.toMotd()}`)
  //console.log(`${data.getMessageAsPrismarine(displayName)?.toMotd()}`)
  let sender
  if (data.uuid) {
    sender = data.players.find(player => player.uuid === data.senderUuid)
  } else {
    const playerListDisplayName = { extra: [prefix, displayName], text: '' }
    sender = data.players.find(player => util.isDeepStrictEqual(player.displayName, playerListDisplayName))
  }

  if (!sender) return undefined

  return { sender, contents, type: "minecraft:chat", displayName, chatType: data.chatType }
}

function isSeparatorAt (children, start) {
  return (children[start]?.text === ':' || children[start]?.text === '\xa7f:') && children[start + 1]?.text  === ' ' 
}

module.exports = kaboom
