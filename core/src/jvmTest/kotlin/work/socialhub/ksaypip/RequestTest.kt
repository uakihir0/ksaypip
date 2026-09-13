package work.socialhub.ksaypip

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.runBlocking
import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.feed.FeedSearchRequest
import work.socialhub.ksaypip.api.request.feed.FeedTagRequest
import work.socialhub.ksaypip.api.request.me.MeUpdateProfileRequest
import work.socialhub.ksaypip.api.request.media.MediaUploadRequest
import work.socialhub.ksaypip.api.request.mutes.MutesMuteRequest
import work.socialhub.ksaypip.api.request.posts.PostsCreateRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactRequest
import java.net.InetSocketAddress
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * What goes on the wire, checked against a local server rather than against the deployment: the
 * path, the method, the query, the bearer token, the idempotency key, the JSON body and the
 * media bytes.
 */
class RequestTest {

    private lateinit var server: HttpServer
    private var method = ""
    private var path = ""
    private var query = ""
    private var contentType = ""
    private var body = ""
    private var bodyBytes = ByteArray(0)
    private var authorization = ""
    private var idempotencyKey: String? = null
    private var status = 200

    private val saypip get() = SaypipFactory.instance("http://127.0.0.1:${server.address.port}", "test-token")

    @BeforeTest
    fun setUp() {
        server = HttpServer.create(InetSocketAddress(0), 0)
        server.createContext("/") { exchange ->
            method = exchange.requestMethod
            path = exchange.requestURI.rawPath
            query = exchange.requestURI.rawQuery ?: ""
            contentType = exchange.requestHeaders.getFirst("Content-Type") ?: ""
            authorization = exchange.requestHeaders.getFirst("Authorization") ?: ""
            idempotencyKey = exchange.requestHeaders.getFirst("Idempotency-Key")
            bodyBytes = exchange.requestBody.readBytes()
            body = bodyBytes.decodeToString()

            val payload = """{"error":{"code":"not_found"}}"""
            val outgoing = if (status in 200..299) "{}" else payload
            val bytes = outgoing.encodeToByteArray()
            exchange.responseHeaders.add("Content-Type", "application/json")
            exchange.sendResponseHeaders(status, bytes.size.toLong())
            exchange.responseBody.write(bytes)
            exchange.close()
        }
        server.start()
    }

    @AfterTest
    fun tearDown() {
        server.stop(0)
    }

    @Test
    fun testFeedCarriesCursorAndBearer() = runBlocking {
        saypip.feed().feed(FeedFeedRequest().apply { cursor = "cur"; limit = 30 })

        assertEquals("GET", method)
        assertEquals("/api/feed", path)
        assertEquals("cursor=cur&limit=30", query)
        assertEquals("Bearer test-token", authorization)
    }

    @Test
    fun testSearchCarriesThePhrase() = runBlocking {
        saypip.feed().search(FeedSearchRequest().apply { q = "ラーメン" })

        assertEquals("/api/search", path)
        assertTrue(query.contains("q=%E3%83%A9%E3%83%BC%E3%83%A1%E3%83%B3"))
    }

    @Test
    fun testTagPathIsPercentEncoded() = runBlocking {
        saypip.feed().tag(FeedTagRequest().apply { tag = "猫" })

        assertEquals("/api/tags/%E7%8C%AB", path)
    }

    @Test
    fun testCreatePostBodyAndIdempotency() = runBlocking {
        saypip.posts().create(
            PostsCreateRequest().apply {
                body = "hello"
                mediaIds = arrayOf("md_1", "md_2")
                wantsTalk = true
                idempotencyKey = "key-1"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/posts", path)
        assertEquals("key-1", idempotencyKey)
        assertEquals("application/json", contentType)
        assertTrue(body.contains("\"body\":\"hello\""))
        assertTrue(body.contains("\"mediaIds\":[\"md_1\",\"md_2\"]"))
        assertTrue(body.contains("\"wantsTalk\":true"))
        assertTrue(!body.contains("idempotencyKey"))
    }

    @Test
    fun testReactUsesTheEmojiPathAndPut() = runBlocking {
        saypip.posts().react(PostsReactRequest().apply { postId = "p_1"; emoji = "🎉" })

        assertEquals("PUT", method)
        assertEquals("/api/posts/p_1/reactions/%F0%9F%8E%89", path)
    }

    @Test
    fun testProfileClearsWithAnExplicitNull() = runBlocking {
        saypip.me().updateProfile(
            MeUpdateProfileRequest().apply {
                displayName = "new name"
                clearBio = true
            },
        )

        assertEquals("PUT", method)
        assertEquals("/api/me/profile", path)
        assertTrue(body.contains("\"displayName\":\"new name\""))
        assertTrue(body.contains("\"bio\":null"))
        assertTrue(!body.contains("avatarMediaId"))
    }

    @Test
    fun testMuteByPostNamesNoIdentity() = runBlocking {
        saypip.mutes().mute(
            MutesMuteRequest().apply {
                postId = "p_1"
                duration = "24h"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/mutes", path)
        assertEquals("""{"target":"post","postId":"p_1","duration":"24h"}""", body)
    }

    @Test
    fun testMediaUploadSendsRawBytesWithTheNamedType() = runBlocking {
        val bytes = byteArrayOf(1, 2, 3, 4)
        saypip.media().upload(
            MediaUploadRequest().apply {
                data = bytes
                contentType = "image/webp"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/media", path)
        assertEquals("image/webp", contentType)
        assertEquals("Bearer test-token", authorization)
        assertTrue(bodyBytes.contentEquals(byteArrayOf(1, 2, 3, 4)))
    }

    @Test
    fun testErrorEnvelopeBecomesATypedException() {
        status = 404

        val exception = assertFailsWith<SaypipException> {
            runBlocking {
                saypip.posts().post(
                    work.socialhub.ksaypip.api.request.posts.PostsPostRequest().apply { postId = "p_missing" },
                )
            }
        }

        assertEquals(404, exception.status)
        assertEquals("not_found", exception.code)
    }
}
