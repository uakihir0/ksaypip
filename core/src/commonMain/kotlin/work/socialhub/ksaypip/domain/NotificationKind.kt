package work.socialhub.ksaypip.domain

/**
 * The closed set of notification lines. A client branches on the kind.
 */
object NotificationKind {
    const val POST_REACTION = "post.reaction"
    const val REPLY_REACTION = "reply.reaction"
    const val CONVERSATION_REPLY = "conversation.reply"

    val ALL = arrayOf(
        POST_REACTION,
        REPLY_REACTION,
        CONVERSATION_REPLY,
    )
}
