package me.ialistannen.sharpbotusagecreator.gui.markdown

import javafx.scene.effect.DropShadow
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.paint.Color
import tornadofx.*
import java.net.URI

class MarkdownStylesheet : Stylesheet() {

    companion object {
        val floatingButton by cssclass("floatingButton")

        private val buttonRadius = 40.px
    }

    init {
        floatingButton {
            backgroundColor += Color.DARKVIOLET
            backgroundRadius += box(50.percent)
            prefWidth = buttonRadius
            prefHeight = buttonRadius
            backgroundImage += URI.create("/images/arrow_upward_white.png")
            backgroundPosition += BackgroundPosition.CENTER
            backgroundRepeat += Pair(BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT)
            effect = DropShadow(10.0, Color.BLACK)

            and(hover) {
                opacity = 0.7
            }
        }
    }
}