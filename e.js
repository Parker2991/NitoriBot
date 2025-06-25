const { request } = require('undici');

(async () => {
  const url = await request('https://en.wikipedia.org/api/rest_v1/page/media-list/amongus');
  const json = await url.body.json();
  for (const e of json.items) {
    console.log(e.caption)
  }
})()