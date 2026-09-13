package work.socialhub.ksaypip.api.request.relationships

import kotlin.js.JsExport

/**
 * Replace the viewer's local label, note and mark for one counterpart.
 *
 * It is a replacement and not a patch: [label] as null clears the name, and note and mark are
 * written as they are sent.
 */
@JsExport
class RelationshipsSetLabelRequest {
    var relationshipId: String? = null
    var label: String? = null
    var note: String? = null
    var markEmoji: String? = null
    var markColor: String? = null
}
