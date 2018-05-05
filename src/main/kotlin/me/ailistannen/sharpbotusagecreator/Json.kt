package me.ailistannen.sharpbotusagecreator

import com.google.gson.*
import jdk.nashorn.api.scripting.ScriptObjectMirror

object Json {
    val gson: Gson = GsonBuilder()
            .registerTypeAdapter(
                    ScriptObjectMirror::class.java,
                    JsonSerializer<ScriptObjectMirror> { src, _, context ->
                        if (src.isArray) {
                            val jsonArray = JsonArray()

                            src.values.forEach { jsonArray.add(context.serialize(it)) }

                            jsonArray
                        } else {
                            val jsonObject = JsonObject()

                            for (entry in src.entries) {
                                jsonObject.add(entry.key, context.serialize(entry.value))
                            }

                            jsonObject
                        }
                    }
            )
            .create()

    /**
     * Parses the given String to a POJO.
     *
     * @param json the json to parse
     * @return the created object
     */
    inline fun <reified T> parse(json: String): T {
        return gson.fromJson<T>(json, T::class.java)
    }

    /**
     * Parses the given String to a POJO.
     *
     * @param obj the object to convert to a json object
     * @return the created object
     */
    fun toTree(obj: Any): JsonObject {
        return gson.toJsonTree(obj).asJsonObject
    }
}