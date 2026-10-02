package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.identified.IdentifiedPageRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.identified.IdentifiedPageResponse
import kotlin.js.JsExport

@JsExport
interface IdentifiedResource {

    /**
     * The public page of an identified persona: its profile, its badge, and its posts at any age.
     * The same for every reader, and readable without a session.
     */
    suspend fun page(request: IdentifiedPageRequest): Response<IdentifiedPageResponse>

    @JsExport.Ignore
    fun pageBlocking(request: IdentifiedPageRequest): Response<IdentifiedPageResponse>
}
