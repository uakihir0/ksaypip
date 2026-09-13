package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.reports.ReportsReportRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.reports.ReportsReportResponse
import kotlin.js.JsExport

@JsExport
interface ReportsResource {

    /**
     * Report, optionally with evidence. The answer carries the report's own ID.
     */
    suspend fun report(request: ReportsReportRequest): Response<ReportsReportResponse>

    @JsExport.Ignore
    fun reportBlocking(request: ReportsReportRequest): Response<ReportsReportResponse>
}
