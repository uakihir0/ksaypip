package work.socialhub.ksaypip.auth

import work.socialhub.ksaypip.domain.OAuthScope

/**
 * One deployment and one registered application.
 *
 * [clientSecret] is for confidential clients only: a client that cannot keep a secret — a
 * native app — leaves it null and relies on PKCE alone, which is what the authorization
 * server expects of a public application.
 */
data class SaypipAuthConfig(
    /** The deployment's origin, `https://saypip.app`, with no trailing slash. */
    var baseUrl: String = "https://saypip.app",

    /** The client ID an operator's register issued. */
    var clientId: String = "",

    /** The client secret, for a confidential client. Never set for a public one. */
    var clientSecret: String? = null,

    /** Where the authorization server returns the code: an address the application owns. */
    var redirectUri: String? = null,

    /** What to ask the reader to agree to, a subset of [OAuthScope.ALL]. */
    var scopes: List<String> = OAuthScope.ALL.toList(),

    /** Overrides, where a deployment does not host the protocol at its default addresses. */
    var authorizationEndpoint: String? = null,
    var tokenEndpoint: String? = null,
    var revokeEndpoint: String? = null,
) {

    val authorizationUrl: String
        get() = authorizationEndpoint ?: "$baseUrl/api/auth/oauth2/authorize"

    val tokenUrl: String
        get() = tokenEndpoint ?: "$baseUrl/api/auth/oauth2/token"

    val revokeUrl: String
        get() = revokeEndpoint ?: "$baseUrl/api/auth/oauth2/revoke"
}
