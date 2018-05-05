package me.ailistannen.sharpbotusagecreator.github

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.streams.toList

class GithubCloningSite(
        private val user: String,
        private val repo: String

) : Iterable<GithubEntry> {

    private var directory: Path? = null
    private var entries: List<GithubEntry>? = null

    override fun iterator(): Iterator<GithubEntry> = getEntries().iterator()

    private fun clone(): Path {
        val tempDirectory = Paths.get("/tmp/sharp")

        if (Files.exists(tempDirectory)) {
            return tempDirectory
        }
//        val tempDirectory = Files.createTempDirectory("cloned-sharpbot")
        val process = ProcessBuilder(
                listOf(
                        "git",
                        "clone",
                        buildGithubUrl(user, repo),
                        tempDirectory.toAbsolutePath().toString()
                )
        )
                .inheritIO()
                .start()

        process.waitFor()

        if (process.exitValue() != 0) {
            throw RuntimeException("Cloning finished with an error")
        }

        // clean up after us?
//        Runtime.getRuntime().addShutdownHook(Thread { tempDirectory.deleteDirectory() })

        return tempDirectory
    }

    /**
     * Returns the found entries.
     *
     * @return the found [GithubEntry]s
     */
    fun getEntries(): List<GithubEntry> {
        if (entries != null) {
            return entries!!
        }

        if (directory == null) {
            directory = clone()
        }

        entries = Files.walk(directory)
                .map { GithubEntry(it, Files.isRegularFile(it)) }
                .toList()

        return entries!!
    }
}

/**
 * Builds the URL to fetch information from github, given the user, repo and a commit sha/branch.
 *
 * @param user the user the repo belongs to
 * @param repo the repository to read from
 * @return the full url
 */
private fun buildGithubUrl(user: String, repo: String): String {
    return "https://github.com/$user/$repo"
}

private fun Path.deleteDirectory() {
    Files.walk(this)
            .sorted(Comparator.reverseOrder())
            .forEach { Files.delete(it) }
}

data class GithubEntry(val path: Path, val isFile: Boolean)