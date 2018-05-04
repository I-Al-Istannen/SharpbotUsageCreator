package me.ailistannen.sharpbotusagecreator.commands

import com.google.gson.JsonObject
import me.ailistannen.sharpbotusagecreator.markdown.MarkdownContainer
import me.ailistannen.sharpbotusagecreator.parser.CommandParser

class CommandFormatter {

    fun format(command: CommandParser.Command): String {
        val info = command.exportsInfo

        return MarkdownContainer {
            heading {
                content = info["name"].asString.capitalize()
                size = 3
            }
            text {
                content = info["description"].asString
            }
            heading {
                content = "Usage"
                size = 4
            }
            code {
                content = info["usage"].asString
                inline = true
            }
            info.get("examples")?.let {
                heading {
                    content = "Usage examples"
                    size = 5
                }
                unorderedList {
                    for (element in it.asJsonArray) {
                        entry {
                            code {
                                content = element.asString
                                inline = true
                            }
                        }
                    }
                }
            }

            info.get("options")?.let {
                heading {
                    content = "Command options"
                    size = 4
                }
                unorderedList {
                    it.asJsonArray.map { it as JsonObject }.forEach {
                        entry {
                            code {
                                content = it["usage"]?.asString ?: it["name"].asString
                                inline = true
                            }
                            text {
                                content = " — ${it["description"].asString}"
                            }
                        }
                    }
                }
            }
        }.asString()
    }
}