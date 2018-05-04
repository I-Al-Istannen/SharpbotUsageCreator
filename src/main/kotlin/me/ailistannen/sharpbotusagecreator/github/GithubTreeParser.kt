package me.ailistannen.sharpbotusagecreator.github

import me.ailistannen.sharpbotusagecreator.Json
import java.net.URL
import java.nio.file.Path

class GithubTreeParser(user: String, repo: String, commitSha: String = "master") : Iterable<GithubEntry> {
    private val githubUrl = buildGithubUrl(user, repo, commitSha)

    private var entries: MutableList<GithubEntry>? = null

    override fun iterator(): Iterator<GithubEntry> {
        return getEntries().iterator()
    }

    /**
     * Returns all parsed entries or parses them if not yet done.
     *
     * @return the parsed entries
     */
    fun getEntries(): List<GithubEntry> {
        if (entries == null) {
            return parse()
        }
        return entries!!
    }

    /**
     * Parses the url.
     *
     * @return the parsed entries
     */
    private fun parse(): List<GithubEntry> {
        val json = URL(githubUrl).readText()
        val (entries, truncated) = Json.parse<ApiResponse>(json)

        if (truncated) {
            throw RuntimeException("Data is truncated, I have no idea how to deal with that yet.")
        }

        this.entries = entries.toMutableList()

        return entries
    }
}

data class GithubEntry(val path: Path, val isFile: Boolean)

data class ApiResponse(val tree: List<GithubEntry>, val truncated: Boolean)

/**
 * Builds the URL to fetch information from github, given the user, repo and a commit sha/branch.
 *
 * @param user the user the repo belongs to
 * @param repo the repository to read from
 * @param commitSha the sha of the commit or the branch name
 * @return the full url
 */
private fun buildGithubUrl(user: String, repo: String, commitSha: String = "master"): String {
    return "https://api.github.com/repos/$user/$repo/git/trees/$commitSha?recursive=1"
}