package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The whole bar under one post: what the two reaction writes answer with.
 *
 * Counted, like every other path that carries a bar.
 */
@JsExport
@Serializable
class PostReactions {

    var reactions: Array<PostReaction> = arrayOf()
}
