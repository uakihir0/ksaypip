package work.socialhub.ksaypip.api.request.mutes

import kotlin.js.JsExport

/**
 * Hide somebody from your own reading, for a window.
 *
 * Exactly one target is named: an [identity] the screen holds, or a [postId] whose author the
 * server reads without telling the client who they are. [duration] is one of the keys from
 * `MuteDuration`.
 */
@JsExport
class MutesMuteRequest {
    var identity: String? = null
    var postId: String? = null
    var duration: String? = null
    var idempotencyKey: String? = null
}
