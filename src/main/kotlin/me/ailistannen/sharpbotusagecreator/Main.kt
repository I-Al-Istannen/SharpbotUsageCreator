package me.ailistannen.sharpbotusagecreator

import me.ailistannen.sharpbotusagecreator.commands.CommandFormatter
import me.ailistannen.sharpbotusagecreator.github.GithubCloningSite
import me.ailistannen.sharpbotusagecreator.parser.CommandParser

fun main(args: Array<String>) {
    if (args.any { it == "--help" }) {
        println("Creates markdown description pages for SharpBot.")
        println()
        println("Options:")
        println("\t--no-redownload-in-temp\t\tClones into this directory and won't delete it" +
                " afterwards. Useful if you are testing and don't want to screw githubs API.")
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

    val formatter = CommandFormatter()
    for (command in commandParser.getCommands().sorted()) {
        println(formatter.format(command))
        println()
    }
}