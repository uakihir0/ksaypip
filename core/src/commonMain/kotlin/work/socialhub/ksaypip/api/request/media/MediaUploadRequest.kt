package work.socialhub.ksaypip.api.request.media

import kotlin.js.JsExport

/**
 * An upload, transcoded on the server, before attaching to a post.
 *
 * The bytes are sent raw, with [contentType] as the body's content type — one of `image/webp`,
 * `image/jpeg`, `image/png`. Nothing is stored under the sender's filename.
 */
@JsExport
class MediaUploadRequest {
    var data: ByteArray? = null
    var contentType: String? = null
}
