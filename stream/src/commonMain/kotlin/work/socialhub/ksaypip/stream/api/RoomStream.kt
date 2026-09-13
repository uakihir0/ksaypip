package work.socialhub.ksaypip.stream.api

import work.socialhub.ksaypip.stream.listener.LifeCycleListener
import work.socialhub.ksaypip.stream.listener.RoomStreamListener

interface RoomStream {

    /**
     * Register the event listener and the life-cycle listener. Both are required before
     * [open].
     */
    fun register(
        listener: RoomStreamListener,
        lifeCycle: LifeCycleListener,
    ): RoomStream

    /**
     * Open the socket and receive frames until [close], or until the connection ends.
     *
     * There is no message a client can send and no replay: a frame missed while the socket was
     * down is read back with the feed, or not at all.
     */
    suspend fun open()

    fun close()

    fun isOpen(): Boolean
}
