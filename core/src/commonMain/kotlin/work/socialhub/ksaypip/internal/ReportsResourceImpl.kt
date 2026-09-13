package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.ReportsResource
import work.socialhub.ksaypip.api.request.reports.ReportsReportRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.reports.ReportsReportResponse
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class ReportsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    ReportsResource {

    override suspend fun report(request: ReportsReportRequest): Response<ReportsReportResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/reports")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("targetType", request.targetType)
                    putOrNull("targetId", request.targetId)
                    putOrNull("reason", request.reason)
                    request.alsoBlock?.let { put("alsoBlock", it) }
                }
                .post()
        }
    }

    override fun reportBlocking(request: ReportsReportRequest): Response<ReportsReportResponse> {
        return toBlocking { report(request) }
    }
}
