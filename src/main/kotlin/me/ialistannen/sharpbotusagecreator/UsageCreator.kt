package me.ialistannen.sharpbotusagecreator

import me.ialistannen.sharpbotusagecreator.commands.CommandFormatter
import me.ialistannen.sharpbotusagecreator.github.GithubCloningSite
import me.ialistannen.sharpbotusagecreator.markdown.MarkdownToc
import me.ialistannen.sharpbotusagecreator.parser.CommandParser

class UsageCreator {

    /**
     * Returns the markdown for the usage using the given options.
     *
     * @param redownloadInTemp if true it will download into a temp folder, if false it will
     * download into the current folder.
     * @param withToc whether to include a table of contents
     * @return the generated markdown
     */
    fun getUsageMarkdown(redownloadInTemp: Boolean = true,
                         withToc: Boolean = true
    ): MutableList<String> {

        if ("1.8" in System.getProperty("java.version")) {
            println("Due to changes to Nashorn (only Java 9 and up support ES6), Java 9 or newer" +
                    " is required to run this program.")
            System.exit(1)
        }

        val githubTreeParser = GithubCloningSite("RayzrDev", "SharpBot", redownloadInTemp)
        val commandParser = CommandParser(githubTreeParser.getEntries())

        val toc = MarkdownToc {
            heading {
                content = "Table of contents"
                size = 2
            }
            orderedList {
                compact = true
                for (headingNode in it) {
                    entry {
                        link {
                            content = headingNode.content
                            url = "#${content.toLowerCase().replace(" ", "-")}"
                        }
                    }
                }
            }
            text { content = "<br><br>" }
        }

        val formatter = CommandFormatter(toc)
        val rendered: MutableList<String> = arrayListOf()
        for (command in commandParser.getCommands().sorted()) {
            rendered.add(formatter.format(command))
        }

        if (withToc) {
            rendered.add(0, toc.asString())
        }

        return rendered
    }
}