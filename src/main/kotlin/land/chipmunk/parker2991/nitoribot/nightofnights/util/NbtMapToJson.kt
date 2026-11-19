package land.chipmunk.parker2991.nitoribot.nightofnights.util

import org.cloudburstmc.nbt.NbtList
import org.cloudburstmc.nbt.NbtMap
import com.google.gson.*

object NbtMapToJson {

    fun convert(map: NbtMap): JsonObject {
        val json = JsonObject()

        for ((key, value) in map.entries) {
            // ifEmpty to fix cloudburstmc NbtMap having empty key for literal strings in text components (e.g. "text" -> {"":"text"})
            json.add(key.ifEmpty { "text" }, convertValue(value))
        }

        return json
    }

    private fun convertValue(value: Any?): JsonElement {
        return when (value) {
            null -> JsonNull.INSTANCE

            is Byte -> JsonPrimitive(value)
            is Short -> JsonPrimitive(value)
            is Int -> JsonPrimitive(value)
            is Long -> JsonPrimitive(value)
            is Float -> JsonPrimitive(value)
            is Double -> JsonPrimitive(value)

            is String -> JsonPrimitive(value)

            is ByteArray -> {
                val arr = JsonArray()
                for (b in value) arr.add(b.toInt())
                arr
            }

            is IntArray -> {
                val arr = JsonArray()
                for (i in value) arr.add(i)
                arr
            }

            is LongArray -> {
                val arr = JsonArray()
                for (l in value) arr.add(l)
                arr
            }

            is NbtMap -> convert(value)

            is NbtList<*> -> {
                val arr = JsonArray()
                for (element in value) {
                    arr.add(convertValue(element))
                }
                arr
            }

            is Boolean -> JsonPrimitive(value)

            else -> JsonPrimitive(value.toString())
        }
    }
}