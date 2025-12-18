import v8 from 'v8';
const { heap_size_limit } = v8.getHeapStatistics();

export class memUsage {
  constructor (context: any) {
    const bot = context.bot;
    const config = context.config;

    setInterval(() => {
      let mbDivide = 1048576;
      bot.console.info(`[Heap Memory] ${Math.floor(process.memoryUsage().heapUsed / mbDivide)} MB / ${Math.floor(process.memoryUsage().heapTotal / mbDivide)} MB`);
      bot.console.info(`[Process Memory] ${Math.floor(process.memoryUsage.rss() / mbDivide)} MB / ${Math.floor(heap_size_limit / mbDivide - 192)} MB`);
    }, 1000);
  }
}

/*
setInterval(() => {
  let osMemUsed = Math.floor((totalmem() / mbDivide) - freemem() / mbDivide)
  console.log(`Heap mem: ${Math.floor(heapUsed / mbDivide)} MB / ${Math.floor(heapTotal / mbDivide)} MB`);
  console.log(`Process mem: ${Math.floor(rss / mbDivide)} MB / ${Math.floor(heap_size_limit) / mbDivide} MB`);
  console.log(`OS mem: ${osMemUsed} MB / ${Math.floor(totalmem() / mbDivide)} MB`)
}, 1000)
*/