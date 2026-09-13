package work.socialhub.ksaypip.internal

import work.socialhub.ksaypip.Saypip
import work.socialhub.ksaypip.api.AppsResource
import work.socialhub.ksaypip.api.BlocksResource
import work.socialhub.ksaypip.api.ConversationsResource
import work.socialhub.ksaypip.api.FeedResource
import work.socialhub.ksaypip.api.FeedbackResource
import work.socialhub.ksaypip.api.FriendRequestsResource
import work.socialhub.ksaypip.api.LinksResource
import work.socialhub.ksaypip.api.MeResource
import work.socialhub.ksaypip.api.MediaResource
import work.socialhub.ksaypip.api.MutesResource
import work.socialhub.ksaypip.api.NotificationsResource
import work.socialhub.ksaypip.api.PostsResource
import work.socialhub.ksaypip.api.RelationshipsResource
import work.socialhub.ksaypip.api.ReportsResource
import work.socialhub.ksaypip.api.UsersResource
import work.socialhub.ksaypip.api.WordMutesResource

class SaypipImpl(
    private val uri: String,
    private val accessToken: String,
) : Saypip {

    private val feed: FeedResource = FeedResourceImpl(uri, accessToken)
    private val posts: PostsResource = PostsResourceImpl(uri, accessToken)
    private val conversations: ConversationsResource = ConversationsResourceImpl(uri, accessToken)
    private val users: UsersResource = UsersResourceImpl(uri, accessToken)
    private val relationships: RelationshipsResource = RelationshipsResourceImpl(uri, accessToken)
    private val friendRequests: FriendRequestsResource = FriendRequestsResourceImpl(uri, accessToken)
    private val notifications: NotificationsResource = NotificationsResourceImpl(uri, accessToken)

    private val mutes: MutesResource = MutesResourceImpl(uri, accessToken)
    private val wordMutes: WordMutesResource = WordMutesResourceImpl(uri, accessToken)

    private val media: MediaResource = MediaResourceImpl(uri, accessToken)
    private val links: LinksResource = LinksResourceImpl(uri, accessToken)

    private val blocks: BlocksResource = BlocksResourceImpl(uri, accessToken)
    private val reports: ReportsResource = ReportsResourceImpl(uri, accessToken)
    private val feedback: FeedbackResource = FeedbackResourceImpl(uri, accessToken)

    private val me: MeResource = MeResourceImpl(uri, accessToken)
    private val apps: AppsResource = AppsResourceImpl(uri, accessToken)

    override fun feed() = feed
    override fun posts() = posts
    override fun conversations() = conversations
    override fun users() = users
    override fun relationships() = relationships
    override fun friendRequests() = friendRequests
    override fun notifications() = notifications

    override fun mutes() = mutes
    override fun wordMutes() = wordMutes

    override fun media() = media
    override fun links() = links

    override fun blocks() = blocks
    override fun reports() = reports
    override fun feedback() = feedback

    override fun me() = me
    override fun apps() = apps

    override fun uri() = uri

    override fun accessToken() = accessToken
}
