package me.ialistannen.sharpbotusagecreator.github

class FileFetcher {

    /**
     * Returns the content for a file from github.
     *
     * @param entry the entry to get it for
     * @return the read content as a String
     */
    fun getContent(entry: GithubEntry): String {
        return entry.path.toFile().readText()
    }
}