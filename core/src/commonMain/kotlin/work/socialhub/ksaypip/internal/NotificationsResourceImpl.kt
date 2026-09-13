package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.NotificationsResource
import work.socialhub.ksaypip.api.request.notifications.NotificationsListRequest
import work.socialhub.ksaypip.api.request.notifications.NotificationsReadRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.notifications.NotificationsListResponse
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class NotificationsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    NotificationsResource {

    override suspend fun list(request: NotificationsListRequest): Response<NotificationsListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/notifications")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun listBlocking(request: NotificationsListRequest): Response<NotificationsListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun read(request: NotificationsReadRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/notifications/read")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .post()
        }
    }

    override fun readBlocking(request: NotificationsReadRequest): ResponseUnit {
        return toBlocking { read(request) }
    }
}
