const net = require('net');
const client = net.connect({ port: 25565, host: 'play.kaboom.pw', username: 'steve' }, () => {
  // 'connect' listener.
  console.log('connected to server!');
  client.write('world!\r\n');
});
/*client.on('data', (data) => {
  console.log(data.toString());
  client.end();
});
client.on('end', () => {
  console.log('disconnected from server');
});*/

console.log(client)
