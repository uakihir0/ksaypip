package work.socialhub.ksaypip.api.request.mutes

import kotlin.js.JsExport

/**
 * The people this reader is hiding, newest mute first.
 */
@JsExport
class MutesListRequest {
    var cursor: String? = null
    var limit: Int? = null
}
