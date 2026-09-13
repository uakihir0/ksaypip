package work.socialhub.ksaypip.auth

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import work.socialhub.ksaypip.SaypipFactory
import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthAuthorizationCodeTokenRequest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * The second half of the development helper: with a callback saved as `build/oauth-callback.json`
 * and the flow context in `build/oauth-context.json`, it exchanges the code, stores the tokens
 * back into `secrets.json`, and proves the token by reading the account and the feed.
 *
 * It does nothing when either file is absent, so the suite stays green without a browser round
 * trip.
 */
class AuthorizationCodeExchangeTest {

    @Serializable
    private data class OAuthContextFile(
        val server: String = "",
        val clientId: String = "",
        val redirectUri: String = "",
        val state: String = "",
        val codeVerifier: String = "",
    )

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun exchangeCodeAndStoreTokens(): Unit = runBlocking {
        val contextFile = File("../build/oauth-context.json")
        val callbackFile = File("../build/oauth-callback.json")
        if (!contextFile.exists() || !callbackFile.exists()) return@runBlocking

        val contextFileValue = json.decodeFromString<OAuthContextFile>(contextFile.readText())
        val callback = json.decodeFromString<Map<String, String>>(callbackFile.readText())

        val secretsFile = File("../secrets.json")
        val all = json.decodeFromString<Map<String, Map<String, String>>>(secretsFile.readText())
        val saypip = all["saypip"] ?: error("saypip section is missing")

        val server = saypip["SAYPIP_SERVER"].orEmpty().ifEmpty { "https://saypip.app" }
        val clientId = saypip["SAYPIP_CLIENT_ID"].orEmpty()

        // The callback must be the one this flow asked for.
        assertEquals(contextFileValue.state, callback["state"])

        val auth = SaypipAuthFactory.instance(
            SaypipAuthConfig(
                baseUrl = server,
                clientId = clientId,
                clientSecret = saypip["SAYPIP_CLIENT_SECRET"],
                redirectUri = contextFileValue.redirectUri,
            ),
        )

        val context = auth.context().also {
            it.clientId = clientId
            it.redirectUri = contextFileValue.redirectUri
            it.state = contextFileValue.state
            it.codeVerifier = contextFileValue.codeVerifier
        }

        val tokens = auth.oauth().authorizationCodeToken(
            context,
            OAuthAuthorizationCodeTokenRequest().also {
                it.code = callback["code"]
            },
        ).data

        // Prove the token before storing it: the account, and the feed as this reader sees it.
        val saypipClient = SaypipFactory.instance(server, tokens.accessToken)
        val me = saypipClient.me().me(MeMeRequest()).data
        val feed = saypipClient.feed().feed(FeedFeedRequest().also { it.limit = 5 }).data

        println("TOKEN-EXPIRES-IN=${tokens.expiresIn}")
        println("SCOPE=${tokens.scope}")
        println("ME unread=${me.unreadNotifications} friends=${me.hasFriends} admin=${me.isAdmin}")
        println("FEED count=${feed.items.size}")

        // Store the tokens where the integration tests find them.
        val updated: Map<String, Map<String, String>> = LinkedHashMap(all).also { map ->
            map["saypip"] = LinkedHashMap(saypip).also { section ->
                section["SAYPIP_ACCESS_TOKEN"] = tokens.accessToken
                if (!tokens.refreshToken.isNullOrEmpty()) {
                    section["SAYPIP_REFRESH_TOKEN"] = tokens.refreshToken
                }
            }
        }
        secretsFile.writeText(json.encodeToString(updated) + "\n")

        // One code is one exchange; a second run must not try to replay it.
        callbackFile.delete()
        println("TOKENS-WRITTEN=ksaypip/secrets.json")
    }
}
