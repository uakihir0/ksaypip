package work.socialhub.ksaypip.stream

import work.socialhub.ksaypip.stream.api.RoomStream

interface StreamResource {

    /**
     * The Global Room: the whole public room, as it happens, filtered for this reader.
     */
    fun roomStream(): RoomStream
}
