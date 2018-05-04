package me.ailistannen.sharpbotusagecreator

import com.google.gson.JsonObject
import me.ailistannen.sharpbotusagecreator.commands.CommandFormatter
import me.ailistannen.sharpbotusagecreator.github.GithubTreeParser
import me.ailistannen.sharpbotusagecreator.parser.CommandParser
import java.net.URL

fun main(args: Array<String>) {

    val url = URL("https://itunes.apple.com/WebObjects/MZStoreServices.woa/ws/genres?id=26")

    val conn = url.openConnection().apply {
        addRequestProperty("User-Agent", "Mozilla/5.0 (compatible)")
    }

    val json1 = conn.getInputStream().bufferedReader().readText()

    val jsonObject = Json.parse<JsonObject>(json1).getAsJsonObject("26")
    val subgenres = jsonObject.getAsJsonObject("subgenres")

    for (entry in subgenres.entrySet()) {
        println(entry.key + " -> " + entry.value.asJsonObject["name"])
    }

    if("".isEmpty()) {
        return
    }

    val githubTreeParser = GithubTreeParser("RayzrDev", "SharpBot")
    val commandParser = CommandParser(githubTreeParser.getEntries())
    val json = """
        exports.info = {
    name: 'embed',
    usage: 'embed [text]',
    description: 'Sends a message via embeds',
    examples: [
        'embed Hello world!',
        'embed -c #ff0000 Red is my favorite color :heart:',
        'embed -r -t "Welcome to my server!" I hope you enjoy your time here.'
    ],
    options: [
        {
            name: '-f',
            description: 'Shows the footer'
        },
        {
            name: '-ft',
            usage: '-ft <text>',
            description: 'Sets the footer text (use quotes for multiple words)'
        },
        {
            name: '-d',
            usage: '-d',
            description: 'Enables the timestamp (date) in the footer'
        },
        {
            name: '-t',
            usage: '-t <text>',
            description: 'Sets the embed title (use quotes for multiple words)'
        },
        {
            name: '-r',
            description: 'Uses your role color for the embed color'
        },
        {
            name: '-c',
            usage: '-c <color>',
            description: 'Sets a hex color for the embed in the format of `#RRGGBB`'
        },
        {
            name: '-i',
            usage: '-i <url>',
            description: 'Sets an image for the embed'
        },
        {
            name: '-a',
            usage: '-a <name>',
            description: 'Sets the author of the embed'
        },
        {
            name: '-th',
            usage: '-th <url>',
            description: 'Sets a thumbnail for the embed'
        }
    ]
}
        """
//    val command = CommandParser.Command(json)

    val formatter = CommandFormatter()
//    println(CommandFormatter().format(command))
    for (command in commandParser.getCommands()) {
        println(formatter.format(command))
        println()
        println()
    }
}