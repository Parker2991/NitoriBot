const mc = require('minecraft-protocol');

const client = mc.createClient({
  host: "amy.lat.",
  port: "25565",
  username: "eee",
  version: "1.20.2",
  hideErrors: false,
})
// username, playerUUID
//client.write('keep_alive');
client.on('init_client', () => {
//  client.write('login_start', { username: client.username, playerUUID: client.uuid });
});
//console.log(client.write);
