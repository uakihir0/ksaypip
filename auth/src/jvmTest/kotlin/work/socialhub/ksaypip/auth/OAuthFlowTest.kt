package work.socialhub.ksaypip.auth

import com.sun.net.httpserver.HttpServer
import io.ktor.http.Url
import work.socialhub.ksaypip.auth.api.entity.oauth.BuildAuthorizationUrlRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthAuthorizationCodeTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRefreshTokenRequest
import work.socialhub.ksaypip.auth.api.entity.oauth.OAuthRevokeRequest
import java.net.InetSocketAddress
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * The protocol as it goes on the wire, against a local authorization server: the URL the reader
 * is sent to, and what the two back-channel calls carry.
 */
class OAuthFlowTest {

    private lateinit var server: HttpServer
    private var requestPath = ""
    private var form = ""
    private var response = ""

    private lateinit var auth: SaypipAuth

    @BeforeTest
    fun setUp() {
        server = HttpServer.create(InetSocketAddress(0), 0)
        server.createContext("/") { exchange ->
            requestPath = exchange.requestURI.path
            form = exchange.requestBody.readBytes().decodeToString()

            val payload = response.ifEmpty { "{}" }
            val bytes = payload.encodeToByteArray()
            exchange.responseHeaders.add("Content-Type", "application/json")
            exchange.sendResponseHeaders(200, bytes.size.toLong())
            exchange.responseBody.write(bytes)
            exchange.close()
        }
        server.start()

        auth = SaypipAuthFactory.instance(
            SaypipAuthConfig(
                baseUrl = "http://127.0.0.1:${server.address.port}",
                clientId = "saypip_app_test",
                redirectUri = "saypip://callback",
            ),
        )
    }

    @AfterTest
    fun tearDown() {
        server.stop(0)
    }

    @Test
    fun testAuthorizationUrlCarriesPkceAndState() {
        val context = auth.context()
        val url = Url(auth.oauth().buildAuthorizationUrl(context, BuildAuthorizationUrlRequest()))
        val parameters = url.parameters

        assertEquals("/api/auth/oauth2/authorize", url.encodedPath)
        assertEquals("code", parameters["response_type"])
        assertEquals("saypip_app_test", parameters["client_id"])
        assertEquals("saypip://callback", parameters["redirect_uri"])
        assertEquals("read write offline_access", parameters["scope"])
        assertEquals("S256", parameters["code_challenge_method"])
        assertEquals(
            work.socialhub.ksaypip.auth.helper.PkceHelper.codeChallenge(context.codeVerifier!!),
            parameters["code_challenge"],
        )
        assertTrue(!parameters["state"].isNullOrBlank())
    }

    @Test
    fun testAuthorizationCodeExchangeSendsTheVerifier() {
        response = """{"access_token":"at_1","token_type":"Bearer","expires_in":3600,""" +
            """"refresh_token":"rt_1","scope":"read write offline_access"}"""

        val context = auth.context()
        val url = Url(auth.oauth().buildAuthorizationUrl(context, BuildAuthorizationUrlRequest()))
        val state = url.parameters["state"]

        assertNotNull(state)
        assertEquals(state, context.state)

        val token = auth.oauth()
            .authorizationCodeToken(context, OAuthAuthorizationCodeTokenRequest().apply { code = "code-1" })
            .data

        assertEquals("/api/auth/oauth2/token", requestPath)
        assertTrue(form.contains("grant_type=authorization_code"))
        assertTrue(form.contains("code=code-1"))
        assertTrue(form.contains("redirect_uri=saypip%3A%2F%2Fcallback"))
        assertTrue(form.contains("client_id=saypip_app_test"))
        assertTrue(form.contains("code_verifier=${context.codeVerifier}"))

        assertEquals("at_1", token.accessToken)
        assertEquals("Bearer", token.tokenType)
        assertEquals(3600L, token.expiresIn)
        assertEquals("rt_1", token.refreshToken)
    }

    @Test
    fun testRefreshTokenExchange() {
        response = """{"access_token":"at_2","token_type":"Bearer","expires_in":3600}"""

        val token = auth.oauth()
            .refreshToken(auth.context(), OAuthRefreshTokenRequest().apply { refreshToken = "rt_1" })
            .data

        assertTrue(form.contains("grant_type=refresh_token"))
        assertTrue(form.contains("refresh_token=rt_1"))
        assertEquals("at_2", token.accessToken)
        assertEquals(null, token.refreshToken)
    }

    @Test
    fun testRevokeSendsTheToken() {
        auth.oauth().revoke(
            auth.context(),
            OAuthRevokeRequest().apply {
                token = "rt_1"
                tokenTypeHint = "refresh_token"
            },
        )

        assertEquals("/api/auth/oauth2/revoke", requestPath)
        assertTrue(form.contains("token=rt_1"))
        assertTrue(form.contains("token_type_hint=refresh_token"))
        assertTrue(form.contains("client_id=saypip_app_test"))
    }
}
