package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.wordmutes.WordMutesForgetRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesListRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesMuteRequest
import work.socialhub.ksaypip.api.request.wordmutes.WordMutesWindowRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.wordmutes.WordMutesListResponse
import kotlin.js.JsExport

@JsExport
interface WordMutesResource {

    /**
     * The words this reader is hiding, spent ones included.
     */
    suspend fun list(request: WordMutesListRequest): Response<WordMutesListResponse>

    @JsExport.Ignore
    fun listBlocking(request: WordMutesListRequest): Response<WordMutesListResponse>

    /**
     * Hide the writing that says one word, for a window.
     */
    suspend fun mute(request: WordMutesMuteRequest): ResponseUnit

    @JsExport.Ignore
    fun muteBlocking(request: WordMutesMuteRequest): ResponseUnit

    /**
     * Replace the window of an existing word mute, waking a spent one.
     */
    suspend fun setWindow(request: WordMutesWindowRequest): ResponseUnit

    @JsExport.Ignore
    fun setWindowBlocking(request: WordMutesWindowRequest): ResponseUnit

    /**
     * Forget one word mute. A real delete, idempotent.
     */
    suspend fun forget(request: WordMutesForgetRequest): ResponseUnit

    @JsExport.Ignore
    fun forgetBlocking(request: WordMutesForgetRequest): ResponseUnit
}
