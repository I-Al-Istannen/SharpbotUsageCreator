package me.ialistannen.sharpbotusagecreator.replacement

import me.ialistannen.sharpbotusagecreator.github.GithubEntry
import me.ialistannen.sharpbotusagecreator.parser.Command

class DefaultTransformer : GithubEntryTransformer {

    override fun canTransform(entry: GithubEntry, content: String): Boolean {
        return entry.isFile && "makeCommand" !in content
    }

    override fun transform(content: String): List<Command> {
        val exportsSection = extractExportsSection(content) ?: return emptyList()

        val jsonObject = parseWithNashornToJson(exportsSection) ?: return emptyList()
        return listOf(Command(jsonObject))
    }
}