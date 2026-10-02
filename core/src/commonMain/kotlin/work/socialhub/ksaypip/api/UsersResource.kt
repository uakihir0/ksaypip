package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.users.UsersSetLabelRequest
import work.socialhub.ksaypip.api.request.users.UsersUserRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.users.UsersSetLabelResponse
import work.socialhub.ksaypip.api.response.users.UsersUserResponse
import kotlin.js.JsExport

@JsExport
interface UsersResource {

    /**
     * A user page as seen by this viewer: the person, a page of their posts, and the
     * relationship where there is one.
     */
    suspend fun user(request: UsersUserRequest): Response<UsersUserResponse>

    @JsExport.Ignore
    fun userBlocking(request: UsersUserRequest): Response<UsersUserResponse>

    /**
     * Replace the viewer's local label, note and mark, addressed by the identity token the caller
     * holds. No conversation is required, and the write is a replacement rather than a patch.
     */
    suspend fun setLabel(request: UsersSetLabelRequest): Response<UsersSetLabelResponse>

    @JsExport.Ignore
    fun setLabelBlocking(request: UsersSetLabelRequest): Response<UsersSetLabelResponse>
}
