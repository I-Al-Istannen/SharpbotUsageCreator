package me.ialistannen.sharpbotusagecreator.gui.view

import javafx.geometry.Orientation
import javafx.scene.control.ButtonBar
import javafx.scene.control.ToggleGroup
import me.ialistannen.sharpbotusagecreator.gui.settings.UserSettingsModel
import tornadofx.*

class StartupDialog : View("Startup dialog") {
    private val userSettingsModel: UserSettingsModel by inject()

    override val root = borderpane {
        center = form {
            fieldset("Program settings", labelPosition = Orientation.VERTICAL) {
                spacing = 20.0

                field("Generate table of contents?") {
                    checkbox {
                        isSelected = true
                        userSettingsModel.includeTocProperty.bind(selectedProperty())
                    }
                }
                field("Download SharpBot in a temporary folder or this directory?") {
                    val group = ToggleGroup()
                    radiobutton("This directory", group)
                    val tempDir = radiobutton("Temp directory", group) { isSelected = true }

                    userSettingsModel.redownloadInTempProperty.bind(tempDir.selectedProperty())
                }
            }
        }
        bottom {
            paddingAll = 10.0
            buttonbar {
                button("Exit", type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                    action {
                        System.exit(0)
                    }
                }
                button("Accept", type = ButtonBar.ButtonData.OK_DONE) {
                    action {
                        userSettingsModel.commit()
                        currentStage?.hide()
                    }
                }
            }
        }
    }
}