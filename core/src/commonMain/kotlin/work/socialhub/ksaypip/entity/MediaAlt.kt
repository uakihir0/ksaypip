package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What `PUT /api/media/{mediaId}/alt` answers with. Null, or a string that trims to nothing,
 * both mean "nobody has described this picture".
 */
@JsExport
@Serializable
class MediaAlt {

    var alt: String? = null
}
