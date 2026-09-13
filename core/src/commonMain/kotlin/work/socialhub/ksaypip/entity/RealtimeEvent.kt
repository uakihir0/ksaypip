package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One frame of the Global Room, whichever kind it is.
 *
 * The **entire** payload is a type and a post ID: no author, no body, no time, no colour. What a
 * reader is shown of a live post — its pictures, its reactions, its author where the viewer would
 * recognise them — is read back through the feed, under the same visibility rules as any other
 * row. No author-derived field of any kind, including per-author sequence numbers, is in it.
 */
@JsExport
@Serializable
class RealtimeEvent {

    /** `post.created` or `post.deleted`; a client ignores a type it does not know. */
    var type: String = ""

    var postId: String = ""
}
