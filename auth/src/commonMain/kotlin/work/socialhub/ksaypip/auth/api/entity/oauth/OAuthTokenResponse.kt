package work.socialhub.ksaypip.auth.api.entity.oauth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What the token endpoint answers with: an opaque access token, and a refresh token when the
 * reader agreed to `offline_access`.
 */
@JsExport
@Serializable
class OAuthTokenResponse {

    @SerialName("access_token")
    var accessToken: String = ""

    @SerialName("token_type")
    var tokenType: String = ""

    @SerialName("expires_in")
    var expiresIn: Long = -1

    @SerialName("refresh_token")
    var refreshToken: String? = null

    /** The scopes the reader actually agreed to, space-delimited. */
    @SerialName("scope")
    var scope: String? = null
}
