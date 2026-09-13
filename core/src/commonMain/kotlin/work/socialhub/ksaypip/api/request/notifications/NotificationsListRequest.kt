package work.socialhub.ksaypip.api.request.notifications

import kotlin.js.JsExport

/**
 * What happened to you, paged; newest arrival first.
 */
@JsExport
class NotificationsListRequest {
    var cursor: String? = null
    var limit: Int? = null
}
