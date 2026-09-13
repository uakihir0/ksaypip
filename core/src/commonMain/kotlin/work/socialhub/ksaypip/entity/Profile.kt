package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A profile, which arrives only where it has been disclosed.
 *
 * The two pictures are URLs rather than media IDs: an avatar takes the thumbnail, a banner the
 * full-size variant, and both are checked for visibility on every fetch.
 */
@JsExport
@Serializable
class Profile {

    var displayName: String? = null

    var bio: String? = null

    var avatarUrl: String? = null

    var bannerUrl: String? = null
}
