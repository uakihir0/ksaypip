package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A post, as this viewer sees it.
 *
 * `author` is the author this viewer would recognise — one they have written a name for, chosen
 * a mark for, or a friend who has disclosed a face — and null for a stranger on a list row.
 * `authorColor` is what to draw for an author the row does not name; it is null wherever
 * `author` is present or the row is the viewer's own.
 *
 * `readableUntil` is when **this viewer** stops being able to read the post, and null means they
 * do not stop. It is not a property of the post.
 */
@JsExport
@Serializable
class Post {

    var id: String = ""

    var body: String = ""

    var createdAt: String = ""

    var media: Array<Media> = arrayOf()

    /** Oldest picture first. Empty on a post nobody has reacted to. */
    var reactions: Array<PostReaction> = arrayOf()

    var conversations: PostConversations = PostConversations()

    /** Whether the author is asking to be talked to. */
    var wantsTalk: Boolean = false

    var author: Person? = null

    var authorColor: String? = null

    var isMine: Boolean = false

    var readableUntil: String? = null

    /** The post this one quotes, when it is a self-reply. Null on most posts. */
    var replyTo: QuotedPost? = null
}
