package work.socialhub.ksaypip.api.request.watches

import kotlin.js.JsExport

/**
 * Keep this person's anonymous writing in the connections timeline, addressed by the identity
 * token the caller holds. Idempotent.
 */
@JsExport
class WatchesWatchRequest {
    var identity: String? = null
    var idempotencyKey: String? = null
}
