package work.socialhub.ksaypip.api.request.media

import kotlin.js.JsExport

/**
 * Media bytes, re-checking visibility on every fetch.
 *
 * `variant` is `full` or `thumb`, both of which are checked identically — it chooses bytes and
 * nothing else.
 */
@JsExport
class MediaBytesRequest {
    var mediaId: String? = null
    var variant: String? = null
}
