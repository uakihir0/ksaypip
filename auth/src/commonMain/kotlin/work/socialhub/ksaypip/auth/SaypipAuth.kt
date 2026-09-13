package work.socialhub.ksaypip.auth

import work.socialhub.ksaypip.auth.api.OAuthResource

/**
 * The client side of this deployment's OAuth 2.1 authorization server.
 *
 * Nothing here reaches the social domain; what it produces — an access token — is what
 * `SaypipFactory.instance` takes.
 */
interface SaypipAuth {

    fun oauth(): OAuthResource

    /**
     * The state the flow keeps between the authorization URL and the token exchange.
     */
    fun context(): OAuthContext
}
