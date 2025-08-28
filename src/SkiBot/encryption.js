const crypto = require('crypto');

class encrytion {
  static encrypt (args) {
    const algorithm = 'aes-192-cbc';
    const password = 'meow';

    const key = crypto.scryptSync(password, 'salt', 24);
    const iv = Buffer.alloc(16, 0); 
    const cipher = crypto.createCipheriv(algorithm, key, iv);
    const hash = crypto.createHash("sha512")
      .update(Math.floor(Date.now() / 2000) + "meow")
      .digest("hex")
      .substring(0, 16)

    const json = {
      player: "meow",
      uuid: ":3",
      hash: hash,
      args: args
    }

    let encrypted = cipher.update(`${JSON.stringify(json)}`, "uft8", "hex")
    encrypted += cipher.final('hex')

    return encrypted
  }
}

module.exports = encrytion