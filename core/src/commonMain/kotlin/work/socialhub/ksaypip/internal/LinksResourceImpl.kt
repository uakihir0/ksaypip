package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.LinksResource
import work.socialhub.ksaypip.api.request.links.LinksImageRequest
import work.socialhub.ksaypip.api.request.links.LinksPreviewRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.links.LinksImageResponse
import work.socialhub.ksaypip.api.response.links.LinksPreviewResponse
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class LinksResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    LinksResource {

    override suspend fun preview(request: LinksPreviewRequest): Response<LinksPreviewResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/links")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .qwn("url", request.url)
                .get()
        }
    }

    override fun previewBlocking(request: LinksPreviewRequest): Response<LinksPreviewResponse> {
        return toBlocking { preview(request) }
    }

    override suspend fun image(request: LinksImageRequest): Response<LinksImageResponse> {
        return proceedBytes {
            HttpRequest()
                .url("${uri}/api/links/image")
                .header(AUTHORIZATION, bearerToken())
                .qwn("url", request.url)
                .get()
        }
    }

    override fun imageBlocking(request: LinksImageRequest): Response<LinksImageResponse> {
        return toBlocking { image(request) }
    }
}
