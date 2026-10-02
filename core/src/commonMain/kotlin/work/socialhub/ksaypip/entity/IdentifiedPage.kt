package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The public page of an identified persona: its profile, its badge, and its posts at any age.
 *
 * Deliberately not a [UserPage] with fields missing. There is no `person` — a viewer-scoped
 * identity has no business on a page that is the same for everybody — and no relationship, which
 * is what keeps the page from becoming a way back to the anonymous persona.
 */
@JsExport
@Serializable
class IdentifiedPage {

    var handle: String = ""

    /** The approved account somewhere else, or null. The only link the page carries. */
    var linkUrl: String? = null

    var profile: Profile = Profile()

    /** Whether the account behind the persona is one of this deployment's operators. */
    var operator: Boolean = false

    /** The persona's identified posts, newest first, at any age. */
    var posts: Array<Post> = arrayOf()

    var postsNextCursor: String? = null

    /** Whether the caller keeps this persona's identified writing in their connections timeline. */
    var watching: Boolean = false
}
