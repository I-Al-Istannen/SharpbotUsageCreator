package me.ialistannen.sharpbotusagecreator.gui.view

import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import javafx.geometry.Pos
import javafx.scene.effect.DropShadow
import javafx.scene.layout.BackgroundPosition
import javafx.scene.layout.BackgroundRepeat
import javafx.scene.paint.Color
import javafx.scene.web.WebView
import me.ialistannen.sharpbotusagecreator.gui.markdown.HeadingIdExtension
import tornadofx.*
import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class MarkdownView : View("Markdown") {
    private var webView: WebView by singleAssign()
    private val controller: MarkdownController by inject()

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

    /**
     * Sets the markdown to display.
     *
     * @param markdown the markdown to display
     */
    fun setMarkdown(markdown: String) {
        runAsync {
            controller.markdownToHtml(markdown)
        } ui {
            webView.engine.load(it)
        }
    }

    /**
     * Scrolls to the top of the page.
     */
    private fun scrollToTop() {
        webView.engine.executeScript("window.scrollTo(0,0);")
    }
}

class MarkdownController : Controller() {

    companion object {

        @JvmStatic
        private val options: MutableDataSet by lazy {
            MutableDataSet().apply {
                set(Parser.EXTENSIONS, listOf(
                        TablesExtension.create(),
                        StrikethroughExtension.create(),
                        HeadingIdExtension()
                ))
            }
        }
        @JvmStatic
        private val parser: Parser = Parser.builder(options).build()

        @JvmStatic
        private val htmlRenderer: HtmlRenderer = HtmlRenderer.builder(options).build()
    }

    /**
     * Displays the given markdown string.
     *
     * @param markdown the markdown to display
     *
     * @return the url to the file to display
     */
    fun markdownToHtml(markdown: String): String {
        val document = parser.parse(markdown)
        val bodyHtml = htmlRenderer.render(document)

        val fullHtml = generateHtmlFromTemplate(bodyHtml)

        val tempFile = Files.createTempFile("sharpbot_usage", ".html")
        tempFile.toFile().deleteOnExit()
        Files.write(
                tempFile,
                listOf(fullHtml),
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        )

        return tempFile.toUri().toString()
    }

    private fun generateHtmlFromTemplate(body: String): String {
        val githubMarkdown = resources.url("/github-markdown.css")
        val bodyStyle = resources.url("/markdown-body-style.css")
        return """
            |<head>
            |  <meta charset="UTF-8">
            |  <link rel="stylesheet" type="text/css" href="$githubMarkdown">
            |  <link rel="stylesheet" type="text/css" href="$bodyStyle">
            |</head>
            |<body>
            |  <div class="markdown-body">
            |${body.prependIndent(" ".repeat(4))}
            |  </div>
            |</body>
            """.trimMargin()
    }
}

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