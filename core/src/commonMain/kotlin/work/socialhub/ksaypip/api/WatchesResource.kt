package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.watches.WatchesListRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.watches.WatchesListResponse
import kotlin.js.JsExport

@JsExport
interface WatchesResource {

    /**
     * The people the caller is keeping, newest first: the viewer's own label and mark for an
     * anonymous entry, the public handle and badge for an identified one.
     */
    suspend fun list(request: WatchesListRequest): Response<WatchesListResponse>

    @JsExport.Ignore
    fun listBlocking(request: WatchesListRequest): Response<WatchesListResponse>

    /**
     * Keep this person's anonymous writing in the connections timeline. Idempotent.
     */
    suspend fun watch(request: WatchesWatchRequest): ResponseUnit

    @JsExport.Ignore
    fun watchBlocking(request: WatchesWatchRequest): ResponseUnit

    /**
     * Stop keeping this person's anonymous writing.
     */
    suspend fun unwatch(request: WatchesUnwatchRequest): ResponseUnit

    @JsExport.Ignore
    fun unwatchBlocking(request: WatchesUnwatchRequest): ResponseUnit

    /**
     * Keep this identified persona's writing in the connections timeline. Idempotent, and never
     * touches the anonymous row.
     */
    suspend fun watchIdentified(request: WatchesWatchIdentifiedRequest): ResponseUnit

    @JsExport.Ignore
    fun watchIdentifiedBlocking(request: WatchesWatchIdentifiedRequest): ResponseUnit

    /**
     * Stop keeping an identified persona's writing.
     */
    suspend fun unwatchIdentified(request: WatchesUnwatchIdentifiedRequest): ResponseUnit

    @JsExport.Ignore
    fun unwatchIdentifiedBlocking(request: WatchesUnwatchIdentifiedRequest): ResponseUnit
}
