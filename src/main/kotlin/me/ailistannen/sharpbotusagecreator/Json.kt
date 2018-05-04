package me.ailistannen.sharpbotusagecreator

import com.google.gson.Gson

object Json {
    val gson: Gson = Gson()

    /**
     * Parses the given String to a POJO.
     *
     * @param json the json to parse
     * @return the created object
     */
    inline fun <reified T> parse(json: String): T {
        return gson.fromJson<T>(json, T::class.java)
    }
}