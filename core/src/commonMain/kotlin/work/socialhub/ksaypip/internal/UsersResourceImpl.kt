package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.UsersResource
import work.socialhub.ksaypip.api.request.users.UsersUserRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.users.UsersUserResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class UsersResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    UsersResource {

    override suspend fun user(request: UsersUserRequest): Response<UsersUserResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/users/${urlEncode(request.identityToken.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun userBlocking(request: UsersUserRequest): Response<UsersUserResponse> {
        return toBlocking { user(request) }
    }
}
