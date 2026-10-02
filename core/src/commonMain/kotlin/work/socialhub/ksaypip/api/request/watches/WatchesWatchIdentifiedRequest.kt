package work.socialhub.ksaypip.api.request.watches

import kotlin.js.JsExport

/**
 * Keep this identified persona's writing in the connections timeline, addressed by the public
 * handle the caller is looking at. Idempotent, and never touches the anonymous row.
 */
@JsExport
class WatchesWatchIdentifiedRequest {
    var handle: String? = null
    var idempotencyKey: String? = null
}
