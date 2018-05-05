package me.ailistannen.sharpbotusagecreator.replacement

import me.ailistannen.sharpbotusagecreator.github.GithubEntry
import me.ailistannen.sharpbotusagecreator.parser.Command

class DictionaryTransformer : GithubEntryTransformer {

    override fun canTransform(entry: GithubEntry, content: String): Boolean {
        return entry.path.toString().endsWith("dictionary.js")
    }

    override fun transform(content: String): List<Command> {
        val descriptions = EXPORTS_EXTRACTOR_REGEX.findAll(content)
                .flatMap {
                    DESCRIPTION_EXTRACTOR_REGEX.findAll(it.value)
                }
                .mapNotNull { it.value.replace("info:", "info =") }
                .mapNotNull { parseWithNashornToJson(it) }
                .map { Command(it) }
                .toList()

        if (descriptions.isEmpty()) {
            throw RuntimeException("No description found.")
        }

        return descriptions
    }
}

private val EXPORTS_EXTRACTOR_REGEX = """(?<=module.)(exports = [\w\W]+?]);""".toRegex()

private val DESCRIPTION_EXTRACTOR_REGEX = """(\s+)(info: \{([\w\W]+?)\1})""".toRegex()