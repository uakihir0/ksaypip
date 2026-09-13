package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The post a self-reply quotes, and the whole of what a quotation may carry.
 *
 * A bounded excerpt and no more: a post may quote one of its writer's own earlier posts, and the
 * quotation outlives the seven days the quoted post itself is readable. The excerpt is cut on
 * the server before serialization, and [media] holds at most the first picture.
 */
@JsExport
@Serializable
class QuotedPost {

    var id: String = ""

    /** At most the excerpt length in code points, with an ellipsis where there was more. */
    var body: String = ""

    var createdAt: String = ""

    /** At most one picture: the quoted post's first. */
    var media: Array<Media> = arrayOf()

    /** When this viewer's reading of the quoted post ends, or null when it does not. */
    var readableUntil: String? = null
}
