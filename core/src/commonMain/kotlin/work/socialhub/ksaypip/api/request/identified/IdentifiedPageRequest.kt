package work.socialhub.ksaypip.api.request.identified

import kotlin.js.JsExport

/**
 * The public page of an identified persona, and a page of its posts at any age.
 */
@JsExport
class IdentifiedPageRequest {
    var handle: String? = null
    var cursor: String? = null
    var limit: Int? = null
}
