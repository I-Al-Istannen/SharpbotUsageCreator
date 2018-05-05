package me.ailistannen.sharpbotusagecreator.parser

import com.google.gson.JsonObject
import me.ailistannen.sharpbotusagecreator.commands.isCommandFile
import me.ailistannen.sharpbotusagecreator.github.FileFetcher
import me.ailistannen.sharpbotusagecreator.github.GithubEntry
import me.ailistannen.sharpbotusagecreator.replacement.*

class CommandParser(private val entries: List<GithubEntry>) {

    private val fileFetcher: FileFetcher = FileFetcher()
    val transformers: MutableList<GithubEntryTransformer> = arrayListOf()
    private var commands: List<Command>? = null

    init {
        transformers.add(StatusTransformer())
        transformers.add(DictionaryTransformer())
        transformers.add(ArrayExportMakeCommandTransformer())
        transformers.add(DefaultTransformer())
    }

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
        commands = entries
                .filter { isCommandFile(it) }
                .mapNotNull {
                    try {
                        val content = fileFetcher.getContent(it)
                        val transformer = transformers.find { t -> t.canTransform(it, content) }
                                ?: throw RuntimeException("No transformer found")

                        transformer.transform(content)
                    } catch (e: Throwable) {
                        System.err.println("Error creating command for '$it' --- '${e.message}''")
                        null
                    }
                }
                .flatten()

        return commands!!
    }

}

data class Command(val exportsInfo: JsonObject)
