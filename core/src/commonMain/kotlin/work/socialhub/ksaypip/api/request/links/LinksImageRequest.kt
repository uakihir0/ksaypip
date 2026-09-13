package work.socialhub.ksaypip.api.request.links

import kotlin.js.JsExport

/**
 * A link card's picture, by *page* address: the only public, cacheable answer here.
 */
@JsExport
class LinksImageRequest {
    var url: String? = null
}
