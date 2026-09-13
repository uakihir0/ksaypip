package work.socialhub.ksaypip.api.request.relationships

import kotlin.js.JsExport

/**
 * Relationship termination: the counterpart's label, conversations, replies and visibility are
 * revoked synchronously, before the response.
 */
@JsExport
class RelationshipsTerminateRequest {
    var relationshipId: String? = null
    var idempotencyKey: String? = null
}
