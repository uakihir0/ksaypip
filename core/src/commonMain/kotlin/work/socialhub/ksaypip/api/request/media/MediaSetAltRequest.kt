package work.socialhub.ksaypip.api.request.media

import kotlin.js.JsExport

/**
 * What the picture shows, until it is attached to a post.
 *
 * Null, or a string that trims to nothing, both clear it.
 */
@JsExport
class MediaSetAltRequest {
    var mediaId: String? = null
    var alt: String? = null
}
