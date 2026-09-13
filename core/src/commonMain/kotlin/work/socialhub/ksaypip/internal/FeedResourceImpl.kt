package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.FeedResource
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
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class FeedResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    FeedResource {

    override suspend fun feed(request: FeedFeedRequest): Response<FeedFeedResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/feed")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun feedBlocking(request: FeedFeedRequest): Response<FeedFeedResponse> {
        return toBlocking { feed(request) }
    }

    override suspend fun talk(request: FeedTalkRequest): Response<FeedTalkResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/feed/talk")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun talkBlocking(request: FeedTalkRequest): Response<FeedTalkResponse> {
        return toBlocking { talk(request) }
    }

    override suspend fun friends(request: FeedFriendsRequest): Response<FeedFriendsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/feed/friends")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun friendsBlocking(request: FeedFriendsRequest): Response<FeedFriendsResponse> {
        return toBlocking { friends(request) }
    }

    override suspend fun search(request: FeedSearchRequest): Response<FeedSearchResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/search")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .qwn("q", request.q)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun searchBlocking(request: FeedSearchRequest): Response<FeedSearchResponse> {
        return toBlocking { search(request) }
    }

    override suspend fun tag(request: FeedTagRequest): Response<FeedTagResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/tags/${urlEncode(request.tag.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun tagBlocking(request: FeedTagRequest): Response<FeedTagResponse> {
        return toBlocking { tag(request) }
    }

    override suspend fun trends(request: FeedTrendsRequest): Response<FeedTrendsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/trends")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun trendsBlocking(request: FeedTrendsRequest): Response<FeedTrendsResponse> {
        return toBlocking { trends(request) }
    }
}
