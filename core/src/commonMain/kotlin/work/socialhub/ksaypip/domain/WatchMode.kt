package work.socialhub.ksaypip.domain

/**
 * Which of an account's two writing modes a watch keeps. Never both in one row.
 */
object WatchMode {
    const val ANONYMOUS = "anonymous"
    const val IDENTIFIED = "identified"

    val ALL = arrayOf(
        ANONYMOUS,
        IDENTIFIED,
    )
}
