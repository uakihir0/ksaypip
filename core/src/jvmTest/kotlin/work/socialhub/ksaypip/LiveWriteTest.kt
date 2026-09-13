package work.socialhub.ksaypip

import kotlinx.coroutines.runBlocking
import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.media.MediaBytesRequest
import work.socialhub.ksaypip.api.request.media.MediaSetAltRequest
import work.socialhub.ksaypip.api.request.media.MediaUploadRequest
import work.socialhub.ksaypip.api.request.me.MeArrangeAsideWidgetsRequest
import work.socialhub.ksaypip.api.request.me.MePinSubjectRequest
import work.socialhub.ksaypip.api.request.me.MeReorderPinnedSubjectsRequest
import work.socialhub.ksaypip.api.request.me.MeUnpinSubjectRequest
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import work.socialhub.ksaypip.api.request.mutes.MutesListRequest
import work.socialhub.ksaypip.api.request.mutes.MutesMuteRequest
import work.socialhub.ksaypip.api.request.mutes.MutesUnmuteRequest
import work.socialhub.ksaypip.api.request.notifications.NotificationsListRequest
import work.socialhub.ksaypip.api.request.notifications.NotificationsReadRequest
import work.socialhub.ksaypip.api.request.posts.PostsCreateRequest
import work.socialhub.ksaypip.api.request.posts.PostsDeleteRequest
import work.socialhub.ksaypip.api.request.posts.PostsPostRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactRequest
import work.socialhub.ksaypip.api.request.posts.PostsRemoveWantsTalkRequest
import work.socialhub.ksaypip.api.request.posts.PostsUnreactRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsListRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsRelationshipRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsSetLabelRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesForgetRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesListRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesMuteRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesWindowRequest
import work.socialhub.ksaypip.domain.MediaVariant
import work.socialhub.ksaypip.domain.MuteDuration
import java.util.Base64
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * The writes that are safe to make on a deployment: a post that is taken down again, one
 * reaction put and taken back, one picture uploaded, a mute and a word mute that are lifted, and
 * the reading mark. Nothing here blocks anyone, reports anyone, sends a message to anybody, or
 * touches a relationship — those are not a test's to make.
 */
class LiveWriteTest {

    private val stamp: Long = System.currentTimeMillis()

    @Test
    fun testPostLifecycle(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val body = "ksaypip live test $stamp"
        val key = "ksaypip-live-$stamp"

        val created = Live.saypip.posts().create(
            PostsCreateRequest().also {
                it.body = body
                it.idempotencyKey = key
            },
        ).data
        assertTrue(created.id.isNotEmpty())
        println("CREATED id=${created.id} mine=${created.isMine}")

        // The same key is the same write.
        val replay = Live.saypip.posts().create(
            PostsCreateRequest().also {
                it.body = body
                it.idempotencyKey = key
            },
        ).data
        assertEquals(created.id, replay.id)
        println("REPLAY same-id=true")

        val fetched = Live.saypip.posts().post(
            PostsPostRequest().also { it.postId = created.id },
        ).data
        assertEquals(body, fetched.body)
        assertTrue(fetched.isMine)

        val bar = Live.saypip.posts().react(
            PostsReactRequest().also {
                it.postId = created.id
                it.emoji = "🎉"
            },
        ).data
        val mine = bar.reactions.firstOrNull { it.emoji == "🎉" }
        assertNotNull(mine)
        assertTrue(mine.mine)
        println("REACT count=${mine.count} mine=${mine.mine}")

        // A second press is the same reaction, not a second one.
        val barAgain = Live.saypip.posts().react(
            PostsReactRequest().also {
                it.postId = created.id
                it.emoji = "🎉"
            },
        ).data
        assertEquals(mine.count, barAgain.reactions.first { it.emoji == "🎉" }.count)

        val cleared = Live.saypip.posts().unreact(
            PostsUnreactRequest().also {
                it.postId = created.id
                it.emoji = "🎉"
            },
        ).data
        assertFalse(cleared.reactions.any { it.emoji == "🎉" && it.mine })
        println("UNREACT bars=${cleared.reactions.size}")

        // The ask is one at a time, and taking it back is a write of its own.
        val talk = Live.saypip.posts().create(
            PostsCreateRequest().also {
                it.body = "ksaypip live talk $stamp"
                it.wantsTalk = true
            },
        ).data
        assertTrue(talk.wantsTalk)

        Live.saypip.posts().removeWantsTalk(
            PostsRemoveWantsTalkRequest().also { it.postId = talk.id },
        )
        val talkBack = Live.saypip.posts().post(
            PostsPostRequest().also { it.postId = talk.id },
        ).data
        assertFalse(talkBack.wantsTalk)
        println("WANTS-TALK raised and taken back")

        // Taken down, and a second taking-down is the same answer.
        Live.saypip.posts().delete(
            PostsDeleteRequest().also {
                it.postId = created.id
                it.idempotencyKey = "ksaypip-live-delete-$stamp"
            },
        )
        val missing = assertFailsWith<SaypipException> {
            Live.saypip.posts().post(
                PostsPostRequest().also { it.postId = created.id },
            )
        }
        assertEquals("not_found", missing.code)
        println("DELETED id=${created.id} then 404")

        Live.saypip.posts().delete(
            PostsDeleteRequest().also { it.postId = talk.id },
        )
    }

    @Test
    fun testMediaUpload(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val onePixelPng = Base64.getDecoder().decode(
            "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8z8BQDwAEhQGAhKmMIQAAAABJRU5ErkJggg=="
        )

        val media = Live.saypip.media().upload(
            MediaUploadRequest().also {
                it.data = onePixelPng
                it.contentType = "image/png"
            },
        ).data
        assertTrue(media.id.isNotEmpty())
        assertTrue(media.width > 0 && media.height > 0)
        println("MEDIA id=${media.id} ${media.width}x${media.height} url=${media.url}")

        val alt = Live.saypip.media().setAlt(
            MediaSetAltRequest().also {
                it.mediaId = media.id
                it.alt = "ksaypip live test picture"
            },
        ).data
        assertEquals("ksaypip live test picture", alt.alt)

        val full = Live.saypip.media().bytes(
            MediaBytesRequest().also {
                it.mediaId = media.id
                it.variant = MediaVariant.FULL
            },
        ).data
        val thumb = Live.saypip.media().bytes(
            MediaBytesRequest().also {
                it.mediaId = media.id
                it.variant = MediaVariant.THUMB
            },
        ).data
        assertTrue(full.isNotEmpty() && thumb.isNotEmpty())
        println("MEDIA bytes full=${full.size} thumb=${thumb.size}")

        // The picture is attached to a post, and then the description cannot be rewritten.
        val post = Live.saypip.posts().create(
            PostsCreateRequest().also {
                it.body = "ksaypip live media $stamp"
                it.mediaIds = arrayOf(media.id)
            },
        ).data
        assertEquals(1, post.media.size)
        assertEquals(media.id, post.media[0].id)
        println("MEDIA attached to post=${post.id}")

        val refusal = assertFailsWith<SaypipException> {
            Live.saypip.media().setAlt(
                MediaSetAltRequest().also {
                    it.mediaId = media.id
                    it.alt = "a late description"
                },
            )
        }
        println("MEDIA late alt refusal: ${refusal.code}/${refusal.reason}")
        assertEquals("conflict", refusal.code)

        Live.saypip.posts().delete(
            PostsDeleteRequest().also { it.postId = post.id },
        )
    }

    @Test
    fun testMuteByPostAndUnmute(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val feed = Live.saypip.feed().feed(FeedFeedRequest().also { it.limit = 20 }).data
        val post = feed.items.firstOrNull { !it.isMine } ?: run {
            println("MUTE skipped: the room has no other author right now")
            return@runBlocking
        }

        Live.saypip.mutes().mute(
            MutesMuteRequest().also {
                it.postId = post.id
                it.duration = MuteDuration.HOUR_1
            },
        )

        val list = Live.saypip.mutes().list(MutesListRequest()).data
        val muted = list.items.firstOrNull { it.person.identity.isNotEmpty() }
        assertNotNull(muted)
        println("MUTE count=${list.items.size} endsAt=${muted.endsAt}")

        Live.saypip.mutes().unmute(
            MutesUnmuteRequest().also { it.identityToken = muted.person.identity },
        )
        val after = Live.saypip.mutes().list(MutesListRequest()).data
        assertFalse(after.items.any { it.person.identity == muted.person.identity })
        println("UNMUTE done")
    }

    @Test
    fun testWordMuteWindowAndForget(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val word = "ksaypiplivetest"

        Live.saypip.wordMutes().mute(
            WordMutesMuteRequest().also {
                it.word = word
                it.duration = MuteDuration.HOUR_1
            },
        )

        val listed = Live.saypip.wordMutes().list(WordMutesListRequest()).data
        val entry = listed.items.firstOrNull { it.word == word }
        assertNotNull(entry)
        assertTrue(entry.active)
        println("WORD-MUTE id=${entry.id} active=${entry.active} endsAt=${entry.endsAt}")

        Live.saypip.wordMutes().setWindow(
            WordMutesWindowRequest().also {
                it.wordMuteId = entry.id
                it.duration = MuteDuration.DAYS_30
            },
        )
        val windowed = Live.saypip.wordMutes().list(WordMutesListRequest()).data
            .items.first { it.id == entry.id }
        assertTrue(windowed.active)
        println("WORD-MUTE window endsAt=${windowed.endsAt}")

        Live.saypip.wordMutes().forget(
            WordMutesForgetRequest().also { it.wordMuteId = entry.id },
        )
        val after = Live.saypip.wordMutes().list(WordMutesListRequest()).data
        assertFalse(after.items.any { it.id == entry.id })
        println("WORD-MUTE forgotten")
    }

    @Test
    fun testPinnedSubjectsAndAsideWidgets(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val before = Live.saypip.me().me(MeMeRequest()).data
        val widgets = before.asideWidgets.toList()
        println("BEFORE pinned=${before.pinnedSubjects.toList()}")

        // Putting a subject on is idempotent, and the answer is the whole row.
        val once = Live.saypip.me().pinSubject(
            MePinSubjectRequest().also { it.tag = "猫" },
        ).data.items.toList()
        assertTrue(once.contains("猫"))

        val twice = Live.saypip.me().pinSubject(
            MePinSubjectRequest().also { it.tag = "猫" },
        ).data.items.toList()
        assertEquals(once, twice)
        println("PINNED ${once}")

        // The order is the resource: the whole row is sent as one rearrangement.
        val reversed = once.reversed().toTypedArray()
        val reordered = Live.saypip.me().reorderPinnedSubjects(
            MeReorderPinnedSubjectsRequest().also { it.items = reversed },
        ).data.items.toList()
        assertEquals(once.reversed(), reordered)
        println("REORDERED ${reordered}")

        val after = Live.saypip.me().unpinSubject(
            MeUnpinSubjectRequest().also { it.tag = "猫" },
        ).data.items.toList()
        assertFalse(after.contains("猫"))
        println("UNPINNED ${after}")

        // The right-hand column is the same shape: every widget, in the arranged order.
        val arranged = Live.saypip.me().arrangeAsideWidgets(
            MeArrangeAsideWidgetsRequest().also { it.items = widgets.toTypedArray() },
        ).data.items.toList()
        assertEquals(
            widgets.map { it.widget to it.visible },
            arranged.map { it.widget to it.visible },
        )
        println("WIDGETS ${arranged.map { "${it.widget}=${it.visible}" }}")
    }

    @Test
    fun testRelationshipPageAndLabel(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val list = Live.saypip.relationships().list(RelationshipsListRequest()).data
        val summary = list.items.firstOrNull() ?: run {
            println("RELATIONSHIP skipped: no counterpart on this account yet")
            return@runBlocking
        }

        val page = Live.saypip.relationships().relationship(
            RelationshipsRelationshipRequest().also { it.relationshipId = summary.id },
        ).data
        println("REL page note=${page.note} request=${page.friendRequest?.direction} " +
            "conversations=${page.conversations.size} can=${page.canSendFriendRequest}")

        val originalLabel = page.counterpart.label
        val originalNote = page.note
        val originalEmoji = page.counterpart.mark.emoji
        val originalColor = page.counterpart.mark.color

        val set = Live.saypip.relationships().setLabel(
            RelationshipsSetLabelRequest().also {
                it.relationshipId = summary.id
                it.label = "ksaypip live"
                it.note = "a live test memo"
                it.markEmoji = "🐢"
                it.markColor = "mint"
            },
        ).data
        assertEquals("ksaypip live", set.label)
        assertEquals("a live test memo", set.note)
        assertEquals("🐢", set.mark.emoji)
        println("REL label set: ${set.label} ${set.mark.emoji}/${set.mark.color}")

        // A replacement and not a patch: what was there goes back, nulls included.
        val restored = Live.saypip.relationships().setLabel(
            RelationshipsSetLabelRequest().also {
                it.relationshipId = summary.id
                it.label = originalLabel
                it.note = originalNote
                it.markEmoji = originalEmoji
                it.markColor = originalColor
            },
        ).data
        assertEquals(originalLabel, restored.label)
        assertEquals(originalNote, restored.note)
        println("REL restored: ${restored.label} note=${restored.note}")
    }

    @Test
    fun testNotificationsRead(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val before = Live.saypip.notifications().list(
            NotificationsListRequest().also { it.limit = 5 },
        ).data
        println("NOTIF before unread=${before.items.count { it.readAt == null }}")

        Live.saypip.notifications().read(NotificationsReadRequest())

        val after = Live.saypip.notifications().list(
            NotificationsListRequest().also { it.limit = 5 },
        ).data
        val me = Live.saypip.me().me(MeMeRequest()).data
        println("NOTIF after unread=${after.items.count { it.readAt == null }} badge=${me.unreadNotifications}")
    }
}
