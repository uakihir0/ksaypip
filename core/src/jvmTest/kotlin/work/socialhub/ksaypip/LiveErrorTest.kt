package work.socialhub.ksaypip

import kotlinx.coroutines.runBlocking
import work.socialhub.ksaypip.api.request.feed.FeedSearchRequest
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

/**
 * The error envelope, against the deployment: a refusal is typed, and the codes a caller can do
 * something about arrive as themselves rather than as a status number.
 */
class LiveErrorTest {

    @Test
    fun testABadTokenIsUnauthenticated() = runBlocking {
        if (!Live.enabled) return@runBlocking

        val wrong = SaypipFactory.instance(Live.server, "not-a-real-token")
        val exception = assertFailsWith<SaypipException> {
            wrong.me().me(MeMeRequest())
        }
        println("BAD-TOKEN status=${exception.status} code=${exception.code}")
        assertEquals(401, exception.status)
        assertEquals("unauthenticated", exception.code)
    }

    @Test
    fun testAShortPhraseIsAValidationError() = runBlocking {
        if (!Live.enabled) return@runBlocking

        val exception = assertFailsWith<SaypipException> {
            Live.saypip.feed().search(
                FeedSearchRequest().also { it.q = "あ" },
            )
        }
        println("SHORT-QUERY status=${exception.status} code=${exception.code}")
        assertEquals(400, exception.status)
        assertEquals("validation_error", exception.code)
    }
}
