package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One person's page, as this viewer sees it: the person, a page of their posts, and the
 * relationship the viewer holds with them where there is one.
 *
 * [watching] is about the anonymous persona and never says whether its public face is watched.
 * [note] is the caller's own memo, here because this page is where it is written now.
 */
@JsExport
@Serializable
class UserPage {

    var person: Person = Person()

    var posts: Array<Post> = arrayOf()

    var postsNextCursor: String? = null

    var relationship: Relationship? = null

    /** Whether the caller keeps this account's anonymous writing in their connections timeline. */
    var watching: Boolean = false

    /** The caller's own memo about this person, or null. */
    var note: String? = null
}
