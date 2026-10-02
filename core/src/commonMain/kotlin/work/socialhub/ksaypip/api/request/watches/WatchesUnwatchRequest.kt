package work.socialhub.ksaypip.api.request.watches

import kotlin.js.JsExport

/**
 * Stop keeping this person's anonymous writing. Letting go of somebody not watched is a success.
 */
@JsExport
class WatchesUnwatchRequest {
    var identityToken: String? = null
}
