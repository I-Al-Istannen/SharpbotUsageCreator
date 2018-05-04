package me.ailistannen.sharpbotusagecreator.github

import me.ailistannen.sharpbotusagecreator.Json
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.*

class FileFetcher {

    /**
     * Returns the content for a file from github.
     *
     * @param url the url to read
     * @return the read content as a String
     */
    fun getContent(url: String): String {
        val json = URL(url).readText()
        val (content, encoding) = Json.parse<ApiResponse>(json)

        if (encoding != "base64") {
            throw RuntimeException("Unknown encoding: '$encoding'")
        }

        val sanitizedContent = content.replace("\n", "")
        return Base64.getDecoder().decode(sanitizedContent).toString(StandardCharsets.UTF_8)
    }

    data class ApiResponse(val content: String, val encoding: String)
}