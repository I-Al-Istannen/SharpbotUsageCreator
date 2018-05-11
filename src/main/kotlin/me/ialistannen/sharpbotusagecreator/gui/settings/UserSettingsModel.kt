package me.ialistannen.sharpbotusagecreator.gui.settings

import tornadofx.*

class UserSettingsModel(var userSettings: UserSettings? = UserSettings()) : ViewModel() {
    val includeTocProperty = bind { userSettings?.includeTocProperty }
    val redownloadInTempProperty = bind { userSettings?.redownloadInTempProperty }

    override fun toString(): String {
        return "UserSettingsModel(settings=$userSettings)"
    }
}