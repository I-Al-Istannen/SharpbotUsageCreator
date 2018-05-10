package me.ialistannen.sharpbotusagecreator

import me.ialistannen.sharpbotusagecreator.commands.CommandFormatter
import me.ialistannen.sharpbotusagecreator.github.GithubCloningSite
import me.ialistannen.sharpbotusagecreator.markdown.MarkdownToc
import me.ialistannen.sharpbotusagecreator.parser.CommandParser

fun main(args: Array<String>) {
    if (args.any { it == "--help" }) {
        println("Creates markdown description pages for SharpBot.")
        println()
        println("Options:")
        println("\t--no-redownload-in-temp\t\tClones into this directory and won't delete it" +
                " afterwards. Useful if you are testing and don't want to screw githubs API.")
        println("\t--toc\t\tPrints a table of contents.")
        return
    }

    if ("1.8" in System.getProperty("java.version")) {
        println("Due to changes to Nashorn (only Java 9 and up support ES6), Java 9 or newer" +
                " is required to run this program.")
        System.exit(1)
    }

    val redownloadInTemp = args.none { it == "--no-redownload-in-temp" }

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

    if (args.any { it == "--toc" }) {
        rendered.add(0, toc.asString())
    }

    rendered.forEach { println(it) }
}