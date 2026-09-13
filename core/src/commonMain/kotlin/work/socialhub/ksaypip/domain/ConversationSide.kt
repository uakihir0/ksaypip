package work.socialhub.ksaypip.domain

/**
 * Conversation-local speaker marker.
 *
 * `a` is the post's author, `b` is whoever started the conversation. A seat rather than a
 * person: a stable marker for the speaker of a quoted line would be an author identifier on a
 * timeline row, which is exactly what this product refuses to hand out.
 */
object ConversationSide {
    const val A = "a"
    const val B = "b"
}
