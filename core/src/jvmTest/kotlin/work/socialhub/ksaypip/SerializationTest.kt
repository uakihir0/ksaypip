package work.socialhub.ksaypip

import work.socialhub.ksaypip.domain.ConversationSide
import work.socialhub.ksaypip.domain.MarkColor
import work.socialhub.ksaypip.entity.Conversation
import work.socialhub.ksaypip.entity.Feed
import work.socialhub.ksaypip.entity.Me
import work.socialhub.ksaypip.entity.Notification
import work.socialhub.ksaypip.entity.Person
import work.socialhub.ksaypip.entity.Post
import work.socialhub.ksaypip.entity.WordMuteList
import work.socialhub.ksaypip.internal.InternalUtility.fromJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SerializationTest {

    @Test
    fun testPersonAndProfile() {
        val person = fromJson<Person>(
            """
            {
              "identity": "vi_tok_8F3K",
              "label": "the game person",
              "mark": { "emoji": "🐢", "color": "mint" },
              "profile": {
                "displayName": "Someone",
                "bio": null,
                "avatarUrl": "https://example.com/a.webp",
                "bannerUrl": null
              }
            }
            """.trimIndent(),
        )

        assertEquals("vi_tok_8F3K", person.identity)
        assertEquals("the game person", person.label)
        assertEquals("🐢", person.mark.emoji)
        assertEquals(MarkColor.MINT, person.mark.color)
        assertNotNull(person.profile)
        assertEquals("Someone", person.profile?.displayName)
        assertNull(person.profile?.bannerUrl)
    }

    @Test
    fun testPostWithReactionsAndQuotation() {
        val post = fromJson<Post>(
            """
            {
              "id": "p_7QK",
              "body": "hello",
              "createdAt": "2026-08-19T09:00:00.000Z",
              "media": [
                { "id": "md_1", "url": "https://example.com/full.webp",
                  "thumbnailUrl": "https://example.com/thumb.webp",
                  "width": 1600, "height": 900, "alt": null }
              ],
              "reactions": [ { "emoji": "🎉", "count": 3, "mine": true } ],
              "conversations": { "count": 2, "mine": false, "lastReply": { "body": "hi", "side": "b" } },
              "wantsTalk": false,
              "author": null,
              "authorColor": "sage",
              "isMine": false,
              "readableUntil": "2026-08-26T09:00:00.000Z",
              "replyTo": {
                "id": "p_quoted", "body": "an older thought",
                "createdAt": "2026-08-10T09:00:00.000Z",
                "media": [], "readableUntil": null
              }
            }
            """.trimIndent(),
        )

        assertEquals("p_7QK", post.id)
        assertEquals(1, post.media.size)
        assertEquals(1600, post.media[0].width)
        assertEquals(3, post.reactions[0].count)
        assertTrue(post.reactions[0].mine)
        assertEquals(2, post.conversations.count)
        assertEquals(ConversationSide.B, post.conversations.lastReply?.side)
        assertEquals(MarkColor.SAGE, post.authorColor)
        assertNull(post.author)
        assertNotNull(post.replyTo)
        assertEquals("an older thought", post.replyTo?.body)
        assertTrue(post.replyTo?.media?.isEmpty() == true)
    }

    @Test
    fun testFeed() {
        val feed = fromJson<Feed>(
            """
            { "items": [], "nextCursor": "Y3Vyc29y" }
            """.trimIndent(),
        )

        assertTrue(feed.items.isEmpty())
        assertEquals("Y3Vyc29y", feed.nextCursor)
    }

    @Test
    fun testConversation() {
        val conversation = fromJson<Conversation>(
            """
            {
              "id": "c_1", "postId": "p_1",
              "createdAt": "2026-08-19T09:00:00.000Z",
              "lastReplyAt": "2026-08-19T10:00:00.000Z",
              "participants": [
                { "side": "a", "person": null, "isMe": false },
                { "side": "b", "person": { "identity": "vi_tok", "label": null,
                  "mark": { "emoji": null, "color": null }, "profile": null }, "isMe": true }
              ],
              "isMine": true,
              "replies": [
                { "id": "r_1", "body": "first", "createdAt": "2026-08-19T09:01:00.000Z",
                  "side": "b", "isMine": true }
              ],
              "canReply": true,
              "olderRepliesCursor": null,
              "originPost": null
            }
            """.trimIndent(),
        )

        assertEquals(2, conversation.participants.size)
        assertTrue(conversation.participants[1].isMe)
        assertEquals(1, conversation.replies.size)
        assertEquals("first", conversation.replies[0].body)
        assertTrue(conversation.canReply)
        assertNull(conversation.olderRepliesCursor)
        assertNull(conversation.originPost)
    }

    @Test
    fun testNotificationOfBothKinds() {
        val reaction = fromJson<Notification>(
            """
            {
              "kind": "post.reaction", "postId": "p_1", "postBody": "my post",
              "postImage": null,
              "reactions": [ { "emoji": "🎉", "count": 3 } ],
              "person": null, "peopleCount": 2,
              "arrivedAt": "2026-08-19T09:00:00.000Z", "readAt": null
            }
            """.trimIndent(),
        )

        assertEquals("post.reaction", reaction.kind)
        assertEquals("my post", reaction.postBody)
        assertEquals(1, reaction.reactions?.size)
        assertEquals(2, reaction.peopleCount)
        assertNull(reaction.readAt)

        val reply = fromJson<Notification>(
            """
            {
              "kind": "conversation.reply", "conversationId": "c_1",
              "person": null, "body": "an answer",
              "arrivedAt": "2026-08-19T09:00:00.000Z", "readAt": "2026-08-19T11:00:00.000Z"
            }
            """.trimIndent(),
        )

        assertEquals("conversation.reply", reply.kind)
        assertEquals("c_1", reply.conversationId)
        assertEquals("an answer", reply.body)
    }

    @Test
    fun testMe() {
        val me = fromJson<Me>(
            """
            {
              "createdAt": "2026-08-01T00:00:00.000Z",
              "profile": null,
              "unreadNotifications": 1,
              "unreadConversations": 2,
              "incomingFriendRequests": 3,
              "hasFriends": true,
              "wantsTalkPostId": null,
              "pinnedSubjects": ["猫", "天気"],
              "asideWidgets": [
                { "widget": "search", "visible": true },
                { "widget": "trends", "visible": false }
              ],
              "isAdmin": false,
              "canSendFeedback": true
            }
            """.trimIndent(),
        )

        assertEquals(1, me.unreadNotifications)
        assertEquals(2, me.pinnedSubjects.size)
        assertEquals(2, me.asideWidgets.size)
        assertEquals("trends", me.asideWidgets[1].widget)
        assertTrue(!me.asideWidgets[1].visible)
        assertNull(me.wantsTalkPostId)
    }

    @Test
    fun testWordMutes() {
        val list = fromJson<WordMuteList>(
            """
            {
              "items": [
                { "id": "wm_1", "word": "ネタバレ", "endsAt": "2026-08-20T00:00:00.000Z",
                  "active": false, "createdAt": "2026-08-19T00:00:00.000Z" }
              ],
              "nextCursor": null
            }
            """.trimIndent(),
        )

        assertEquals(1, list.items.size)
        assertEquals("ネタバレ", list.items[0].word)
        assertTrue(!list.items[0].active)
    }
}
