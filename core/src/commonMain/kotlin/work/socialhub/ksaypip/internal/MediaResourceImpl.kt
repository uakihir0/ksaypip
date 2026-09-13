package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.SaypipException
import work.socialhub.ksaypip.api.MediaResource
import work.socialhub.ksaypip.api.request.media.MediaBytesRequest
import work.socialhub.ksaypip.api.request.media.MediaSetAltRequest
import work.socialhub.ksaypip.api.request.media.MediaUploadRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.media.MediaBytesResponse
import work.socialhub.ksaypip.api.response.media.MediaSetAltResponse
import work.socialhub.ksaypip.api.response.media.MediaUploadResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class MediaResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    MediaResource {

    override suspend fun upload(request: MediaUploadRequest): Response<MediaUploadResponse> {
        return proceed {
            val data = request.data
                ?: throw SaypipException("MediaUploadRequest.data is required.")
            val contentType = request.contentType
                ?: throw SaypipException("MediaUploadRequest.contentType is required.")

            // Raw bytes and the content type the caller named: one request the shared HTTP helper
            // cannot express (RawRequest says why).
            RawRequest.postBytes(
                url = "${uri}/api/media",
                accessToken = accessToken,
                contentType = contentType,
                bytes = data,
            )
        }
    }

    override fun uploadBlocking(request: MediaUploadRequest): Response<MediaUploadResponse> {
        return toBlocking { upload(request) }
    }

    override suspend fun setAlt(request: MediaSetAltRequest): Response<MediaSetAltResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/media/${urlEncode(request.mediaId.orEmpty())}/alt")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .jsonBody {
                    put("alt", request.alt?.let { kotlinx.serialization.json.JsonPrimitive(it) } ?: JsonNull)
                }
                .put()
        }
    }

    override fun setAltBlocking(request: MediaSetAltRequest): Response<MediaSetAltResponse> {
        return toBlocking { setAlt(request) }
    }

    override suspend fun bytes(request: MediaBytesRequest): Response<MediaBytesResponse> {
        return proceedBytes {
            HttpRequest()
                .url("${uri}/api/media/${urlEncode(request.mediaId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .qwn("variant", request.variant)
                .get()
        }
    }

    override fun bytesBlocking(request: MediaBytesRequest): Response<MediaBytesResponse> {
        return toBlocking { bytes(request) }
    }
}
