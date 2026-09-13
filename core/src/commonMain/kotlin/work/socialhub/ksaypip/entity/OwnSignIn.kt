package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * How this account signs in, for the one screen that shows it to the account itself.
 *
 * The only response in this API that carries an address, and it carries the caller's own. It is
 * cookie-only: a bearer token cannot reach `GET /me/sign-in`.
 */
@JsExport
@Serializable
class OwnSignIn {

    var email: String? = null

    /** The social providers linked to this account (`google`, `apple`). Empty for code-only. */
    var providers: Array<String> = arrayOf()
}
