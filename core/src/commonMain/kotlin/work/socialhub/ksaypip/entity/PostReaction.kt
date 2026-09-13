package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One picture on a post, how many people put it there, and whether the viewer is one of them.
 *
 * A count, everywhere a post is carried. Who the people are is a second address,
 * `GET /api/posts/{postId}/reactions`.
 */
@JsExport
@Serializable
class PostReaction {

    var emoji: String = ""

    var count: Int = 0

    /** Whether the viewer's own reaction is among the count. */
    var mine: Boolean = false
}
