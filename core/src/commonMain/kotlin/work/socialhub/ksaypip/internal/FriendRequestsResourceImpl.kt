package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.FriendRequestsResource
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsAcceptRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsDeclineRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsListRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsRequestRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsAcceptResponse
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsListResponse
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsRequestResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class FriendRequestsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    FriendRequestsResource {

    override suspend fun list(request: FriendRequestsListRequest): Response<FriendRequestsListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/friend-requests")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun listBlocking(request: FriendRequestsListRequest): Response<FriendRequestsListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun request(
        request: FriendRequestsRequestRequest,
    ): Response<FriendRequestsRequestResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/friend-requests")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("identity", request.identity)
                }
                .post()
        }
    }

    override fun requestBlocking(request: FriendRequestsRequestRequest): Response<FriendRequestsRequestResponse> {
        return toBlocking { request(request) }
    }

    override suspend fun accept(request: FriendRequestsAcceptRequest): Response<FriendRequestsAcceptResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/friend-requests/${urlEncode(request.friendRequestId.orEmpty())}/accept")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .post()
        }
    }

    override fun acceptBlocking(request: FriendRequestsAcceptRequest): Response<FriendRequestsAcceptResponse> {
        return toBlocking { accept(request) }
    }

    override suspend fun decline(request: FriendRequestsDeclineRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/friend-requests/${urlEncode(request.friendRequestId.orEmpty())}/decline")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .post()
        }
    }

    override fun declineBlocking(request: FriendRequestsDeclineRequest): ResponseUnit {
        return toBlocking { decline(request) }
    }
}
