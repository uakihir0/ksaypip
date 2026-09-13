package work.socialhub.ksaypip.domain

/**
 * The widgets a reader may arrange down the right-hand column.
 *
 * An id a client does not recognise is one this build did not ship, and is ignored rather than
 * drawn as an empty card.
 */
object AsideWidget {
    const val SEARCH = "search"
    const val TRENDS = "trends"
    const val NEWS = "news"
    const val PROMISE = "promise"

    val ALL = arrayOf(SEARCH, TRENDS, NEWS, PROMISE)
}
