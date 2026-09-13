package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * Who is on each picture of one post: the answer to the one question a reader has to ask for.
 *
 * The same bar as [PostReactions] and in the same order, with the people on it.
 */
@JsExport
@Serializable
class PostReactors {

    var reactions: Array<ReactionWithPeople> = arrayOf()
}
