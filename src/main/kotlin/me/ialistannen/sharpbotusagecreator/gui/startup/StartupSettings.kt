package me.ialistannen.sharpbotusagecreator.gui.startup

import javafx.application.Platform
import javafx.geometry.Orientation
import javafx.scene.control.ButtonBar
import javafx.util.Duration
import me.ialistannen.sharpbotusagecreator.gui.markdown.MarkdownView
import me.ialistannen.sharpbotusagecreator.gui.settings.UserSettingsModel
import tornadofx.*
import tornadofx.ViewTransition.Direction
import tornadofx.ViewTransition.Slide

class StartupSettings : View("Settings") {

    private val userSettingsModel: UserSettingsModel by inject()
    private val controller: FetchMarkdownController by inject()

    override val root = borderpane {
        center {
            form {
                fieldset("Generation settings") {
                    labelPosition = Orientation.HORIZONTAL

                    field("Include table of contents") {
                        checkbox {
                            isSelected = true
                            userSettingsModel.includeTocProperty.bind(selectedProperty())
                        }
                    }
                }
                fieldset("Download folder") {
                    labelPosition = Orientation.HORIZONTAL

                    field("Download in temporary folder") {
                        checkbox {
                            isSelected = true
                            userSettingsModel.redownloadInTempProperty.bind(selectedProperty())
                        }
                    }
                }
            }
        }
        bottom {
            buttonbar {
                paddingAll = 10.0

                button("Exit", type = ButtonBar.ButtonData.CANCEL_CLOSE) {
                    action {
                        Platform.exit()
                    }
                }
                button("Accept", type = ButtonBar.ButtonData.OK_DONE) {
                    action {
                        commit()
                    }
                }
            }
        }
    }

    private fun commit() {
        if (userSettingsModel.commit()) {
            root.runAsyncWithOverlay {
                controller.fetchMarkdown(userSettingsModel.userSettings!!)
            } ui {
                val params = mapOf("markdown" to it)
                val markdownView = find(MarkdownView::class, params)

                replaceWith(
                        markdownView,
                        Slide(Duration.seconds(1.0), Direction.DOWN),
                        sizeToScene = true,
                        centerOnScreen = true
                )
            }
        }
    }
}
