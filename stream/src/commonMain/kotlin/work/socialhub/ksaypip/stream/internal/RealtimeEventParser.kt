package work.socialhub.ksaypip.stream.internal

import kotlinx.serialization.Serializable
import work.socialhub.ksaypip.domain.RealtimeEventType
import work.socialhub.ksaypip.entity.RealtimeEvent
import work.socialhub.ksaypip.internal.InternalUtility

/**
 * The wire frame as an event, or null for a type this build does not know.
 *
 * Events are additive, and a client ignores types it does not recognise — which is what makes
 * adding one safe — so an unknown type is read and dropped rather than raised.
 */
internal object RealtimeEventParser {

    fun parse(message: String): RealtimeEvent? {
        val envelope = try {
            InternalUtility.fromJson<Envelope>(message)
        } catch (e: Exception) {
            return null
        }

        return when (envelope.type) {
            RealtimeEventType.POST_CREATED,
            RealtimeEventType.POST_DELETED,
            -> RealtimeEvent().also {
                it.type = envelope.type
                it.postId = envelope.postId
            }

            else -> null
        }
    }

    @Serializable
    private class Envelope {
        var type: String = ""
        var postId: String = ""
    }
}
