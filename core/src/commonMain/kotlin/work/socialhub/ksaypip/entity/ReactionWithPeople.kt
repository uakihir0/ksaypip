package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The same picture with the people on it: what `GET /api/posts/{postId}/reactions` answers with,
 * and the only place in this API where a reaction is attributed.
 *
 * Empty `people` is an answer and a common one: everyone unnameable, the viewer themselves (who
 * is `mine`), and anyone either side has blocked are all in `count` and not in the array.
 */
@JsExport
@Serializable
class ReactionWithPeople {

    var emoji: String = ""

    var count: Int = 0

    var mine: Boolean = false

    var people: Array<Person> = arrayOf()
}
