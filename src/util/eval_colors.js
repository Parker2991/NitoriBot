const styles = {
  bigint: '\xa76', // gold
  boolean: '\xa76', // gold
  date: '\xa75', // dark_purple
  module: '\xa7n', // underline
  name: undefined, //undefined
  null: '\xa73', // bold
  number: '\xa76', // gold
  regexp: '\xa74', // dark_red
  special: '\xa73', // blue
  string: '\xa72', // green
  symbol: '\xa72', // green
  undefined: '\xa78' // dark_gray
}

function stylize (str, styleType) {
  const style = styles[styleType]
  if (style !== undefined) return `${style}${str}\xa7r`
  return str
}

module.exports = { stylize, styles }
