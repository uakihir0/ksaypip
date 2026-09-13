package work.socialhub.ksaypip.auth

import work.socialhub.ksaypip.auth.helper.PkceHelper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PkceHelperTest {

    /**
     * The S256 example from RFC 7636, Appendix B.
     */
    @Test
    fun testS256AgainstTheRfcVector() {
        val verifier = "dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk"

        assertEquals(
            "E9Melhoa2OwvFrEMTJguCHaoeK1t8URWbuGJSstw-cM",
            PkceHelper.codeChallenge(verifier),
        )
    }

    @Test
    fun testCodeVerifierIsUrlSafeAndUnpadded() {
        val verifier = PkceHelper.codeVerifier()

        assertEquals(43, verifier.length)
        assertTrue(verifier.all { it.isLetterOrDigit() || it == '-' || it == '_' })
        assertTrue(!verifier.contains('='))
    }
}
