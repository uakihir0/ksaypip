package work.socialhub.ksaypip.stream

import work.socialhub.khttpclient.websocket.WebsocketRequest
import work.socialhub.ksaypip.entity.RealtimeEvent
import work.socialhub.ksaypip.stream.internal.RealtimeEventParser
import work.socialhub.ksaypip.util.Headers

class StreamClient(
    uri: String,
    accessToken: String,
) {

    var client = WebsocketRequest()
    var isOpen: Boolean = false

    var eventCallback: ((RealtimeEvent) -> Unit)? = null
    var openedCallback: (() -> Unit)? = null
    var closedCallback: (() -> Unit)? = null
    var errorCallback: ((Exception) -> Unit)? = null

    fun eventCallback(callback: (RealtimeEvent) -> Unit) = also { this.eventCallback = callback }
    fun openedCallback(callback: () -> Unit) = also { this.openedCallback = callback }
    fun closedCallback(callback: () -> Unit) = also { this.closedCallback = callback }
    fun errorCallback(callback: (Exception) -> Unit) = also { this.errorCallback = callback }

    init {
        this.client.url(uri)

        // A visitor may listen (INV-10), so the header is the token's and not a requirement.
        if (accessToken.isNotEmpty()) {
            this.client.header(Headers.AUTHORIZATION, "Bearer $accessToken")
        }

        this.client.textListener = {
            onMessage(it)
        }
        this.client.onOpenListener = {
            this.isOpen = true
            this.openedCallback?.invoke()
        }
        this.client.onCloseListener = {
            this.isOpen = false
            this.closedCallback?.invoke()
        }
        this.client.onErrorListener = {
            this.errorCallback?.invoke(it)
        }
    }

    suspend fun open() {
        client.open()
    }

    fun close() {
        client.close()
    }

    private fun onMessage(message: String) {
        val event = RealtimeEventParser.parse(message)
        if (event != null) {
            eventCallback?.invoke(event)
        }
    }
}
