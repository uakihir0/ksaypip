package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One picture, at the two sizes it is stored in. Both URLs are subject to the same visibility
 * check, so a client may use either freely: the thumbnail for a list, the full size for the post
 * itself.
 */
@JsExport
@Serializable
class Media {

    var id: String = ""

    var url: String = ""

    var thumbnailUrl: String = ""

    var width: Int = 0

    var height: Int = 0

    /** What the picture shows, or null when nobody wrote one. */
    var alt: String? = null
}
