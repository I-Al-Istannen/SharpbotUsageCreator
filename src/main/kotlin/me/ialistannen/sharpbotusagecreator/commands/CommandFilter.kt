package me.ialistannen.sharpbotusagecreator.commands

import me.ialistannen.sharpbotusagecreator.github.GithubEntry

fun isCommandFile(t: GithubEntry): Boolean {
    return t.path.toString().contains("src/commands")
            && t.path.toString().endsWith(".js")
            && t.isFile
            && !t.path.toString().endsWith("_base.js")
}