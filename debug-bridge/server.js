const io = require('socket.io');
let port = 8080;
// should i even have this here?
const server = new io.Server(8080);
console.log('server started');

server.on('connection', (socket) => {
  socket.on('player', (data) => {
    console.log(data)
  });

  socket.on('disguised', (data) => {
    console.log(data)
  });

  socket.on('system', (data) => {
    console.log(data)
  });

/*  socket.on('meow', (data) => {
    console.log(data)
  })*/
});
