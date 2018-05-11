package me.ialistannen.sharpbotusagecreator.gui.startup

import me.ialistannen.sharpbotusagecreator.UsageCreator
import me.ialistannen.sharpbotusagecreator.gui.settings.UserSettings
import tornadofx.*

class FetchMarkdownController : Controller() {

    /**
     * Fetches the markdown.
     *
     * @param userSettings the settings to use
     *
     * @return the markdown string
     */
    fun fetchMarkdown(userSettings: UserSettings): String {
        return UsageCreator().getUsageMarkdown(
                userSettings.redownloadInTempProperty.value,
                userSettings.includeTocProperty.value
        ).joinToString("\n")
    }
}