const BOOLEAN_TYPES = [
  "interpret",
  "bold",
  "italic",
  "underlined",
  "strikethrough",
  "obfuscated",
];
//function convertNbtComponentToJson(key, tag) {
export default {
  convertNbtComponentToJson (key, tag) {
    if (!tag) {
      return null;
    } else if (tag.type === "compound") {
      const object = {};

      for (const key of Object.keys(tag.value)) {
        object[key === "" ? "text" : key] = this.convertNbtComponentToJson(
          key,
          tag.value[key],
        );
      }

      return object;
    } else if (tag.type === "list") {
      const array = [];

      for (const item of tag.value.value) {
        array.push(
          this.convertNbtComponentToJson(null, { type: tag.value.type, value: item }),
        );
      }

      return array;
    } else if (tag.type === "byte") {
      if (key && BOOLEAN_TYPES.includes(key)) return tag.value !== 0;

      return tag.value;
    } else if (tag.type === "int") {
      return tag.value;
    } else if (tag.type === "string") {
      return tag.value;
    } else if (tag.type === "float") {
      return tag.value
    } else if (tag.type === "intArray") {
      const array = [];

      for (const value of tag.value) {
        array.push(value);
      }

      return array;
    } else if (tag.type === "short") {
      return tag.value;
    } else if (tag.type === "long") {
      return tag.value
    } else if (tag.type === "double") {
      return tag.value
    } else {
      console.log("Failed to parse NBT component type " + tag.type);

      return { text: "" };
    }
  }
}
