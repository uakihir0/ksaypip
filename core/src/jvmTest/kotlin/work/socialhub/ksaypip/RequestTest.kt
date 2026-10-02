package work.socialhub.ksaypip

import com.sun.net.httpserver.HttpServer
import kotlinx.coroutines.runBlocking
import work.socialhub.ksaypip.api.request.apps.AppsIconRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsReplyRequest
import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.feed.FeedSearchRequest
import work.socialhub.ksaypip.api.request.feed.FeedTagRequest
import work.socialhub.ksaypip.api.request.identified.IdentifiedPageRequest
import work.socialhub.ksaypip.api.request.me.MeUpdateProfileRequest
import work.socialhub.ksaypip.api.request.media.MediaUploadRequest
import work.socialhub.ksaypip.api.request.mutes.MutesMuteRequest
import work.socialhub.ksaypip.api.request.posts.PostsCreateRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactRequest
import work.socialhub.ksaypip.api.request.posts.PostsStartConversationRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsSetLabelRequest
import work.socialhub.ksaypip.api.request.replies.RepliesReactRequest
import work.socialhub.ksaypip.api.request.replies.RepliesUnreactRequest
import work.socialhub.ksaypip.api.request.users.UsersSetLabelRequest
import work.socialhub.ksaypip.api.request.watches.WatchesListRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchRequest
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
    private var responseBytes: ByteArray? = null

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
            val bytes = responseBytes ?: outgoing.encodeToByteArray()
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
    fun testStartConversationPathBodyAndIdempotency() = runBlocking {
        saypip.posts().startConversation(
            PostsStartConversationRequest().apply {
                postId = "p_1"
                body = "はじめまして"
                idempotencyKey = "key-2"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/posts/p_1/conversations", path)
        assertEquals("key-2", idempotencyKey)
        assertEquals("application/json", contentType)
        assertEquals("""{"body":"はじめまして"}""", body)
        // The key is a header, never a body field.
        assertTrue(!body.contains("idempotencyKey"))
    }

    @Test
    fun testStartConversationCanBeIdentified() = runBlocking {
        saypip.posts().startConversation(
            PostsStartConversationRequest().apply {
                postId = "p_1"
                body = "はじめまして"
                identified = true
            },
        )

        assertEquals("""{"body":"はじめまして","identified":true}""", body)
    }

    @Test
    fun testConversationReplyCanBeIdentified() = runBlocking {
        saypip.conversations().reply(
            ConversationsReplyRequest().apply {
                conversationId = "c_1"
                body = "hello"
                identified = true
                idempotencyKey = "key-3"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/conversations/c_1/replies", path)
        assertEquals("key-3", idempotencyKey)
        assertEquals("""{"body":"hello","identified":true}""", body)
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
    fun testCreatePostCarriesEveryoneAndIdentified() = runBlocking {
        saypip.posts().create(
            PostsCreateRequest().apply {
                body = "quiet #猫"
                everyone = false
                identified = true
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/posts", path)
        assertTrue(body.contains("\"everyone\":false"))
        assertTrue(body.contains("\"identified\":true"))
    }

    @Test
    fun testPostReactionCanBeIdentified() = runBlocking {
        saypip.posts().react(
            PostsReactRequest().apply {
                postId = "p_1"
                emoji = "🎉"
                identified = true
            },
        )

        assertEquals("PUT", method)
        assertEquals("/api/posts/p_1/reactions/%F0%9F%8E%89", path)
        assertEquals("""{"identified":true}""", body)
    }

    @Test
    fun testReplyReactionCanBeIdentified() = runBlocking {
        saypip.replies().react(
            RepliesReactRequest().apply {
                replyId = "r_1"
                emoji = "🎉"
                identified = true
            },
        )

        assertEquals("PUT", method)
        assertEquals("/api/replies/r_1/reactions/%F0%9F%8E%89", path)
        assertEquals("""{"identified":true}""", body)
    }

    @Test
    fun testReplyUnreactUsesTheEmojiPath() = runBlocking {
        saypip.replies().unreact(
            RepliesUnreactRequest().apply {
                replyId = "r_1"
                emoji = "🎉"
            },
        )

        assertEquals("DELETE", method)
        assertEquals("/api/replies/r_1/reactions/%F0%9F%8E%89", path)
        assertEquals("", body)
    }

    @Test
    fun testSetLabelByIdentityCarriesTheGradient() = runBlocking {
        saypip.users().setLabel(
            UsersSetLabelRequest().apply {
                identityToken = "vi_tok_1"
                label = "a name"
                markEmoji = "🐢"
                markColors = arrayOf("mint", "sage")
            },
        )

        assertEquals("PUT", method)
        assertEquals("/api/users/vi_tok_1/label", path)
        assertEquals(
            """{"label":"a name","note":null,"mark":{"emoji":"🐢","colors":["mint","sage"]}}""",
            body,
        )
    }

    @Test
    fun testRelationshipLabelUsesTheColorsPair() = runBlocking {
        saypip.relationships().setLabel(
            RelationshipsSetLabelRequest().apply {
                relationshipId = "rel_1"
                markColors = arrayOf("rose", "sky")
            },
        )

        assertEquals("PUT", method)
        assertEquals("/api/relationships/rel_1/label", path)
        assertEquals(
            """{"label":null,"note":null,"mark":{"emoji":null,"colors":["rose","sky"]}}""",
            body,
        )
    }

    @Test
    fun testWatchListIsARead() = runBlocking {
        saypip.watches().list(WatchesListRequest())

        assertEquals("GET", method)
        assertEquals("/api/watches", path)
        assertEquals("Bearer test-token", authorization)
    }

    @Test
    fun testWatchAndUnwatch() = runBlocking {
        saypip.watches().watch(
            WatchesWatchRequest().apply {
                identity = "vi_tok_1"
                idempotencyKey = "watch-1"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/watches", path)
        assertEquals("watch-1", idempotencyKey)
        assertEquals("""{"identity":"vi_tok_1"}""", body)

        saypip.watches().unwatch(WatchesUnwatchRequest().apply { identityToken = "vi_tok/2" })

        assertEquals("DELETE", method)
        assertEquals("/api/watches/vi_tok%2F2", path)
    }

    @Test
    fun testWatchIdentifiedUsesTheHandle() = runBlocking {
        saypip.watches().watchIdentified(
            WatchesWatchIdentifiedRequest().apply {
                handle = "foo"
                idempotencyKey = "watch-2"
            },
        )

        assertEquals("POST", method)
        assertEquals("/api/watches/identified", path)
        assertEquals("watch-2", idempotencyKey)
        assertEquals("""{"handle":"foo"}""", body)

        saypip.watches().unwatchIdentified(
            WatchesUnwatchIdentifiedRequest().apply { handle = "foo" },
        )

        assertEquals("DELETE", method)
        assertEquals("/api/watches/identified/foo", path)
    }

    @Test
    fun testIdentifiedPageIsPaged() = runBlocking {
        saypip.identified().page(
            IdentifiedPageRequest().apply {
                handle = "foo"
                cursor = "cur"
                limit = 10
            },
        )

        assertEquals("GET", method)
        assertEquals("/api/identified/foo", path)
        assertEquals("cursor=cur&limit=10", query)
    }

    @Test
    fun testAppIconComesBackAsBytes() = runBlocking {
        // The icon is the one read in this suite whose body is not JSON.
        responseBytes = byteArrayOf(0x52, 0x49, 0x46, 0x46)

        val response = saypip.apps().icon(AppsIconRequest().apply { clientId = "saypip_app_1" })

        assertEquals("GET", method)
        assertEquals("/api/oauth/app-icon/saypip_app_1", path)
        assertTrue(response.data.contentEquals(byteArrayOf(0x52, 0x49, 0x46, 0x46)))
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
