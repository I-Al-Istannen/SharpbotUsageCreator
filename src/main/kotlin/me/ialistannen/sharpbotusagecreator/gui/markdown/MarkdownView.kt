package me.ialistannen.sharpbotusagecreator.gui.markdown

import javafx.geometry.Pos
import javafx.scene.web.WebView
import tornadofx.*

class MarkdownView : View("Markdown display") {
    private var webView: WebView by singleAssign()
    private val controller: MarkdownController by inject()
    private val markdown: String by param("")

    override val root = borderpane {
        center = stackpane {
            webView = webview {
                engine.isJavaScriptEnabled = true
            }
            button {
                addClass(MarkdownStylesheet.floatingButton)
                stackpaneConstraints {
                    alignment = Pos.BOTTOM_RIGHT
                    margin = insets(40)
                }

                action { scrollToTop() }
            }

            addStylesheet(MarkdownStylesheet::class)
        }
    }

    init {
        setMarkdown(markdown)
    }

    /**
     * Sets the markdown to display.
     *
     * @param markdown the markdown to display
     */
    private fun setMarkdown(markdown: String) {
        runAsync {
            controller.markdownToHtml(markdown)
        } ui { url ->
            webView.engine.load(url)
        }
    }

    /**
     * Scrolls to the top of the page.
     */
    private fun scrollToTop() {
        webView.engine.executeScript("window.scrollTo(0,0);")
    }
}