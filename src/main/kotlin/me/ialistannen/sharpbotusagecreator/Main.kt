package me.ialistannen.sharpbotusagecreator

fun main(args: Array<String>) {
    if (args.none { it == "--no-gui" }) {
        me.ialistannen.sharpbotusagecreator.gui.main(args)
        return
    }

    if (args.any { it == "--help" }) {
        println("Creates markdown description pages for SharpBot.")
        println()
        println("Options:")
        println("\t--no-redownload-in-temp\t\tClones into this directory and won't delete it" +
                " afterwards. Useful if you are testing and don't want to screw githubs API.")
        println("\t--toc\t\tPrints a table of contents.")
        return
    }

    if ("1.8" in System.getProperty("java.version")) {
        println("Due to changes to Nashorn (only Java 9 and up support ES6), Java 9 or newer" +
                " is required to run this program.")
        System.exit(1)
    }

    val redownloadInTemp = args.none { it == "--no-redownload-in-temp" }

    UsageCreator()
            .getUsageMarkdown(redownloadInTemp, args.any { it == "--toc" })
            .forEach { println(it) }
}