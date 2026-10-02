package work.socialhub.ksaypip.internal

import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.RelationshipsResource
import work.socialhub.ksaypip.api.request.relationships.RelationshipsListRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsRelationshipRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsSetLabelRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsTerminateRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.relationships.RelationshipsListResponse
import work.socialhub.ksaypip.api.response.relationships.RelationshipsRelationshipResponse
import work.socialhub.ksaypip.api.response.relationships.RelationshipsSetLabelResponse
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class RelationshipsResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    RelationshipsResource {

    override suspend fun list(request: RelationshipsListRequest): Response<RelationshipsListResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/relationships")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun listBlocking(request: RelationshipsListRequest): Response<RelationshipsListResponse> {
        return toBlocking { list(request) }
    }

    override suspend fun relationship(
        request: RelationshipsRelationshipRequest,
    ): Response<RelationshipsRelationshipResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/relationships/${urlEncode(request.relationshipId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun relationshipBlocking(
        request: RelationshipsRelationshipRequest,
    ): Response<RelationshipsRelationshipResponse> {
        return toBlocking { relationship(request) }
    }

    override suspend fun setLabel(request: RelationshipsSetLabelRequest): Response<RelationshipsSetLabelResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/relationships/${urlEncode(request.relationshipId.orEmpty())}/label")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .labelBody(request.label, request.note, request.markEmoji, request.markColors)
                .put()
        }
    }

    override fun setLabelBlocking(request: RelationshipsSetLabelRequest): Response<RelationshipsSetLabelResponse> {
        return toBlocking { setLabel(request) }
    }

    override suspend fun terminate(request: RelationshipsTerminateRequest): ResponseUnit {
        return proceedUnit {
            HttpRequest()
                .url("${uri}/api/relationships/${urlEncode(request.relationshipId.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .idempotency(request.idempotencyKey)
                .delete()
        }
    }

    override fun terminateBlocking(request: RelationshipsTerminateRequest): ResponseUnit {
        return toBlocking { terminate(request) }
    }
}
