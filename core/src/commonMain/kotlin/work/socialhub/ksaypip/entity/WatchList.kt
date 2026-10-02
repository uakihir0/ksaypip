package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The whole of what the viewer is keeping, newest first.
 *
 * Read whole rather than paged: the list is at most `MAX_WATCHED_ACCOUNTS` long.
 */
@JsExport
@Serializable
class WatchList {

    var items: Array<Watch> = arrayOf()
}
