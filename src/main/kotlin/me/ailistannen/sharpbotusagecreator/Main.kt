package me.ailistannen.sharpbotusagecreator

import me.ailistannen.sharpbotusagecreator.commands.CommandFormatter
import me.ailistannen.sharpbotusagecreator.github.GithubCloningSite
import me.ailistannen.sharpbotusagecreator.parser.CommandParser

fun main(args: Array<String>) {
    val githubTreeParser = GithubCloningSite("RayzrDev", "SharpBot")
    val commandParser = CommandParser(githubTreeParser.getEntries())

    val formatter = CommandFormatter()
    for (command in commandParser.getCommands()) {
        println(formatter.format(command))
        println()
        println()
    }
}