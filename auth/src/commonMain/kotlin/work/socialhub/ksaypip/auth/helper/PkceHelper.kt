package work.socialhub.ksaypip.auth.helper

import dev.whyoleg.cryptography.CryptographyProvider
import dev.whyoleg.cryptography.algorithms.SHA256
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlin.random.Random

/**
 * PKCE: the verifier and the S256 challenge derived from it.
 *
 * A public client cannot keep a secret, so proof that the code came back to the same client that
 * asked for it is this pair instead: the verifier never leaves the client until the token
 * exchange, and only its hash ever reaches the authorization endpoint.
 */
object PkceHelper {

    /**
     * A fresh verifier: 32 random bytes, base64url without padding.
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun codeVerifier(): String {
        val bytes = ByteArray(32)
        Random.nextBytes(bytes)
        return Base64.UrlSafe.encode(bytes).trimEnd('=')
    }

    /**
     * `BASE64URL(SHA256(verifier))`, without padding — what `code_challenge` carries with
     * `code_challenge_method=S256`.
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun codeChallenge(codeVerifier: String): String {
        val hasher = CryptographyProvider.Default.get(SHA256).hasher()
        val digest = hasher.hashBlocking(codeVerifier.encodeToByteArray())
        return Base64.UrlSafe.encode(digest).trimEnd('=')
    }
}
