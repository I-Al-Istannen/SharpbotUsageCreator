package me.ialistannen.sharpbotusagecreator.replacement

import me.ialistannen.sharpbotusagecreator.github.GithubEntry
import me.ialistannen.sharpbotusagecreator.parser.Command

class StatusTransformer : GithubEntryTransformer {

    override fun canTransform(entry: GithubEntry, content: String) =
            entry.path.toString().endsWith("status.js")

    override fun transform(content: String): List<Command> {
        val evalString = content
                .replace(REPLACEMENT_REGEX, "")
                .replace("exports.", "")

        val jsonObject = parseWithNashornToJson(evalString) ?: return emptyList()
        return listOf(Command(jsonObject))
    }
}

private val REPLACEMENT_REGEX = "exports.run = [\\w\\W]+?};".toRegex()