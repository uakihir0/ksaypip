package work.socialhub.ksaypip.domain

/**
 * The event kinds the Global Room sends.
 *
 * An event is a notification and not a post: it says that there is something to read and which
 * post it is, and the post itself is read back over HTTP. Events are additive, and a client
 * ignores a type it does not recognise.
 */
object RealtimeEventType {
    const val POST_CREATED = "post.created"
    const val POST_DELETED = "post.deleted"

    val ALL = arrayOf(POST_CREATED, POST_DELETED)
}
