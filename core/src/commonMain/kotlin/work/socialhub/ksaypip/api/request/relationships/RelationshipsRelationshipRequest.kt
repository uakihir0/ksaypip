package work.socialhub.ksaypip.api.request.relationships

import kotlin.js.JsExport

/**
 * A relationship page: label, note, mark, history and conversations.
 */
@JsExport
class RelationshipsRelationshipRequest {
    var relationshipId: String? = null
}
