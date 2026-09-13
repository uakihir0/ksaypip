package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.BlocksResource
import work.socialhub.ksaypip.api.request.blocks.BlocksBlockRequest
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class BlocksResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    BlocksResource {

    override suspend fun block(request: BlocksBlockRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/blocks")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("identity", request.identity)
                }
                .post()
        }
    }

    override fun blockBlocking(request: BlocksBlockRequest): ResponseUnit {
        return toBlocking { block(request) }
    }
}
