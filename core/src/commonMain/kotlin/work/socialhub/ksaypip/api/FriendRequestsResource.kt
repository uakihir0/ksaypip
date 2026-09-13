package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsAcceptRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsDeclineRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsListRequest
import work.socialhub.ksaypip.api.request.friendrequests.FriendRequestsRequestRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsAcceptResponse
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsListResponse
import work.socialhub.ksaypip.api.response.friendrequests.FriendRequestsRequestResponse
import kotlin.js.JsExport

@JsExport
interface FriendRequestsResource {

    /**
     * Incoming and outgoing pending friend requests.
     */
    suspend fun list(request: FriendRequestsListRequest): Response<FriendRequestsListResponse>

    @JsExport.Ignore
    fun listBlocking(request: FriendRequestsListRequest): Response<FriendRequestsListResponse>

    /**
     * Ask to become friends. Requires a conversation threshold.
     */
    suspend fun request(request: FriendRequestsRequestRequest): Response<FriendRequestsRequestResponse>

    @JsExport.Ignore
    fun requestBlocking(request: FriendRequestsRequestRequest): Response<FriendRequestsRequestResponse>

    /**
     * Accept a friend request. Discloses both profiles, but never retroactively.
     */
    suspend fun accept(request: FriendRequestsAcceptRequest): Response<FriendRequestsAcceptResponse>

    @JsExport.Ignore
    fun acceptBlocking(request: FriendRequestsAcceptRequest): Response<FriendRequestsAcceptResponse>

    /**
     * Decline a friend request. The counterpart is not told.
     */
    suspend fun decline(request: FriendRequestsDeclineRequest): ResponseUnit

    @JsExport.Ignore
    fun declineBlocking(request: FriendRequestsDeclineRequest): ResponseUnit
}
