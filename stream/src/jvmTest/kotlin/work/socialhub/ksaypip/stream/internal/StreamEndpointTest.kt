package work.socialhub.ksaypip.stream.internal

import kotlin.test.Test
import kotlin.test.assertEquals

class StreamEndpointTest {

    @Test
    fun testHttpsBecomesWss() {
        assertEquals(
            "wss://saypip.app/ws",
            StreamEndpoint.webSocketUrl("https://saypip.app"),
        )
    }

    @Test
    fun testHttpBecomesWsForALocalDeployment() {
        assertEquals(
            "ws://127.0.0.1:8787/ws",
            StreamEndpoint.webSocketUrl("http://127.0.0.1:8787"),
        )
    }

    @Test
    fun testTrailingSlashIsIgnored() {
        assertEquals(
            "wss://saypip.app/ws",
            StreamEndpoint.webSocketUrl("https://saypip.app/"),
        )
    }

    @Test
    fun testAnAddressThatAlreadyNamesASchemeIsKept() {
        assertEquals(
            "ws://localhost:8787/ws",
            StreamEndpoint.webSocketUrl("ws://localhost:8787"),
        )
    }
}
