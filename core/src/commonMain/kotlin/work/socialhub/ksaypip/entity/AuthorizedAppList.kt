package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The applications this reader has authorized, oldest first.
 */
@JsExport
@Serializable
class AuthorizedAppList {

    var items: Array<AuthorizedApp> = arrayOf()
}
