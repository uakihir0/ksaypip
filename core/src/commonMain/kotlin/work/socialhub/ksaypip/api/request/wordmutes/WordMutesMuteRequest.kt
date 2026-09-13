package work.socialhub.ksaypip.api.request.wordmutes

import kotlin.js.JsExport

/**
 * Hide the writing that says one word, for a window.
 *
 * The match is a plain substring of the post's body, case-folded; [duration] is one of the keys
 * from `MuteDuration`.
 */
@JsExport
class WordMutesMuteRequest {
    var word: String? = null
    var duration: String? = null
    var idempotencyKey: String? = null
}
