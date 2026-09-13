package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A page of the viewer's own relationships, most recently active first.
 */
@JsExport
@Serializable
class RelationshipList {

    var items: Array<RelationshipSummary> = arrayOf()

    var nextCursor: String? = null
}
