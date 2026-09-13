package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.links.LinksImageRequest
import work.socialhub.ksaypip.api.request.links.LinksPreviewRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.links.LinksImageResponse
import work.socialhub.ksaypip.api.response.links.LinksPreviewResponse
import kotlin.js.JsExport

@JsExport
interface LinksResource {

    /**
     * What an address in a post leads to. All-null fields are an ordinary answer.
     */
    suspend fun preview(request: LinksPreviewRequest): Response<LinksPreviewResponse>

    @JsExport.Ignore
    fun previewBlocking(request: LinksPreviewRequest): Response<LinksPreviewResponse>

    /**
     * The card's picture, by page address: the only public, cacheable answer here.
     */
    suspend fun image(request: LinksImageRequest): Response<LinksImageResponse>

    @JsExport.Ignore
    fun imageBlocking(request: LinksImageRequest): Response<LinksImageResponse>
}
