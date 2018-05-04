package me.ailistannen.sharpbotusagecreator.commands

import me.ailistannen.sharpbotusagecreator.github.GithubEntry

fun isCommandFile(t: GithubEntry): Boolean {
    return t.path.startsWith("src/commands")
            && t.path.endsWith(".js")
            && t.type == "blob"
}