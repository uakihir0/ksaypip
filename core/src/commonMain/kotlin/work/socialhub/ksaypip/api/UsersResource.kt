package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.users.UsersUserRequest
import work.socialhub.ksaypip.api.response.Response
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
}
