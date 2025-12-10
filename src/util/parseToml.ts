import { readFileSync } from 'fs';
import { join } from 'path';
import toml from 'smol-toml';
export function parseToml (path: any) {
  try {
    const file = readFileSync(join(path), 'utf8');
    const parse = toml.parse(file)
    /**
     * stringify if the parsed toml is not stringified and then parsed again with JSON.stringify and JSON.parse it will error with
     * Type 'TomlValue' must have a '[Symbol.iterator]()' method that returns an iterator. when we try to use it in a for const of statement
     */
    const stringify = JSON.stringify(parse);
    const parsed = JSON.parse(stringify)
    return parsed
  } catch (error: unknown) {
    console.error(`\x1b[31mFailed to parse toml due to\x1b[0m\n${error}`)
  }
}
