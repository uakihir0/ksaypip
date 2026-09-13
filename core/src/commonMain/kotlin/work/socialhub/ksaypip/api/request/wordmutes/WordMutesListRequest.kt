package work.socialhub.ksaypip.api.request.wordmutes

import kotlin.js.JsExport

/**
 * The words this reader is hiding, spent ones included.
 */
@JsExport
class WordMutesListRequest {
    var cursor: String? = null
    var limit: Int? = null
}
