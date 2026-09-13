package work.socialhub.ksaypip.auth.api.entity.oauth

import kotlin.js.JsExport

/**
 * The code returned to the redirect address, exchanged for tokens.
 *
 * The verifier and redirect address come from the [work.socialhub.ksaypip.auth.OAuthContext] the
 * authorization URL was built with.
 */
@JsExport
class OAuthAuthorizationCodeTokenRequest {
    var code: String? = null
}
