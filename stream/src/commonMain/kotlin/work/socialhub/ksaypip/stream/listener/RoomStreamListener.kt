package work.socialhub.ksaypip.stream.listener

import work.socialhub.ksaypip.entity.RealtimeEvent

/**
 * What the room carries: a post arrived, or a post is gone.
 *
 * The event is a notification and not the post. Read it back through the feed — coalesced and
 * deduplicated by post ID — and the same visibility rules answer as for any other row.
 */
interface RoomStreamListener {

    fun onEvent(event: RealtimeEvent)
}
