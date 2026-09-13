package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.blocks.BlocksBlockRequest
import work.socialhub.ksaypip.api.response.ResponseUnit
import kotlin.js.JsExport

@JsExport
interface BlocksResource {

    /**
     * Block is forget plus avoid, without telling the blocked person.
     */
    suspend fun block(request: BlocksBlockRequest): ResponseUnit

    @JsExport.Ignore
    fun blockBlocking(request: BlocksBlockRequest): ResponseUnit
}
