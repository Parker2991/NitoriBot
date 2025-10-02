const { request, statusCode, Headers, trailers, body } = require('undici');

async function websiteRequesting (url) {
  const url = await request(String(url))
  console.log(url)
}

module.exports = websiteRequesting;
/*
 import { request } from 'undici'

const {
  statusCode,
  headers,
  trailers,
  body
} = await request('http://localhost:3000/foo')

console.log('response received', statusCode)
console.log('headers', headers)

for await (const data of body) { console.log('data', data) }

console.log('trailers', trailers)

*/

/*const https = require('https');
const http = require('http');

function websiteRequesting (url) {
  if (url.startsWith('http://')) {

  } else {


    https.request(url, '', (res) => {
      console.log(res)
    })

  }
}

module.exports = websiteRequesting;*/