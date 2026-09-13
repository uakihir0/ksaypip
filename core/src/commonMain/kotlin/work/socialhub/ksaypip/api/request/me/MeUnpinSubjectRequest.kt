package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport

/**
 * Let one subject go; the row closes up behind it. Idempotent; the whole row answers.
 */
@JsExport
class MeUnpinSubjectRequest {
    var tag: String? = null
}
