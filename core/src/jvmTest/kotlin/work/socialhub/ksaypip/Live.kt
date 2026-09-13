package work.socialhub.ksaypip

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

/**
 * The live half of the suite: it reads `secrets.json` (the repository root, which is not
 * committed) and reaches the deployment with the access token there.
 *
 * Everything is a no-op without credentials, so `./gradlew jvmTest` stays green on a machine
 * that has none — and, more importantly, no test ever invents a token.
 */
object Live {

    private val json = Json { ignoreUnknownKeys = true }

    val enabled: Boolean
    val server: String
    val clientId: String
    val clientSecret: String?
    val redirectUri: String
    val accessToken: String
    val refreshToken: String?

    val saypip: Saypip

    init {
        val file = File("../secrets.json")
        val section = if (file.exists()) {
            runCatching {
                json.decodeFromString<Map<String, Map<String, String>>>(file.readText())["saypip"]
            }.getOrNull()
        } else {
            null
        }

        server = section?.get("SAYPIP_SERVER").orEmpty().ifEmpty { "https://saypip.app" }
        clientId = section?.get("SAYPIP_CLIENT_ID").orEmpty()
        clientSecret = section?.get("SAYPIP_CLIENT_SECRET")?.takeIf { it.isNotEmpty() }
        redirectUri = section?.get("SAYPIP_REDIRECT_URI").orEmpty()
        accessToken = section?.get("SAYPIP_ACCESS_TOKEN").orEmpty()
        refreshToken = section?.get("SAYPIP_REFRESH_TOKEN")?.takeIf { it.isNotEmpty() }

        enabled = accessToken.isNotEmpty()
        saypip = SaypipFactory.instance(server, accessToken)
    }

    /**
     * The flow helpers write tokens back here, so a refresh in a test is picked up by the next
     * run rather than leaving a stale pair in the file.
     */
    fun storeTokens(
        accessToken: String,
        refreshToken: String?,
    ) {
        val file = File("../secrets.json")
        if (!file.exists()) return

        val all = json.decodeFromString<Map<String, Map<String, String>>>(file.readText())
        val updated: Map<String, Map<String, String>> = LinkedHashMap(all).also { map ->
            map["saypip"] = LinkedHashMap(map["saypip"] ?: emptyMap()).also { section ->
                section["SAYPIP_ACCESS_TOKEN"] = accessToken
                if (!refreshToken.isNullOrEmpty()) {
                    section["SAYPIP_REFRESH_TOKEN"] = refreshToken
                }
            }
        }
        file.writeText(json.encodeToString(updated) + "\n")
    }
}
