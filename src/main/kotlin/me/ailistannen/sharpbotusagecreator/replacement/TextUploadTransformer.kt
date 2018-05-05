package me.ailistannen.sharpbotusagecreator.replacement

import jdk.nashorn.api.scripting.ScriptObjectMirror
import me.ailistannen.sharpbotusagecreator.Json
import me.ailistannen.sharpbotusagecreator.github.GithubEntry
import me.ailistannen.sharpbotusagecreator.parser.Command

class TextUploadTransformer : GithubEntryTransformer {

    override fun canTransform(entry: GithubEntry, content: String): Boolean {
        return entry.path.toString().endsWith("text-upload.js")
    }

    override fun transform(content: String): List<Command> {
        val modifiedContent = COMMAND_MODIFIER_REGEX.replace(content, "$1")
                .replace("module.", "")

        val descriptions = (parseWithNashorn(modifiedContent) as ScriptObjectMirror)
                .values
                .map { it as ScriptObjectMirror }
                .flatMap { it.values }
                .mapNotNull { Json.toTree(it) }
                .map { Command(it) }
                .toList()

        if (descriptions.isEmpty()) {
            throw RuntimeException("No descriptions found in text upload.")
        }

        return descriptions
    }
}

private val COMMAND_MODIFIER_REGEX = """(\s+)run: [\w\W]+},\n\1""".toRegex()