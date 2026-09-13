package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One person's page, as this viewer sees it: the person, a page of their posts, and the
 * relationship the viewer holds with them where there is one.
 */
@JsExport
@Serializable
class UserPage {

    var person: Person = Person()

    var posts: Array<Post> = arrayOf()

    var postsNextCursor: String? = null

    var relationship: Relationship? = null
}
