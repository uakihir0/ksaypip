package work.socialhub.ksaypip.api.response

import kotlin.js.JsExport

/**
 * A successful answer together with how it arrived.
 *
 * The payload is the schema the endpoint declares; [json] is the body as it came, for a caller
 * that wants a field this library does not model.
 */
@JsExport
class Response<T>(
    var data: T,
) {
    var json: String? = null
    var status: Int = 200
}
