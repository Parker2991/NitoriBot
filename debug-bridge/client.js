const io = require('socket.io-client');

const client = io.connect('http://localhost:8080');

client.on('connect', () => {
  client.emit('meow', ':3') 
})
