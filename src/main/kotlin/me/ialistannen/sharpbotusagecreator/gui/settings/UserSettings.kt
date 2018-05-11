package me.ialistannen.sharpbotusagecreator.gui.settings

import javafx.beans.property.SimpleBooleanProperty

class UserSettings {
    val includeTocProperty = SimpleBooleanProperty(true)
    val redownloadInTempProperty = SimpleBooleanProperty(true)

    override fun toString(): String {
        return "UserSettings(includeTocProperty=$includeTocProperty, redownloadInTempProperty=$redownloadInTempProperty)"
    }
}