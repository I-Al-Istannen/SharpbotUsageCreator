package me.ailistannen.sharpbotusagecreator.markdown

abstract class MarkdownNode {

    abstract fun asString(): String
}

class MarkdownContainer(init: (MarkdownContainer).() -> Unit) : MarkdownNode() {

    private val children: MutableList<MarkdownNode> = arrayListOf()

    init {
        this.init()
    }

    infix fun text(init: (StringNode).() -> Unit) {
        addNode(init, StringNode())
    }

    fun heading(init: (HeadingNode).() -> Unit) {
        addNode(init, HeadingNode())
    }

    fun code(init: (CodeNode).() -> Unit) {
        addNode(init, CodeNode())
    }

    fun unorderedList(init: (UnorderedListNode).() -> Unit) {
        addNode(init, UnorderedListNode())
    }

    private fun <T : MarkdownNode> addNode(init: (T).() -> Unit, element: T) {
        element.init()
        children.add(element)
    }

    override fun asString(): String {
        val result = StringBuilder()

        for (child in children) {
            result
                    .append(child.asString())
                    .append("\n")
        }

        return result.toString()
    }

}

class StringNode(var content: String = "") : MarkdownNode() {
    override fun asString(): String = content
}

class HeadingNode(var content: String = "", var size: Int = 1) : MarkdownNode() {
    override fun asString(): String = "\n${"#".repeat(size)} $content \n"
}

class CodeNode(var content: String = "", var language: String = "") : MarkdownNode() {

    var inline = false

    override fun asString(): String {
        if (inline) {
            return "` $content `"
        }
        return """
            |```$language
            |$content
            |```
            |""".trimMargin()
    }
}

class UnorderedListNode(private var entries: MutableList<MarkdownNode> = arrayListOf()) : MarkdownNode() {

    fun entry(init: (MarkdownContainer).() -> Unit) {
        val container = MarkdownContainer(init)
        entries.add(container)
    }

    override fun asString(): String {
        val result = StringBuilder()

        for (entry in entries) {
            val lines = entry.asString().lines()
            val indentedContent = lines
                    .drop(1)
                    .joinToString("\n") { "  $it" }
            val entryString = lines.first() + "\n" + indentedContent
            result
                    .append("* $entryString")
                    .append("\n")
        }

        return result.toString()
    }
}