package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.AppsResource
import work.socialhub.ksaypip.api.request.apps.AppsListRequest
import work.socialhub.ksaypip.api.request.apps.AppsRevokeRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.apps.AppsListResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class AppsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    AppsResource {

    override suspend fun list(request: AppsListRequest): Response<AppsListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/me/apps")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun listBlocking(request: AppsListRequest): Response<AppsListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun revoke(request: AppsRevokeRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/me/apps/${urlEncode(request.consentId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .delete()
        }
    }

    override fun revokeBlocking(request: AppsRevokeRequest): ResponseUnit {
        return toBlocking { revoke(request) }
    }
}
