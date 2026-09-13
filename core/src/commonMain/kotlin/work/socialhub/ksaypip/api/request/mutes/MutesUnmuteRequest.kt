package work.socialhub.ksaypip.api.request.mutes

import kotlin.js.JsExport

/**
 * Stop hiding them. A real delete, idempotent.
 */
@JsExport
class MutesUnmuteRequest {
    var identityToken: String? = null
}
