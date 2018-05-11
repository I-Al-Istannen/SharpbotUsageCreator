package me.ialistannen.sharpbotusagecreator.gui.markdown

import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.options.MutableDataSet
import tornadofx.*
import java.nio.file.Files
import java.nio.file.StandardOpenOption

class MarkdownController : Controller() {

    companion object {

        @JvmStatic
        private val options: MutableDataSet by lazy {
            MutableDataSet().apply {
                set(Parser.EXTENSIONS, listOf(
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
        val githubMarkdown = resources.url("/css/github-markdown.css")
        val bodyStyle = resources.url("/css/markdown-body-style.css")
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