package me.ialistannen.sharpbotusagecreator.gui.settings

import javafx.beans.property.SimpleBooleanProperty

class UserSettings {
    var includeTocProperty = SimpleBooleanProperty()
    var redownloadInTempProperty = SimpleBooleanProperty()

    override fun toString(): String {
        return "Settings[ toc=${includeTocProperty.value}, inTemp=${redownloadInTempProperty.value} ]"
    }
}