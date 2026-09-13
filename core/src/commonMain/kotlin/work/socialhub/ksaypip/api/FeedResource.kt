package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.feed.FeedFeedRequest
import work.socialhub.ksaypip.api.request.feed.FeedFriendsRequest
import work.socialhub.ksaypip.api.request.feed.FeedSearchRequest
import work.socialhub.ksaypip.api.request.feed.FeedTagRequest
import work.socialhub.ksaypip.api.request.feed.FeedTalkRequest
import work.socialhub.ksaypip.api.request.feed.FeedTrendsRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.feed.FeedFeedResponse
import work.socialhub.ksaypip.api.response.feed.FeedFriendsResponse
import work.socialhub.ksaypip.api.response.feed.FeedSearchResponse
import work.socialhub.ksaypip.api.response.feed.FeedTagResponse
import work.socialhub.ksaypip.api.response.feed.FeedTalkResponse
import work.socialhub.ksaypip.api.response.feed.FeedTrendsResponse
import kotlin.js.JsExport

@JsExport
interface FeedResource {

    /**
     * The Global Room, paged; 7-day window, block-filtered.
     */
    suspend fun feed(request: FeedFeedRequest): Response<FeedFeedResponse>

    @JsExport.Ignore
    fun feedBlocking(request: FeedFeedRequest): Response<FeedFeedResponse>

    /**
     * The feed narrowed to the posts whose author is asking to be talked to.
     */
    suspend fun talk(request: FeedTalkRequest): Response<FeedTalkResponse>

    @JsExport.Ignore
    fun talkBlocking(request: FeedTalkRequest): Response<FeedTalkResponse>

    /**
     * Your friends' posts, paged; past the window, and only since each friendship.
     */
    suspend fun friends(request: FeedFriendsRequest): Response<FeedFriendsResponse>

    @JsExport.Ignore
    fun friendsBlocking(request: FeedFriendsRequest): Response<FeedFriendsResponse>

    /**
     * The feed filtered by a phrase; the same window and filters, no author narrowing.
     */
    suspend fun search(request: FeedSearchRequest): Response<FeedSearchResponse>

    @JsExport.Ignore
    fun searchBlocking(request: FeedSearchRequest): Response<FeedSearchResponse>

    /**
     * The feed filtered to one hashtag, without the `#`.
     */
    suspend fun tag(request: FeedTagRequest): Response<FeedTagResponse>

    @JsExport.Ignore
    fun tagBlocking(request: FeedTagRequest): Response<FeedTagResponse>

    /**
     * The last trend measurement. Not paged.
     */
    suspend fun trends(request: FeedTrendsRequest): Response<FeedTrendsResponse>

    @JsExport.Ignore
    fun trendsBlocking(request: FeedTrendsRequest): Response<FeedTrendsResponse>
}
