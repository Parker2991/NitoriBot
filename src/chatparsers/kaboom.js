const util = require('util')

function kaboom (message, data) {
  if (message === null || typeof message !== 'object') return

  if (message.text !== '' || !Array.isArray(message.extra) || message.extra.length < 3) return

  const children = message.extra

  const prefix = children[0]
  let displayName = data.senderName ?? { text: '' }
  let contents = { text: '' }

  if (isSeparatorAt(children, 1)) { // Missing/blank display name
    if (children.length > 3) contents = children[3]
  } else if (isSeparatorAt(children, 2)) {
    displayName = children[1]
    if (children.length > 4) contents = children[4]
  } else {
    return undefined
  }

  const playerListDisplayName = { extra: [prefix, displayName], text: '' }
  let sender
  if (data.uuid) {
    sender = data.players.find(player => player.uuid === data.senderUuid)
  } else {
    const playerListDisplayName = { extra: [prefix, displayName], text: '' }
    sender = data.players.find(player => util.isDeepStrictEqual(player.displayName, playerListDisplayName))
  }

  if (!sender) return undefined

  return { sender, contents, type: 'minecraft:chat', displayName }
}

function isSeparatorAt (children, start) {
  return (children[start]?.text === ':' || children[start]?.text === '\xa7f:') && children[start + 1]?.text  === ' ' 
}

module.exports = kaboom
