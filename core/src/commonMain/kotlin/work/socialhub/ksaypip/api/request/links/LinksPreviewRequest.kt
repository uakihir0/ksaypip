package work.socialhub.ksaypip.api.request.links

import kotlin.js.JsExport

/**
 * What an address in a post leads to: the Worker reads the page, not the reader.
 */
@JsExport
class LinksPreviewRequest {
    var url: String? = null
}
