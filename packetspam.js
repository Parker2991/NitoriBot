const mc = require('minecraft-protocol')

const client = mc.createClient({
  host: "kaboom.pw",
  username: "among",
  version: "1.20.2"
})

client.on('login', (data) => {
//  console.log(client.write)
  setInterval(() => {
    client.write('set_creative_slot', {
      slot: 1,
      item: {
    
        present: true,
        itemId: 1,
        itemCount: 64
      }
    })
  }, 30)
})
