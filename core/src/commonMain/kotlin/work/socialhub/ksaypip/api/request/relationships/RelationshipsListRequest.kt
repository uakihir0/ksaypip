package work.socialhub.ksaypip.api.request.relationships

import kotlin.js.JsExport

/**
 * The viewer's own relationships, paged; most recently active first.
 */
@JsExport
class RelationshipsListRequest {
    var cursor: String? = null
    var limit: Int? = null
}
