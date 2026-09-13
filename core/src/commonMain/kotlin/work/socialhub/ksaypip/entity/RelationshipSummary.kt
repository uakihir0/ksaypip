package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One relationship in the viewer's own list.
 *
 * Neither the note nor the conversations are here: both are read on the relationship's own page.
 * `lastActivityAt` is a time and never a count.
 */
@JsExport
@Serializable
class RelationshipSummary {

    var id: String = ""

    var counterpart: Person = Person()

    var firstInteractionAt: String = ""

    var friendSince: String? = null

    var lastActivityAt: String = ""
}
