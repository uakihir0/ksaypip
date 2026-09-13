package work.socialhub.ksaypip.auth

import kotlinx.serialization.json.Json
import work.socialhub.ksaypip.auth.api.entity.oauth.BuildAuthorizationUrlRequest
import java.io.File
import kotlin.test.Test

/**
 * A development helper, not an assertion: it prints the authorization URL for the client in
 * `secrets.json` and keeps the flow's state and PKCE verifier in `build/oauth-context.json` so
 * the code can be exchanged afterwards.
 *
 * It does nothing when `secrets.json` is absent or unfilled, so the suite stays green without
 * credentials.
 */
class AuthorizationUrlTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun printAuthorizationUrl() {
        val secretsFile = File("../secrets.json")
        if (!secretsFile.exists()) return

        val secrets = json
            .decodeFromString<Map<String, Map<String, String>>>(secretsFile.readText())["saypip"]
            ?: return

        val server = secrets["SAYPIP_SERVER"].orEmpty().ifEmpty { "https://saypip.app" }
        val clientId = secrets["SAYPIP_CLIENT_ID"].orEmpty()
        val redirectUri = secrets["SAYPIP_REDIRECT_URI"].orEmpty()
        if (clientId.isEmpty()) {
            println("secrets.json: SAYPIP_CLIENT_ID is empty.")
            return
        }

        val auth = SaypipAuthFactory.instance(
            SaypipAuthConfig(
                baseUrl = server,
                clientId = clientId,
                clientSecret = secrets["SAYPIP_CLIENT_SECRET"],
                redirectUri = redirectUri,
            ),
        )

        val context = auth.context()
        val url = auth.oauth().buildAuthorizationUrl(context, BuildAuthorizationUrlRequest())

        File("../build").mkdirs()
        File("../build/oauth-context.json").writeText(
            buildString {
                appendLine("{")
                appendLine("""  "server": ${quote(server)},""")
                appendLine("""  "clientId": ${quote(clientId)},""")
                appendLine("""  "redirectUri": ${quote(context.redirectUri ?: "")},""")
                appendLine("""  "state": ${quote(context.state ?: "")},""")
                appendLine("""  "codeVerifier": ${quote(context.codeVerifier ?: "")}""")
                appendLine("}")
            },
        )

        println("AUTHORIZATION-URL=$url")
    }

    private fun quote(value: String): String {
        return kotlinx.serialization.json.JsonPrimitive(value).toString()
    }
}
