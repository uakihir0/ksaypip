package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.mutes.MutesListRequest
import work.socialhub.ksaypip.api.request.mutes.MutesMuteRequest
import work.socialhub.ksaypip.api.request.mutes.MutesUnmuteRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.mutes.MutesListResponse
import kotlin.js.JsExport

@JsExport
interface MutesResource {

    /**
     * The people this reader is hiding, newest mute first.
     */
    suspend fun list(request: MutesListRequest): Response<MutesListResponse>

    @JsExport.Ignore
    fun listBlocking(request: MutesListRequest): Response<MutesListResponse>

    /**
     * Hide somebody, by identity or by one of their posts, for a window.
     */
    suspend fun mute(request: MutesMuteRequest): ResponseUnit

    @JsExport.Ignore
    fun muteBlocking(request: MutesMuteRequest): ResponseUnit

    /**
     * Stop hiding them. A real delete, idempotent.
     */
    suspend fun unmute(request: MutesUnmuteRequest): ResponseUnit

    @JsExport.Ignore
    fun unmuteBlocking(request: MutesUnmuteRequest): ResponseUnit
}
