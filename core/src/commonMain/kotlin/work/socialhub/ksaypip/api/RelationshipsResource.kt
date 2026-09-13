package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.relationships.RelationshipsListRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsRelationshipRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsSetLabelRequest
import work.socialhub.ksaypip.api.request.relationships.RelationshipsTerminateRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.ResponseUnit
import work.socialhub.ksaypip.api.response.relationships.RelationshipsListResponse
import work.socialhub.ksaypip.api.response.relationships.RelationshipsRelationshipResponse
import work.socialhub.ksaypip.api.response.relationships.RelationshipsSetLabelResponse
import kotlin.js.JsExport

@JsExport
interface RelationshipsResource {

    /**
     * The viewer's own relationships, paged; most recently active first.
     */
    suspend fun list(request: RelationshipsListRequest): Response<RelationshipsListResponse>

    @JsExport.Ignore
    fun listBlocking(request: RelationshipsListRequest): Response<RelationshipsListResponse>

    /**
     * A relationship page: label, note, mark, history and conversations.
     */
    suspend fun relationship(request: RelationshipsRelationshipRequest): Response<RelationshipsRelationshipResponse>

    @JsExport.Ignore
    fun relationshipBlocking(request: RelationshipsRelationshipRequest): Response<RelationshipsRelationshipResponse>

    /**
     * Replace the viewer's local label, note and mark. A replacement, not a patch.
     */
    suspend fun setLabel(request: RelationshipsSetLabelRequest): Response<RelationshipsSetLabelResponse>

    @JsExport.Ignore
    fun setLabelBlocking(request: RelationshipsSetLabelRequest): Response<RelationshipsSetLabelResponse>

    /**
     * Relationship termination: everything shared is revoked synchronously.
     */
    suspend fun terminate(request: RelationshipsTerminateRequest): ResponseUnit

    @JsExport.Ignore
    fun terminateBlocking(request: RelationshipsTerminateRequest): ResponseUnit
}
