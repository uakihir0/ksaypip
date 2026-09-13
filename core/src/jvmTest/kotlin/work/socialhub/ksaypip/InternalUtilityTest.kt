package work.socialhub.ksaypip

import work.socialhub.ksaypip.domain.ErrorCode
import work.socialhub.ksaypip.domain.ErrorReason
import work.socialhub.ksaypip.internal.InternalUtility.errorOf
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class InternalUtilityTest {

    @Test
    fun testUrlEncodeLeavesUnreservedCharacters() {
        assertEquals("abcXYZ0189-._~", urlEncode("abcXYZ0189-._~"))
    }

    @Test
    fun testUrlEncodeEncodesTagAndEmoji() {
        assertEquals("%E7%8C%AB", urlEncode("猫"))
        assertEquals("%F0%9F%8E%89", urlEncode("🎉"))
        assertEquals("a%20b", urlEncode("a b"))
        assertEquals("%2F", urlEncode("/"))
    }

    @Test
    fun testErrorOfParsesCodeAndReason() {
        val exception = errorOf(
            409,
            """{ "error": { "code": "conflict", "reason": "wants_talk_already_open" } }""",
        )

        assertEquals(409, exception.status)
        assertEquals(ErrorCode.CONFLICT, exception.code)
        assertEquals(ErrorReason.WANTS_TALK_ALREADY_OPEN, exception.reason)
    }

    @Test
    fun testErrorOfKeepsABodyWithoutAnEnvelope() {
        val exception = errorOf(500, "oops")

        assertEquals(500, exception.status)
        assertNull(exception.code)
        assertNull(exception.reason)
        assertEquals("oops", exception.body)
    }
}
