package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A page of posts. Every list here is newest-first, and `nextCursor` asks for older rows further
 * down; null means there is no next page.
 */
@JsExport
@Serializable
class Feed {

    var items: Array<Post> = arrayOf()

    var nextCursor: String? = null
}
