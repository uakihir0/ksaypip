package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The whole bar under one reply: what the two reply reaction writes answer with.
 *
 * The same shape as [PostReactions] and deliberately not the same name. There is no attributed
 * sibling — with only the two participants able to react and the viewer one of them, the counts
 * already say who.
 */
@JsExport
@Serializable
class ReplyReactions {

    var reactions: Array<PostReaction> = arrayOf()
}
