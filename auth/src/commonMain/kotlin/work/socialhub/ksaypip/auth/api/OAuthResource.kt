package work.socialhub.ksaypip.auth.api

import work.socialhub.ksaypip.auth.OAuthContext
import work.socialhub.ksaypip.auth.api.entity.oauth.BuildAuthorizationUrlRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthAuthorizationCodeTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRefreshTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRevokeRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthTokenResponse
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit

/**
 * The three endpoints of the authorization code flow with PKCE.
 *
 * The intended order is: [buildAuthorizationUrl] (which generates and keeps the verifier and the
 * state in the context), the reader's browser, the callback carrying `code` and `state`, then
 * [authorizationCodeToken] with the code. [refreshToken] carries an application past the access
 * token's hour, and [revoke] ends one.
 */
interface OAuthResource {

    /**
     * Where to send the reader. Generates a PKCE verifier and a state into the context when it
     * does not already hold them, and returns the URL to open.
     */
    fun buildAuthorizationUrl(
        context: OAuthContext,
        request: BuildAuthorizationUrlRequest,
    ): String

    /**
     * The code, exchanged for an access token and — where `offline_access` was granted — a refresh
     * token.
     */
    fun authorizationCodeToken(
        context: OAuthContext,
        request: OAuthAuthorizationCodeTokenRequest,
    ): Response<OAuthTokenResponse>

    /**
     * A refresh token exchanged for a new access token.
     */
    fun refreshToken(
        context: OAuthContext,
        request: OAuthRefreshTokenRequest,
    ): Response<OAuthTokenResponse>

    /**
     * A token given up. Nothing to answer with.
     */
    fun revoke(
        context: OAuthContext,
        request: OAuthRevokeRequest,
    ): ResponseUnit
}
