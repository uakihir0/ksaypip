package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport

/**
 * Keep one subject in the row over the timeline. Idempotent; the whole row answers.
 */
@JsExport
class MePinSubjectRequest {
    var tag: String? = null
}
