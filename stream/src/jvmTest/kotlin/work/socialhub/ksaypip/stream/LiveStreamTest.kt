package work.socialhub.ksaypip.stream

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import work.socialhub.ksaypip.SaypipFactory
import work.socialhub.ksaypip.api.request.posts.PostsCreateRequest
import work.socialhub.ksaypip.api.request.posts.PostsDeleteRequest
import work.socialhub.ksaypip.domain.RealtimeEventType
import work.socialhub.ksaypip.entity.RealtimeEvent
import work.socialhub.ksaypip.stream.SaypipEx.stream
import work.socialhub.ksaypip.stream.listener.LifeCycleListener
import work.socialhub.ksaypip.stream.listener.RoomStreamListener
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * The room with a reader's seat, proven end to end: a post is written while the socket is open,
 * and the frame for it is read back — then the post is taken down and its deletion frame is read
 * back too.
 *
 * It is a no-op without credentials in `secrets.json`.
 */
class LiveStreamTest {

    @Test
    fun readAPostedAndDeletedFrameBack() = runBlocking {
        val secretsFile = File("../secrets.json")
        if (!secretsFile.exists()) return@runBlocking

        val secrets = Json { ignoreUnknownKeys = true }
            .decodeFromString<Map<String, Map<String, String>>>(secretsFile.readText())["saypip"]
            ?: return@runBlocking

        val server = secrets["SAYPIP_SERVER"].orEmpty().ifEmpty { "https://saypip.app" }
        val token = secrets["SAYPIP_ACCESS_TOKEN"].orEmpty()
        if (token.isEmpty()) return@runBlocking

        val saypip = SaypipFactory.instance(server, token)
        val frames = Channel<String>(Channel.UNLIMITED)
        var connected = false

        val room = saypip.stream().roomStream()
        room.register(
            listener = object : RoomStreamListener {
                override fun onEvent(event: RealtimeEvent) {
                    frames.trySend("${event.type}:${event.postId}")
                }
            },
            lifeCycle = object : LifeCycleListener {
                override fun onConnect() {
                    connected = true
                }

                override fun onDisconnect() {}

                override fun onError(e: Exception) {
                    println("ROOM-ERROR=${e.message}")
                }
            },
        )

        val opening = launch { room.open() }
        try {
            withTimeout(10_000) {
                while (!connected) delay(50)
            }
            println("ROOM-CONNECTED=true")

            val post = saypip.posts().create(
                PostsCreateRequest().also {
                    it.body = "ksaypip stream test ${System.currentTimeMillis()}"
                },
            ).data

            val created = withTimeout(20_000) {
                var frame = frames.receive()
                while (frame != "${RealtimeEventType.POST_CREATED}:${post.id}") {
                    frame = frames.receive()
                }
                frame
            }
            println("FRAME=$created")
            assertEquals("${RealtimeEventType.POST_CREATED}:${post.id}", created)

            saypip.posts().delete(
                PostsDeleteRequest().also { it.postId = post.id },
            )

            val deleted = withTimeout(20_000) {
                var frame = frames.receive()
                while (frame != "${RealtimeEventType.POST_DELETED}:${post.id}") {
                    frame = frames.receive()
                }
                frame
            }
            println("FRAME=$deleted")
            assertEquals("${RealtimeEventType.POST_DELETED}:${post.id}", deleted)
        } finally {
            room.close()
            opening.cancel()
        }
    }
}
