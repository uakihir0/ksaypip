package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import work.socialhub.ksaypip.domain.MarkColor

/**
 * The picture one viewer gave one person, for the screens that have no real one to show.
 *
 * Both halves are the viewer's own choice and both are usually null; `color` is always a key from
 * [MarkColor], never a colour value.
 */
@JsExport
@Serializable
class Mark {

    var emoji: String? = null

    var color: String? = null
}
