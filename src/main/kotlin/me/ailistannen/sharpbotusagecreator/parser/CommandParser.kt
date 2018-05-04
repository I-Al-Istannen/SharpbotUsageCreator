package me.ailistannen.sharpbotusagecreator.parser

import com.google.gson.JsonObject
import me.ailistannen.sharpbotusagecreator.Json
import me.ailistannen.sharpbotusagecreator.commands.isCommandFile
import me.ailistannen.sharpbotusagecreator.github.FileFetcher
import me.ailistannen.sharpbotusagecreator.github.GithubEntry

class CommandParser(private val entries: List<GithubEntry>) {

    private val fileFetcher: FileFetcher = FileFetcher()
    private var commands: List<Command>? = null

    /**
     * Returns all commands and parses when needed (i.e. web requests!).
     */
    fun getCommands(): List<Command> {
        if (commands == null) {
            return parse()
        }
        return commands!!
    }

    private fun parse(): List<Command> {
        commands = entries.filter { isCommandFile(it) }
                .map { it.url }
                .map { fileFetcher.getContent(it) }
                .map { Command(it) }
        return commands!!
    }

    data class Command(val content: String) {

        val exportsInfo: JsonObject

        init {
            val matchResult = EXTRACTOR_REGEX.find(content)
                    ?: throw RuntimeException("File didn't match regex: '$content'")

            exportsInfo = Json.parse(matchResult.groupValues[1])
        }

    }
}

private val EXTRACTOR_REGEX = Regex("""exports.info = (\{[\w\W]+})""")