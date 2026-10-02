package work.socialhub.ksaypip.api.request.users

import kotlin.js.JsExport

/**
 * Replace the viewer's local label, note and mark for one person, addressed by the identity token
 * the caller holds rather than by a relationship. No conversation is required.
 *
 * It is a replacement and not a patch: [label] as null clears the name, and note and mark are
 * written as they are sent. [markColors] is the gradient's two ends, top left then bottom right,
 * or null for no gradient.
 */
@JsExport
class UsersSetLabelRequest {
    var identityToken: String? = null
    var label: String? = null
    var note: String? = null
    var markEmoji: String? = null
    var markColors: Array<String>? = null
}
