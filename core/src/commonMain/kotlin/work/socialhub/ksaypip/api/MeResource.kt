package work.socialhub.ksaypip.api

import work.socialhub.ksaypip.api.request.me.MeArrangeAsideWidgetsRequest
import work.socialhub.ksaypip.api.request.me.MeMeRequest
import work.socialhub.ksaypip.api.request.me.MePinSubjectRequest
import work.socialhub.ksaypip.api.request.me.MePostsRequest
import work.socialhub.ksaypip.api.request.me.MeReorderPinnedSubjectsRequest
import work.socialhub.ksaypip.api.request.me.MeUnpinSubjectRequest
import work.socialhub.ksaypip.api.request.me.MeUpdateProfileRequest
import work.socialhub.ksaypip.api.response.Response
import work.socialhub.ksaypip.api.response.me.MeArrangeAsideWidgetsResponse
import work.socialhub.ksaypip.api.response.me.MeMeResponse
import work.socialhub.ksaypip.api.response.me.MePinSubjectResponse
import work.socialhub.ksaypip.api.response.me.MePostsResponse
import work.socialhub.ksaypip.api.response.me.MeReorderPinnedSubjectsResponse
import work.socialhub.ksaypip.api.response.me.MeUnpinSubjectResponse
import work.socialhub.ksaypip.api.response.me.MeUpdateProfileResponse
import kotlin.js.JsExport

@JsExport
interface MeResource {

    /**
     * The caller's own account state.
     */
    suspend fun me(request: MeMeRequest): Response<MeMeResponse>

    @JsExport.Ignore
    fun meBlocking(request: MeMeRequest): Response<MeMeResponse>

    /**
     * The viewer's own posts, paged. The only post list without a window over it.
     */
    suspend fun posts(request: MePostsRequest): Response<MePostsResponse>

    @JsExport.Ignore
    fun postsBlocking(request: MePostsRequest): Response<MePostsResponse>

    /**
     * Own display name, bio, avatar and banner.
     */
    suspend fun updateProfile(request: MeUpdateProfileRequest): Response<MeUpdateProfileResponse>

    @JsExport.Ignore
    fun updateProfileBlocking(request: MeUpdateProfileRequest): Response<MeUpdateProfileResponse>

    /**
     * Keep one subject in the row over the timeline. Answers with the whole row.
     */
    suspend fun pinSubject(request: MePinSubjectRequest): Response<MePinSubjectResponse>

    @JsExport.Ignore
    fun pinSubjectBlocking(request: MePinSubjectRequest): Response<MePinSubjectResponse>

    /**
     * Let one subject go. Answers with the whole row.
     */
    suspend fun unpinSubject(request: MeUnpinSubjectRequest): Response<MeUnpinSubjectResponse>

    @JsExport.Ignore
    fun unpinSubjectBlocking(request: MeUnpinSubjectRequest): Response<MeUnpinSubjectResponse>

    /**
     * The whole order of kept subjects, sent as one rearrangement.
     */
    suspend fun reorderPinnedSubjects(request: MeReorderPinnedSubjectsRequest): Response<MeReorderPinnedSubjectsResponse>

    @JsExport.Ignore
    fun reorderPinnedSubjectsBlocking(
        request: MeReorderPinnedSubjectsRequest,
    ): Response<MeReorderPinnedSubjectsResponse>

    /**
     * The whole arrangement of the right-hand column, sent as one list.
     */
    suspend fun arrangeAsideWidgets(request: MeArrangeAsideWidgetsRequest): Response<MeArrangeAsideWidgetsResponse>

    @JsExport.Ignore
    fun arrangeAsideWidgetsBlocking(request: MeArrangeAsideWidgetsRequest): Response<MeArrangeAsideWidgetsResponse>
}
