package me.ialistannen.sharpbotusagecreator.gui

import javafx.application.Application
import me.ialistannen.sharpbotusagecreator.gui.startup.StartupSettings
import tornadofx.*

class CommandUsageCreator : App(StartupSettings::class)

fun main(args: Array<String>) {
    Application.launch(CommandUsageCreator::class.java, *args)
}