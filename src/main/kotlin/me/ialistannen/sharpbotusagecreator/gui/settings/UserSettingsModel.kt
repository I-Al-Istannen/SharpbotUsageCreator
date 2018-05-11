package me.ialistannen.sharpbotusagecreator.gui.settings

import tornadofx.*

class UserSettingsModel : ItemViewModel<UserSettings>() {
    val includeTocProperty = bind { item?.includeTocProperty }
    val redownloadInTempProperty = bind { item?.redownloadInTempProperty }

    init {
        item = UserSettings()
    }

    override fun toString(): String {
        return "UserSettingsModel(settings=$item)"
    }
}