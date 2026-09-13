package work.socialhub.ksaypip.api.request.feedback

import kotlin.js.JsExport

/**
 * Tell us about saypip. Delivered, and stored nowhere.
 *
 * [route] is a route *pattern* (`/posts/$postId`) and never the address the sender was on. The
 * picture is re-encoded and its filename dropped; it travels as `screenshot.webp`.
 */
@JsExport
class FeedbackSendRequest {
    var message: String? = null
    var contact: String? = null
    var route: String? = null
    var image: ByteArray? = null
}
