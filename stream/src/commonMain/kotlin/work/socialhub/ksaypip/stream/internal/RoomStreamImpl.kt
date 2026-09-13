package work.socialhub.ksaypip.stream.internal

import work.socialhub.ksaypip.stream.StreamClient
import work.socialhub.ksaypip.stream.api.RoomStream
import work.socialhub.ksaypip.stream.listener.LifeCycleListener
import work.socialhub.ksaypip.stream.listener.RoomStreamListener

class RoomStreamImpl(
    private val uri: String,
    private val accessToken: String,
) : RoomStream {

    var listener: RoomStreamListener? = null
    var lifeCycle: LifeCycleListener? = null
    var client: StreamClient? = null

    override fun register(
        listener: RoomStreamListener,
        lifeCycle: LifeCycleListener,
    ): RoomStream {
        return also {
            it.listener = listener
            it.lifeCycle = lifeCycle
        }
    }

    override suspend fun open() {
        val listener = checkNotNull(listener) { "listener is required" }
        val lifeCycle = checkNotNull(lifeCycle) { "lifeCycle is required" }

        val client = StreamClient(StreamEndpoint.webSocketUrl(uri), accessToken)
            .also { this.client = it }

        client.eventCallback = listener::onEvent
        client.openedCallback = lifeCycle::onConnect
        client.closedCallback = lifeCycle::onDisconnect
        client.errorCallback = lifeCycle::onError

        client.open()
    }

    override fun close() {
        client?.close()
    }

    override fun isOpen(): Boolean {
        return client?.isOpen ?: false
    }
}
