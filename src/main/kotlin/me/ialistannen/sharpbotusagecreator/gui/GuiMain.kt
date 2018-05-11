package me.ialistannen.sharpbotusagecreator.gui

import javafx.application.Application
import tornadofx.*

class CommandUsageCreator : App(MainView::class)

fun main(args: Array<String>) {
    Application.launch(CommandUsageCreator::class.java, *args)
}