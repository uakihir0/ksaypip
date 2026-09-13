package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport

/**
 * The whole order of kept subjects, sent as one rearrangement.
 *
 * The set has to be exactly the stored one, and the server refuses one that is not with
 * `conflict` / `pinned_subjects_changed` rather than obeying it.
 */
@JsExport
class MeReorderPinnedSubjectsRequest {
    var items: Array<String>? = null
}
