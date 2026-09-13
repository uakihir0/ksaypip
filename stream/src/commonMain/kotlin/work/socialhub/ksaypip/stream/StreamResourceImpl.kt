package work.socialhub.ksaypip.stream

import work.socialhub.ksaypip.stream.api.RoomStream
import work.socialhub.ksaypip.stream.internal.RoomStreamImpl

class StreamResourceImpl(
    private val uri: String,
    private val accessToken: String,
) : StreamResource {

    override fun roomStream(): RoomStream {
        return RoomStreamImpl(uri, accessToken)
    }
}
