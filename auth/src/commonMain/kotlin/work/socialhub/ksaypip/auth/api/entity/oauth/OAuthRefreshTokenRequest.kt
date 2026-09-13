package work.socialhub.ksaypip.auth.api.entity.oauth

import kotlin.js.JsExport

/**
 * A refresh token exchanged for a new access token — and, where the server rotates them, a new
 * refresh token.
 */
@JsExport
class OAuthRefreshTokenRequest {
    var refreshToken: String? = null
}
