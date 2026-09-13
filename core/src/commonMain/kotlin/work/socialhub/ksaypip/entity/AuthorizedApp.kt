package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One application a reader has authorized, as the settings list draws it.
 *
 * The handle is the consent's own ID and not the client ID: ending an authorization is a
 * statement about this reader's grant, so the row they hold is the row they act on.
 */
@JsExport
@Serializable
class AuthorizedApp {

    var id: String = ""

    var clientId: String = ""

    var name: String? = null

    var uri: String? = null

    var scopes: Array<String> = arrayOf()

    var grantedAt: String = ""

    var updatedAt: String = ""
}
