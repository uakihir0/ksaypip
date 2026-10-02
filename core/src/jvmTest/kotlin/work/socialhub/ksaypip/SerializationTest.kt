package work.socialhub.ksaypip

import work.socialhub.ksaypip.domain.ConversationSide
import work.socialhub.ksaypip.domain.MarkColor
import work.socialhub.ksaypip.domain.WatchMode
import work.socialhub.ksaypip.entity.Conversation
import work.socialhub.ksaypip.entity.Feed
import work.socialhub.ksaypip.entity.IdentifiedPage
import work.socialhub.ksaypip.entity.Me
import work.socialhub.ksaypip.entity.Notification
import work.socialhub.ksaypip.entity.Person
import work.socialhub.ksaypip.entity.Post
import work.socialhub.ksaypip.entity.Reply
import work.socialhub.ksaypip.entity.ReplyReactions
import work.socialhub.ksaypip.entity.UserPage
import work.socialhub.ksaypip.entity.WatchList
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
              "mark": { "emoji": "🐢", "colors": ["mint", "sage"] },
              "profile": {
                "displayName": "Someone",
                "bio": null,
                "avatarUrl": "https://example.com/a.webp",
                "bannerUrl": null
              },
              "identified": null
            }
            """.trimIndent(),
        )

        assertEquals("vi_tok_8F3K", person.identity)
        assertEquals("the game person", person.label)
        assertEquals("🐢", person.mark.emoji)
        assertEquals(listOf(MarkColor.MINT, MarkColor.SAGE), person.mark.colors?.toList())
        assertNotNull(person.profile)
        assertEquals("Someone", person.profile?.displayName)
        assertNull(person.profile?.bannerUrl)
        assertNull(person.identified)
    }

    @Test
    fun testIdentifiedPersonHasNoViewerIdentity() {
        val person = fromJson<Person>(
            """
            {
              "identity": null,
              "label": null,
              "mark": { "emoji": null, "colors": null },
              "profile": null,
              "identified": {
                "handle": "foo",
                "displayName": "Foo",
                "avatarUrl": "https://example.com/foo.webp",
                "verified": true,
                "operator": false
              }
            }
            """.trimIndent(),
        )

        assertNull(person.identity)
        val identified = assertNotNull(person.identified)
        assertEquals("foo", identified.handle)
        assertTrue(identified.verified)
        assertTrue(!identified.operator)
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
              "everyone": false,
              "identified": false,
              "author": null,
              "authorColors": ["sage", "mint"],
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
        assertTrue(!post.everyone)
        assertTrue(!post.identified)
        assertEquals(listOf(MarkColor.SAGE, MarkColor.MINT), post.authorColors?.toList())
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

        val replyReaction = fromJson<Notification>(
            """
            {
              "kind": "reply.reaction", "conversationId": "c_1", "replyId": "r_1",
              "replyBody": "my reply",
              "reactions": [ { "emoji": "🎉", "count": 1 } ],
              "person": null, "peopleCount": 1,
              "arrivedAt": "2026-08-19T09:00:00.000Z", "readAt": null
            }
            """.trimIndent(),
        )

        assertEquals("reply.reaction", replyReaction.kind)
        assertEquals("r_1", replyReaction.replyId)
        assertEquals("my reply", replyReaction.replyBody)
        assertEquals(1, replyReaction.peopleCount)
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
              "hasWatches": true,
              "canPostIdentified": true,
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
        assertTrue(me.hasWatches)
        assertTrue(me.canPostIdentified)
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

    @Test
    fun testReplyWithIdentifiedAuthorAndReactions() {
        val reply = fromJson<Reply>(
            """
            {
              "id": "r_1", "body": "under my own name", "createdAt": "2026-09-24T09:00:00.000Z",
              "side": "b", "isMine": true, "identified": true,
              "identifiedAuthor": {
                "handle": "foo", "displayName": "Foo", "avatarUrl": null,
                "verified": true, "operator": true
              },
              "reactions": [ { "emoji": "🎉", "count": 1, "mine": false } ]
            }
            """.trimIndent(),
        )

        assertTrue(reply.identified)
        assertEquals("foo", reply.identifiedAuthor?.handle)
        assertTrue(reply.identifiedAuthor?.operator == true)
        assertEquals(1, reply.reactions.size)
        assertEquals(1, reply.reactions[0].count)
    }

    @Test
    fun testReplyReactions() {
        val bar = fromJson<ReplyReactions>(
            """
            { "reactions": [ { "emoji": "🎉", "count": 2, "mine": true } ] }
            """.trimIndent(),
        )

        assertEquals(1, bar.reactions.size)
        assertEquals(2, bar.reactions[0].count)
        assertTrue(bar.reactions[0].mine)
    }

    @Test
    fun testIdentifiedPage() {
        val page = fromJson<IdentifiedPage>(
            """
            {
              "handle": "foo",
              "linkUrl": "https://x.com/foo",
              "profile": {
                "displayName": "Foo", "bio": "hello",
                "avatarUrl": null, "bannerUrl": null
              },
              "operator": false,
              "posts": [],
              "postsNextCursor": null,
              "watching": true
            }
            """.trimIndent(),
        )

        assertEquals("foo", page.handle)
        assertEquals("https://x.com/foo", page.linkUrl)
        assertEquals("Foo", page.profile.displayName)
        assertTrue(page.watching)
        assertTrue(page.posts.isEmpty())
    }

    @Test
    fun testWatchListHasBothModes() {
        val list = fromJson<WatchList>(
            """
            {
              "items": [
                { "mode": "anonymous",
                  "person": { "identity": "vi_tok_1", "label": "someone",
                    "mark": { "emoji": null, "colors": null },
                    "profile": null, "identified": null },
                  "createdAt": "2026-09-25T09:00:00.000Z" },
                { "mode": "identified",
                  "person": { "identity": null, "label": null,
                    "mark": { "emoji": null, "colors": null }, "profile": null,
                    "identified": { "handle": "foo", "displayName": "Foo",
                      "avatarUrl": null, "verified": true, "operator": false } },
                  "createdAt": "2026-09-25T10:00:00.000Z" }
              ]
            }
            """.trimIndent(),
        )

        assertEquals(2, list.items.size)
        assertEquals(WatchMode.ANONYMOUS, list.items[0].mode)
        assertEquals("vi_tok_1", list.items[0].person.identity)
        assertEquals(WatchMode.IDENTIFIED, list.items[1].mode)
        assertNull(list.items[1].person.identity)
        assertEquals("foo", list.items[1].person.identified?.handle)
    }

    @Test
    fun testUserPageCarriesTheMemoAndWatch() {
        val page = fromJson<UserPage>(
            """
            {
              "person": { "identity": "vi_tok_1", "label": null,
                "mark": { "emoji": null, "colors": null }, "profile": null,
                "identified": null },
              "posts": [],
              "postsNextCursor": null,
              "relationship": null,
              "watching": true,
              "note": "a private memo"
            }
            """.trimIndent(),
        )

        assertTrue(page.watching)
        assertEquals("a private memo", page.note)
    }
}
