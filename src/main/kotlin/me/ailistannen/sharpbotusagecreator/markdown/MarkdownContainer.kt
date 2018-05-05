package me.ailistannen.sharpbotusagecreator.markdown

abstract class MarkdownNode {

    abstract fun asString(): String
}

class MarkdownContainer(
        var toc: MarkdownToc = MarkdownToc { },
        init: (MarkdownContainer).() -> Unit

) : MarkdownNode() {

    private val children: MutableList<MarkdownNode> = arrayListOf()

    init {
        this.init()
    }

    infix fun text(init: (StringNode).() -> Unit) {
        addNode(init, StringNode())
    }

    fun heading(inToc: Boolean = false, init: (HeadingNode).() -> Unit) {
        val headingNode = HeadingNode()
        addNode(init, headingNode)
        if (inToc) {
            toc.addEntry(headingNode)
        }
    }

    fun code(init: (CodeNode).() -> Unit) {
        addNode(init, CodeNode())
    }

    fun unorderedList(init: (UnorderedListNode).() -> Unit) {
        addNode(init, UnorderedListNode(this))
    }

    fun orderedList(init: (OrderedListNode).() -> Unit) {
        addNode(init, OrderedListNode(this))
    }

    fun link(init: (LinkNode).() -> Unit) {
        addNode(init, LinkNode())
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
        }

        return result.toString()
    }

}

class StringNode(var content: String = "") : MarkdownNode() {

    var paragraph: Boolean = false;

    override fun asString(): String = if (paragraph) {
        "\n\n$content\n\n"
    } else {
        content
    }
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

class UnorderedListNode(parent: MarkdownContainer,
                        entries: MutableList<MarkdownNode> = arrayListOf()
) : ListNode(parent, { "*" }, entries)

class OrderedListNode(parent: MarkdownContainer,
                      entries: MutableList<MarkdownNode> = arrayListOf()
) : ListNode(parent, { it.inc().toString() + "." }, entries)

abstract class ListNode(private var parent: MarkdownContainer,
                        private var prefix: (Int) -> String,
                        private var entries: MutableList<MarkdownNode> = arrayListOf()
) : MarkdownNode() {

    var compact: Boolean = false

    fun entry(init: (MarkdownContainer).() -> Unit) {
        val container = MarkdownContainer(parent.toc, init)
        entries.add(container)
    }

    override fun asString(): String {
        val result = StringBuilder()

        for ((index, entry) in entries.withIndex()) {
            val lines = entry.asString().lines()
            val indentedContent = lines
                    .drop(1)
                    .joinToString("\n") { "  $it" }
            val entryString = lines.first() + "\n" + indentedContent
            result.append("${prefix.invoke(index)} $entryString")

            if (!compact) {
                result.append("\n")
            }
        }

        return result.toString()
    }
}

class LinkNode(var content: String = "", var url: String = "") : MarkdownNode() {

    override fun asString(): String {
        return "[$content]($url)"
    }
}

class MarkdownToc(private val formatter: MarkdownContainer.(List<HeadingNode>) -> Unit) {
    private val nodes: MutableList<HeadingNode> = arrayListOf()

    fun addEntry(headingNode: HeadingNode) {
        nodes.add(headingNode)
    }

    fun asString(): String {
        val root = MarkdownContainer(this) {}
        root.formatter(nodes)
        return root.asString()
    }
}