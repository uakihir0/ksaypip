package work.socialhub.ksaypip.internal

import work.socialhub.ksaypip.api.FeedbackResource
import work.socialhub.ksaypip.api.request.feedback.FeedbackSendRequest
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class FeedbackResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    FeedbackResource {

    override suspend fun send(request: FeedbackSendRequest): ResponseUnit {
        return proceedUnit {
            val form = buildMap {
                request.message?.let { put("message", it) }
                request.contact?.let { put("contact", it) }
                request.route?.let { put("route", it) }
            }

            // A screenshot is re-encoded by the server and stored nowhere; its part carries the
            // type the bytes are, which is the other request RawRequest exists for.
            RawRequest.postMultipart(
                url = "${uri}/api/feedback",
                accessToken = accessToken,
                params = form,
                fileKey = if (request.image != null) "image" else null,
                fileName = if (request.image != null) "screenshot.webp" else null,
                fileContentType = if (request.image != null) MediaType.WEBP else null,
                fileBytes = request.image,
            )
        }
    }

    override fun sendBlocking(request: FeedbackSendRequest): ResponseUnit {
        return toBlocking { send(request) }
    }
}
