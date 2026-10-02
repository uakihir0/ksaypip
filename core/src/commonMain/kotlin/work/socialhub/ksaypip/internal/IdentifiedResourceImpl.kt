package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.IdentifiedResource
import work.socialhub.ksaypip.api.request.identified.IdentifiedPageRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.identified.IdentifiedPageResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class IdentifiedResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    IdentifiedResource {

    override suspend fun page(request: IdentifiedPageRequest): Response<IdentifiedPageResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/identified/${urlEncode(request.handle.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun pageBlocking(request: IdentifiedPageRequest): Response<IdentifiedPageResponse> {
        return toBlocking { page(request) }
    }
}
