package work.socialhub.ksaypip

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
import kotlin.js.JsExport

/**
 * The Saypip API, one deployment and one reader's view of it.
 *
 * Every read and write here is a bearer-token call, so an application is expected to have been
 * authorized through the `auth` module and to hold an access token. Nothing on this interface can
 * reach the cookie-only blocks — the admin area, `GET /me/sign-in`, `DELETE /me`, the push
 * endpoints and the realtime socket — because a token reaches none of them.
 */
@JsExport
interface Saypip {

    fun feed(): FeedResource
    fun posts(): PostsResource
    fun conversations(): ConversationsResource
    fun users(): UsersResource
    fun relationships(): RelationshipsResource
    fun friendRequests(): FriendRequestsResource
    fun notifications(): NotificationsResource

    fun mutes(): MutesResource
    fun wordMutes(): WordMutesResource

    fun media(): MediaResource
    fun links(): LinksResource

    fun blocks(): BlocksResource
    fun reports(): ReportsResource
    fun feedback(): FeedbackResource

    fun me(): MeResource
    fun apps(): AppsResource

    fun uri(): String

    fun accessToken(): String
}
