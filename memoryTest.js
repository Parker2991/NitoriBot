const v8 = require('v8');
const { totalmem, freemem } = require('os');
const { heap_size_limit } = v8.getHeapStatistics();
const { heapUsed, heapTotal, rss } = process.memoryUsage();
let mbDivide = 1048576;
setInterval(() => {
  let osMemUsed = Math.floor((totalmem() / mbDivide) - freemem() / mbDivide)
  console.log(`Heap mem: ${Math.floor(heapUsed / mbDivide)} MB / ${Math.floor(heapTotal / mbDivide)} MB`);
  console.log(`Process mem: ${Math.floor(rss / mbDivide)} MB / ${Math.floor(heap_size_limit / mbDivide - 192)} MB`);
  console.log(`OS mem: ${osMemUsed} MB / ${Math.floor(totalmem() / mbDivide)} MB`)
}, 1000)