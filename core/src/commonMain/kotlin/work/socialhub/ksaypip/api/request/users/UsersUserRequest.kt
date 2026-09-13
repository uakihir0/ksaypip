package work.socialhub.ksaypip.api.request.users

import kotlin.js.JsExport

/**
 * A user page as seen by this viewer, and a page of their posts.
 */
@JsExport
class UsersUserRequest {
    var identityToken: String? = null
    var cursor: String? = null
    var limit: Int? = null
}
