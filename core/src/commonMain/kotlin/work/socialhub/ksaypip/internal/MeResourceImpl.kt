package work.socialhub.ksaypip.internal

import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.add
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import work.socialhub.khttpclient.HttpRequest
import work.socialhub.ksaypip.api.MeResource
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
import work.socialhub.ksaypip.internal.InternalUtility.urlEncode
import work.socialhub.ksaypip.util.Headers.AUTHORIZATION
import work.socialhub.ksaypip.util.MediaType
import work.socialhub.ksaypip.util.toBlocking

class MeResourceImpl(
    uri: String,
    accessToken: String,
) : AbstractAuthResourceImpl(uri, accessToken),
    MeResource {

    override suspend fun me(request: MeMeRequest): Response<MeMeResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/me")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .get()
        }
    }

    override fun meBlocking(request: MeMeRequest): Response<MeMeResponse> {
        return toBlocking { me(request) }
    }

    override suspend fun posts(request: MePostsRequest): Response<MePostsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/me/posts")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .pagination(request.cursor, request.limit)
                .get()
        }
    }

    override fun postsBlocking(request: MePostsRequest): Response<MePostsResponse> {
        return toBlocking { posts(request) }
    }

    override suspend fun updateProfile(request: MeUpdateProfileRequest): Response<MeUpdateProfileResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/me/profile")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .jsonBody {
                    profileField("displayName", request.displayName, request.clearDisplayName)
                    profileField("bio", request.bio, request.clearBio)
                    profileField("avatarMediaId", request.avatarMediaId, request.clearAvatarMediaId)
                    profileField("bannerMediaId", request.bannerMediaId, request.clearBannerMediaId)
                }
                .put()
        }
    }

    private fun kotlinx.serialization.json.JsonObjectBuilder.profileField(
        key: String,
        value: String?,
        clear: Boolean,
    ) {
        when {
            value != null -> put(key, value)
            clear -> put(key, JsonNull)
        }
    }

    override fun updateProfileBlocking(request: MeUpdateProfileRequest): Response<MeUpdateProfileResponse> {
        return toBlocking { updateProfile(request) }
    }

    override suspend fun pinSubject(request: MePinSubjectRequest): Response<MePinSubjectResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/pinned-subjects/${urlEncode(request.tag.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .put()
        }
    }

    override fun pinSubjectBlocking(request: MePinSubjectRequest): Response<MePinSubjectResponse> {
        return toBlocking { pinSubject(request) }
    }

    override suspend fun unpinSubject(request: MeUnpinSubjectRequest): Response<MeUnpinSubjectResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/pinned-subjects/${urlEncode(request.tag.orEmpty())}")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .delete()
        }
    }

    override fun unpinSubjectBlocking(request: MeUnpinSubjectRequest): Response<MeUnpinSubjectResponse> {
        return toBlocking { unpinSubject(request) }
    }

    override suspend fun reorderPinnedSubjects(
        request: MeReorderPinnedSubjectsRequest,
    ): Response<MeReorderPinnedSubjectsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/pinned-subjects")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .jsonBody {
                    put(
                        "items",
                        buildJsonArray {
                            request.items?.forEach { add(it) }
                        },
                    )
                }
                .put()
        }
    }

    override fun reorderPinnedSubjectsBlocking(
        request: MeReorderPinnedSubjectsRequest,
    ): Response<MeReorderPinnedSubjectsResponse> {
        return toBlocking { reorderPinnedSubjects(request) }
    }

    override suspend fun arrangeAsideWidgets(
        request: MeArrangeAsideWidgetsRequest,
    ): Response<MeArrangeAsideWidgetsResponse> {
        return proceed {
            HttpRequest()
                .url("${uri}/api/me/aside-widgets")
                .header(AUTHORIZATION, bearerToken())
                .accept(MediaType.JSON)
                .jsonBody {
                    put(
                        "items",
                        buildJsonArray {
                            request.items?.forEach { item ->
                                add(
                                    buildJsonObject {
                                        put("widget", item.widget)
                                        put("visible", item.visible)
                                    },
                                )
                            }
                        },
                    )
                }
                .put()
        }
    }

    override fun arrangeAsideWidgetsBlocking(
        request: MeArrangeAsideWidgetsRequest,
    ): Response<MeArrangeAsideWidgetsResponse> {
        return toBlocking { arrangeAsideWidgets(request) }
    }
}
