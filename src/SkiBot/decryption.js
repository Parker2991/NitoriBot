const crypto = require('crypto');

class decryption {
  static decrypt (context) {
    const { skibot } = context.config
    const encrypted = context.encrypted
    
    const algorithm = "aes-192-cbc";
    const password = skibot.password;
    const key = crypto.scryptSync(password, "salt", 24);
    const iv = Buffer.alloc(16, 0);
    const decipher = crypto.createDecipheriv(algorithm, key, iv)

    const input = encrypted
    let decrypted = decipher.update(`${encrypted}`, 'hex', 'utf8')
    decrypted += decipher.final('utf8')
    return decrypted
  }
}

module.exports = decryption;

