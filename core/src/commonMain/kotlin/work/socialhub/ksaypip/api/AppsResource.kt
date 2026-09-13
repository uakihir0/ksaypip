package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.apps.AppsListRequest
import work.socialhub.ksaypip.api.request.apps.AppsRevokeRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.apps.AppsListResponse
import kotlin.js.JsExport

@JsExport
interface AppsResource {

    /**
     * The applications this reader has authorized, with what each may do.
     */
    suspend fun list(request: AppsListRequest): Response<AppsListResponse>

    @JsExport.Ignore
    fun listBlocking(request: AppsListRequest): Response<AppsListResponse>

    /**
     * End one application's access: consent and tokens go together.
     */
    suspend fun revoke(request: AppsRevokeRequest): ResponseUnit

    @JsExport.Ignore
    fun revokeBlocking(request: AppsRevokeRequest): ResponseUnit
}
