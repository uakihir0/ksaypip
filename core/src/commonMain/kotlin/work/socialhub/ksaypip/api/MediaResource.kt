package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.media.MediaBytesRequest
import work.socialhub.ksaypip.api.request.media.MediaSetAltRequest
import work.socialhub.ksaypip.api.request.media.MediaUploadRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.media.MediaBytesResponse
import work.socialhub.ksaypip.api.response.media.MediaSetAltResponse
import work.socialhub.ksaypip.api.response.media.MediaUploadResponse
import kotlin.js.JsExport

@JsExport
interface MediaResource {

    /**
     * Upload, transcoded on the server, before attaching to a post.
     */
    suspend fun upload(request: MediaUploadRequest): Response<MediaUploadResponse>

    @JsExport.Ignore
    fun uploadBlocking(request: MediaUploadRequest): Response<MediaUploadResponse>

    /**
     * What the picture shows, until it is attached to a post.
     */
    suspend fun setAlt(request: MediaSetAltRequest): Response<MediaSetAltResponse>

    @JsExport.Ignore
    fun setAltBlocking(request: MediaSetAltRequest): Response<MediaSetAltResponse>

    /**
     * Media bytes, re-checking visibility on every fetch.
     */
    suspend fun bytes(request: MediaBytesRequest): Response<MediaBytesResponse>

    @JsExport.Ignore
    fun bytesBlocking(request: MediaBytesRequest): Response<MediaBytesResponse>
}
