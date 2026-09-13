package work.socialhub.ksaypip.auth

import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import work.socialhub.ksaypip.SaypipFactory
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRefreshTokenRequest
import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * The refresh grant against the deployment: a confidential client renews its own access token at
 * the token endpoint, which is the same endpoint that proved the client-secret method.
 *
 * The rotated pair is written back to `secrets.json`; it is a no-op without credentials.
 */
class LiveRefreshTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun refreshTheGrant(): Unit = runBlocking {
        val secretsFile = File("../secrets.json")
        if (!secretsFile.exists()) return@runBlocking

        val all = json.decodeFromString<Map<String, Map<String, String>>>(secretsFile.readText())
        val saypip = all["saypip"] ?: return@runBlocking

        val server = saypip["SAYPIP_SERVER"].orEmpty().ifEmpty { "https://saypip.app" }
        val clientId = saypip["SAYPIP_CLIENT_ID"].orEmpty()
        val clientSecret = saypip["SAYPIP_CLIENT_SECRET"]?.takeIf { it.isNotEmpty() }
        val refreshToken = saypip["SAYPIP_REFRESH_TOKEN"].orEmpty()
        if (clientId.isEmpty() || refreshToken.isEmpty()) return@runBlocking

        val auth = SaypipAuthFactory.instance(
            SaypipAuthConfig(
                baseUrl = server,
                clientId = clientId,
                clientSecret = clientSecret,
            ),
        )

        val tokens = auth.oauth().refreshToken(
            auth.context(),
            OAuthRefreshTokenRequest().also { it.refreshToken = refreshToken },
        ).data

        assertTrue(tokens.accessToken.isNotEmpty())

        // The new access token must act: the account is read with it.
        val me = SaypipFactory.instance(server, tokens.accessToken).me()
            .me(MeMeRequest()).data
        println(
            "REFRESH expires=${tokens.expiresIn} scope=${tokens.scope} " +
                "rotated=${!tokens.refreshToken.isNullOrEmpty() && tokens.refreshToken != refreshToken} " +
                "me.unread=${me.unreadNotifications}"
        )

        val updated: Map<String, Map<String, String>> = LinkedHashMap(all).also { map ->
            map["saypip"] = LinkedHashMap(saypip).also { section ->
                section["SAYPIP_ACCESS_TOKEN"] = tokens.accessToken
                if (!tokens.refreshToken.isNullOrEmpty()) {
                    section["SAYPIP_REFRESH_TOKEN"] = tokens.refreshToken
                }
            }
        }
        secretsFile.writeText(json.encodeToString(updated) + "\n")
        println("REFRESH-STORED=ksaypip/secrets.json")
    }
}
