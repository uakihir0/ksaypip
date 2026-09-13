package work.socialhub.ksaypip.api.response

import kotlin.js.JsExport

/**
 * A successful answer with no resource behind it.
 */
@JsExport
class ResponseUnit {
    var json: String? = null
    var status: Int = 200
}
