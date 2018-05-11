package me.ialistannen.sharpbotusagecreator.gui

import me.ialistannen.sharpbotusagecreator.UsageCreator
import me.ialistannen.sharpbotusagecreator.gui.markdown.MarkdownView
import me.ialistannen.sharpbotusagecreator.gui.settings.UserSettingsModel
import tornadofx.*


class MainView : View("Main view") {

    private val userSettingsModel: UserSettingsModel by inject()
    private var markdownView: MarkdownView by singleAssign()

    override val root = borderpane {
        markdownView = find(MarkdownView::class)
        center = markdownView.root
    }

    init {
        markdownView.root.runAsyncWithOverlay {
            UsageCreator().getUsageMarkdown(
                    userSettingsModel.redownloadInTempProperty.value,
                    userSettingsModel.includeTocProperty.value
            )
        } ui {
            markdownView.setMarkdown(it.joinToString("\n"))
        }
    }
}
