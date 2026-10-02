package work.socialhub.ksaypip.api.request.watches

import kotlin.js.JsExport

/**
 * Stop keeping an identified persona's writing, addressed by its public handle.
 */
@JsExport
class WatchesUnwatchIdentifiedRequest {
    var handle: String? = null
}
