package work.socialhub.ksaypip.api.request.wordmutes

import kotlin.js.JsExport

/**
 * Forget one word mute. A real delete, idempotent.
 */
@JsExport
class WordMutesForgetRequest {
    var wordMuteId: String? = null
    var idempotencyKey: String? = null
}
