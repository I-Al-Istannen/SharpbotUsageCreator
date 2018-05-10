package me.ialistannen.sharpbotusagecreator.replacement

import com.google.gson.JsonObject
import jdk.nashorn.api.scripting.NashornScriptEngineFactory
import me.ialistannen.sharpbotusagecreator.Json
import me.ialistannen.sharpbotusagecreator.github.GithubEntry
import me.ialistannen.sharpbotusagecreator.parser.Command
import javax.script.ScriptEngine

interface GithubEntryTransformer {

    companion object {
        private val engine: ScriptEngine = NashornScriptEngineFactory()
                .getScriptEngine("--language=es6")
    }

    /**
     * Checks if this transformer can transform the given entry.
     *
     * @param entry the entry to check against
     * @param content the content of the entry
     * @return true if this transformer can transform the entry
     */
    fun canTransform(entry: GithubEntry, content: String): Boolean

    /**
     * Transforms this command into a list of [JsonObject]s,
     * which hold the information about the commands.
     * This is normally the `exports.info` section.
     *
     * @param content the content of the command
     * @return the transformed command(s)
     */
    fun transform(content: String): List<Command>

    /**
     * Returns the exports section as a String, if there is any.
     *
     * Format:
     * ```java
     * info = { /* stuff */ }
     * ```
     *
     * @param content the content of the command
     * @return the exports section or null if not found
     */
    fun extractExportsSection(content: String): String? {
        val matchResult = EXTRACTOR_REGEX.find(content)
                ?: return null

        return matchResult.groupValues[1]
    }

    /**
     * Parses a given string with nashorn and then converts the resulting object into
     * a [JsonObject].
     *
     * @param string the string (object) to parse
     * @return the parsed JsonObject
     */
    fun parseWithNashornToJson(string: String): JsonObject? = try {
        val eval = engine.eval(string)

        Json.toTree(eval)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }

    /**
     * Parses a given string with nashorn.
     *
     * @param string the string (object) to parse
     * @return the result of invoking nashorn
     */
    fun parseWithNashorn(string: String) = try {
        engine.eval(string)
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }

}

private val EXTRACTOR_REGEX = Regex("""exports.(info = \{[\w\W]+});""")
