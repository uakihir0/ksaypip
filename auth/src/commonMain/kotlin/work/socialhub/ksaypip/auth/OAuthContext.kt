package work.socialhub.ksaypip.auth

/**
 * What the client holds across the two steps of the authorization code flow.
 *
 * A client generates the code verifier and the state before sending the reader to the
 * authorization endpoint and keeps them here until the code comes back; the exchange reads both
 * from this object, so a caller cannot accidentally exchange a code without the verifier it was
 * issued against.
 */
class OAuthContext {

    var clientId: String? = null

    var redirectUri: String? = null

    var scopes: List<String>? = null

    /** The one-time state, checked against the callback's `state`. */
    var state: String? = null

    /** The PKCE verifier. Its S256 hash went to the authorization endpoint. */
    var codeVerifier: String? = null
}
