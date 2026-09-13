package work.socialhub.ksaypip.domain

/**
 * The OAuth scopes this deployment issues.
 *
 * `read` and `write` are decisions about the reader's own account, and `offline_access` is not a
 * permission at all: it is what makes a refresh token issuable.
 */
object OAuthScope {
    const val READ = "read"
    const val WRITE = "write"
    const val OFFLINE_ACCESS = "offline_access"

    val ALL = arrayOf(READ, WRITE, OFFLINE_ACCESS)
}
