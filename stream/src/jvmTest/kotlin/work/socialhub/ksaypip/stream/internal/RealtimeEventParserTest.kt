package work.socialhub.ksaypip.stream.internal

import work.socialhub.ksaypip.domain.RealtimeEventType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RealtimeEventParserTest {

    @Test
    fun testPostCreated() {
        val event = RealtimeEventParser.parse(
            """{ "type": "post.created", "postId": "p_7QK" }""",
        )

        assertEquals(RealtimeEventType.POST_CREATED, event?.type)
        assertEquals("p_7QK", event?.postId)
    }

    @Test
    fun testPostDeleted() {
        val event = RealtimeEventParser.parse(
            """{ "type": "post.deleted", "postId": "p_7QK" }""",
        )

        assertEquals(RealtimeEventType.POST_DELETED, event?.type)
        assertEquals("p_7QK", event?.postId)
    }

    @Test
    fun testAnUnknownTypeIsIgnored() {
        // Events are additive: a type this build does not know is read and dropped.
        assertNull(RealtimeEventParser.parse("""{ "type": "post.pinned", "postId": "p_1" }"""))
    }

    @Test
    fun testABodyThatIsNotAFrameIsIgnored() {
        assertNull(RealtimeEventParser.parse("oops"))
        assertNull(RealtimeEventParser.parse(""))
    }

    @Test
    fun testAnEventCarriesNothingButTheID() {
        val event = RealtimeEventParser.parse(
            """
            {
              "type": "post.created",
              "postId": "p_7QK",
              "body": "this field does not exist",
              "author": { "identity": "vi_tok_1" }
            }
            """.trimIndent(),
        )

        assertEquals("p_7QK", event?.postId)
    }
}
