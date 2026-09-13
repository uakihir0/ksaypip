package work.socialhub.ksaypip.api.request.blocks

import kotlin.js.JsExport

/**
 * Block is forget plus avoid: everything termination does, plus keeping the two apart, without
 * telling the blocked person they were blocked.
 */
@JsExport
class BlocksBlockRequest {
    var identity: String? = null
    var idempotencyKey: String? = null
}
