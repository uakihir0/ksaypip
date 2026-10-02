package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.WatchesResource
import work.socialhub.ksaypip.api.request.watches.WatchesListRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesUnwatchRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchIdentifiedRequest
import work.socialhub.ksaypip.api.request.watches.WatchesWatchRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.watches.WatchesListResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class WatchesResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    WatchesResource {

    override suspend fun list(request: WatchesListRequest): Response<WatchesListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/watches")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun listBlocking(request: WatchesListRequest): Response<WatchesListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun watch(request: WatchesWatchRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/watches")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("identity", request.identity)
                }
                .post()
        }
    }

    override fun watchBlocking(request: WatchesWatchRequest): ResponseUnit {
        return toBlocking { watch(request) }
    }

    override suspend fun unwatch(request: WatchesUnwatchRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/watches/${urlEncode(request.identityToken.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unwatchBlocking(request: WatchesUnwatchRequest): ResponseUnit {
        return toBlocking { unwatch(request) }
    }

    override suspend fun watchIdentified(request: WatchesWatchIdentifiedRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/watches/identified")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .jsonBody {
                    putOrNull("handle", request.handle)
                }
                .post()
        }
    }

    override fun watchIdentifiedBlocking(request: WatchesWatchIdentifiedRequest): ResponseUnit {
        return toBlocking { watchIdentified(request) }
    }

    override suspend fun unwatchIdentified(request: WatchesUnwatchIdentifiedRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/watches/identified/${urlEncode(request.handle.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unwatchIdentifiedBlocking(request: WatchesUnwatchIdentifiedRequest): ResponseUnit {
        return toBlocking { unwatchIdentified(request) }
    }
}
