package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.notifications.NotificationsListRequest
import work.socialhub.ksaypip.api.request.notifications.NotificationsReadRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.notifications.NotificationsListResponse
import kotlin.js.JsExport

@JsExport
interface NotificationsResource {

    /**
     * What happened to you, paged; newest arrival first.
     */
    suspend fun list(request: NotificationsListRequest): Response<NotificationsListResponse>

    @JsExport.Ignore
    fun listBlocking(request: NotificationsListRequest): Response<NotificationsListResponse>

    /**
     * Mark the reaction lines read. Reply lines are read with their conversations.
     */
    suspend fun read(request: NotificationsReadRequest): ResponseUnit

    @JsExport.Ignore
    fun readBlocking(request: NotificationsReadRequest): ResponseUnit
}
