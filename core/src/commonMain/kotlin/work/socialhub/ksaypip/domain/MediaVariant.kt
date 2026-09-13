package work.socialhub.ksaypip.domain

/**
 * Which stored size to serve. Both are checked identically, so this chooses bytes and nothing
 * else.
 */
object MediaVariant {
    const val FULL = "full"
    const val THUMB = "thumb"

    val ALL = arrayOf(FULL, THUMB)
}
