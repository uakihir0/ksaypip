package work.socialhub.ksaypip

import kotlinx.coroutines.runBlocking
import work.socialhub.ksaypip.api.request.apps.AppsListRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsConversationRequest
import work.socialhub.ksaypip.api.request.conversations.ConversationsListRequest
import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.feed.FeedFriendsRequest
import work.socialhub.ksaypip.api.request.feed.FeedSearchRequest
import work.socialhub.ksaypip.api.request.feed.FeedTagRequest
import work.socialhub.ksaypip.api.request.feed.FeedTalkRequest
import work.socialhub.ksaypip.api.request.feed.FeedTrendsRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsListRequest
import work.socialhub.ksaypip.api.request.links.LinksImageRequest
import work.socialhub.ksaypip.api.request.links.LinksPreviewRequest
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import work.socialhub.ksaypip.api.request.me.MePostsRequest
import work.socialhub.ksaypip.api.request.mutes.MutesListRequest
import work.socialhub.ksaypip.api.request.notifications.NotificationsListRequest
import work.socialhub.ksaypip.api.request.posts.PostsConversationsRequest
import work.socialhub.ksaypip.api.request.posts.PostsPostRequest
import work.socialhub.ksaypip.api.request.posts.PostsReactionsRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsListRequest
import work.socialhub.ksaypip.api.request.users.UsersUserRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesListRequest
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

/**
 * Every read the client offers, against the deployment in `secrets.json`.
 *
 * It is a smoke suite: each test asserts little and prints much, because what it is looking for
 * is the exception that says a request or a shape is wrong.
 */
class LiveReadTest {

    @Test
    fun testMeAndTrends(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val me = Live.saypip.me().me(MeMeRequest()).data
        println("ME unread=${me.unreadNotifications} conv=${me.unreadConversations} " +
            "requests=${me.incomingFriendRequests} friends=${me.hasFriends} " +
            "pinned=${me.pinnedSubjects.toList()} widgets=${me.asideWidgets.size}")

        val trends = Live.saypip.feed().trends(FeedTrendsRequest()).data
        println("TRENDS measuredAt=${trends.measuredAt} items=${trends.items.size}")
    }

    @Test
    fun testFeedAndPaging(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val first = Live.saypip.feed().feed(FeedFeedRequest().also { it.limit = 5 }).data
        println("FEED count=${first.items.size} next=${first.nextCursor != null}")

        first.items.firstOrNull()?.let { post ->
            println("POST id=${post.id} body=${post.body.take(40)} " +
                "media=${post.media.size} reactions=${post.reactions.size} " +
                "talk=${post.wantsTalk} mine=${post.isMine}")
        }

        if (first.nextCursor != null) {
            val second = Live.saypip.feed().feed(
                FeedFeedRequest().also {
                    it.limit = 5
                    it.cursor = first.nextCursor
                },
            ).data
            println("FEED page2 count=${second.items.size} next=${second.nextCursor != null}")
        }
    }

    @Test
    fun testTalkAndFriendsFeeds(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val talk = Live.saypip.feed().talk(FeedTalkRequest()).data
        println("TALK count=${talk.items.size}")

        val friends = Live.saypip.feed().friends(FeedFriendsRequest()).data
        println("FRIENDS count=${friends.items.size}")
    }

    @Test
    fun testSearchAndTag(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val search = Live.saypip.feed().search(
            FeedSearchRequest().also {
                it.q = "テス"
                it.limit = 5
            },
        ).data
        println("SEARCH count=${search.items.size}")
        search.items.take(3).forEach { println("  hit=${it.body.take(30)}") }

        val tag = Live.saypip.feed().tag(
            FeedTagRequest().also {
                it.tag = "猫"
                it.limit = 5
            },
        ).data
        println("TAG 猫 count=${tag.items.size}")
        assertNotNull(tag.items)
    }

    @Test
    fun testSinglePostGraph(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val feed = Live.saypip.feed().feed(FeedFeedRequest().also { it.limit = 20 }).data

        // A post fetched on its own resolves its author for a signed-in viewer; one of ours has
        // no author to resolve, so somebody else's is the interesting row.
        val post = feed.items.firstOrNull { !it.isMine } ?: feed.items.firstOrNull()
            ?: return@runBlocking

        val single = Live.saypip.posts().post(
            PostsPostRequest().also { it.postId = post.id },
        ).data
        println("SINGLE id=${single.id} mine=${single.isMine} author=${single.author?.identity} " +
            "color=${single.authorColor} readable=${single.readableUntil != null}")

        val reactions = Live.saypip.posts().reactions(
            PostsReactionsRequest().also { it.postId = post.id },
        ).data
        println("REACTORS bars=${reactions.reactions.size} " +
            reactions.reactions.joinToString { "${it.emoji}=${it.count}/people:${it.people.size}" })

        val conversations = Live.saypip.posts().conversations(
            PostsConversationsRequest().also { it.postId = post.id },
        ).data
        println("CONVERSATIONS count=${conversations.items.size}")

        single.author?.let { person ->
            dumpUserPage(person.identity)
        }

        // The viewer's relationships carry an identity for each counterpart, which is the other
        // way a user page is reached.
        val relationships = Live.saypip.relationships().list(RelationshipsListRequest()).data
        relationships.items.firstOrNull()?.let { relationship ->
            dumpUserPage(relationship.counterpart.identity)
        }
    }

    private suspend fun dumpUserPage(identity: String) {
        val page = Live.saypip.users().user(
            UsersUserRequest().also {
                it.identityToken = identity
                it.limit = 5
            },
        ).data
        println("USERPAGE label=${page.person.label} posts=${page.posts.size} " +
            "next=${page.postsNextCursor != null} " +
            "friend=${page.relationship?.friendSince != null}")
    }

    @Test
    fun testRelationshipsAndRequests(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val relationships = Live.saypip.relationships().list(RelationshipsListRequest()).data
        println("RELATIONSHIPS count=${relationships.items.size}")
        relationships.items.take(3).forEach {
            println("  rel=${it.id} counterpart=${it.counterpart.identity.take(12)}… " +
                "friend=${it.friendSince != null}")
        }

        val requests = Live.saypip.friendRequests().list(FriendRequestsListRequest()).data
        println("FRIEND-REQUESTS count=${requests.items.size}")
    }

    @Test
    fun testNotifications(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val list = Live.saypip.notifications().list(
            NotificationsListRequest().also { it.limit = 10 },
        ).data
        println("NOTIFICATIONS count=${list.items.size} next=${list.nextCursor != null}")
        list.items.forEach {
            println("  kind=${it.kind} person=${it.person?.identity?.take(12)} " +
                "post=${it.postId} conv=${it.conversationId} read=${it.readAt != null}")
        }
    }

    @Test
    fun testMePostsAndApps(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val posts = Live.saypip.me().posts(MePostsRequest().also { it.limit = 5 }).data
        println("ME-POSTS count=${posts.items.size} next=${posts.nextCursor != null}")

        val apps = Live.saypip.apps().list(AppsListRequest()).data
        println("APPS count=${apps.items.size}")
        apps.items.forEach {
            println("  app=${it.name} scopes=${it.scopes.toList()}")
        }
    }

    @Test
    fun testMutesAndWordMutes(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val mutes = Live.saypip.mutes().list(MutesListRequest()).data
        println("MUTES count=${mutes.items.size}")

        val wordMutes = Live.saypip.wordMutes().list(WordMutesListRequest()).data
        println("WORD-MUTES count=${wordMutes.items.size}")
        wordMutes.items.take(3).forEach {
            println("  word=${it.word} active=${it.active} endsAt=${it.endsAt}")
        }
    }

    @Test
    fun testConversations(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val list = Live.saypip.conversations().list(
            ConversationsListRequest().also { it.limit = 5 },
        ).data
        println("CONVERSATION-LIST count=${list.items.size}")

        list.items.firstOrNull()?.let { digest ->
            val conversation = Live.saypip.conversations().conversation(
                ConversationsConversationRequest().also {
                    it.conversationId = digest.id
                    it.limit = 10
                },
            ).data
            println("CONVERSATION id=${conversation.id} replies=${conversation.replies.size} " +
                "canReply=${conversation.canReply} older=${conversation.olderRepliesCursor != null}")
        }
    }

    @Test
    fun testLinksPreview(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val preview = Live.saypip.links().preview(
            LinksPreviewRequest().also { it.url = "https://example.com/" },
        ).data
        println("LINK title=${preview.title} description=${preview.description} " +
            "image=${preview.imageUrl != null}")
    }

    @Test
    fun testLinkImage(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        // A far-side picture travels through the Worker: this page's card image is on GitHub's
        // host, so the bytes come back from this origin.
        val url = "https://github.com/uakihir0/ksaypip"
        val preview = Live.saypip.links().preview(
            LinksPreviewRequest().also { it.url = url },
        ).data
        println("LINK-IMAGE preview title=${preview.title} image=${preview.imageUrl}")

        if (preview.imageUrl != null) {
            val bytes = Live.saypip.links().image(
                LinksImageRequest().also { it.url = url },
            ).data
            assertTrue(bytes.isNotEmpty())
            println("LINK-IMAGE bytes=${bytes.size}")
        }
    }

    @Test
    fun testNotFoundIsTyped(): Unit = runBlocking {
        if (!Live.enabled) return@runBlocking

        val exception = assertFailsWith<SaypipException> {
            Live.saypip.posts().post(
                PostsPostRequest().also { it.postId = "p_does_not_exist" },
            )
        }
        println("NOT-FOUND status=${exception.status} code=${exception.code}")
        assertNotNull(exception.code)
    }
}
