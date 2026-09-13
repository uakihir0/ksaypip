package work.socialhub.ksaypip.api.request.me

import kotlin.js.JsExport

/**
 * Own display name, bio, avatar and banner.
 *
 * Every field is optional, and each of the four means something different absent than it does
 * null: absent leaves what is stored alone, and a `clear…` flag sends the explicit null that
 * clears it.
 */
@JsExport
class MeUpdateProfileRequest {
    var displayName: String? = null
    var bio: String? = null
    var avatarMediaId: String? = null
    var bannerMediaId: String? = null

    /** Send `displayName: null`, clearing the stored name. */
    var clearDisplayName: Boolean = false

    /** Send `bio: null`, clearing the stored bio. */
    var clearBio: Boolean = false

    /** Send `avatarMediaId: null`, taking the avatar down. */
    var clearAvatarMediaId: Boolean = false

    /** Send `bannerMediaId: null`, taking the banner down. */
    var clearBannerMediaId: Boolean = false
}
