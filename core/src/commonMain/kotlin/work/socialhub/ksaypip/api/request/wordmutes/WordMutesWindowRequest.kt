package work.socialhub.ksaypip.api.request.wordmutes

import kotlin.js.JsExport

/**
 * Replace the window of an existing word mute, waking a spent one, which is the point.
 */
@JsExport
class WordMutesWindowRequest {
    var wordMuteId: String? = null
    var duration: String? = null
}
