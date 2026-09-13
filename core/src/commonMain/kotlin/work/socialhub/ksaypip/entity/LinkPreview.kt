package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What is at the other end of a link in a post, so a card can be drawn for it.
 *
 * Every field but the address may be null, and all-null is an ordinary answer rather than a
 * failure: a page can be gone, private, or simply silent about itself.
 */
@JsExport
@Serializable
class LinkPreview {

    /** The address that was asked about, normalized: no fragment. */
    var url: String = ""

    var title: String? = null

    var description: String? = null

    /** An address on this origin, never the far side's. Treat as opaque and pass to an `img`. */
    var imageUrl: String? = null
}
