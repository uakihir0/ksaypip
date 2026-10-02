package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport
import work.socialhub.ksaypip.domain.MarkColor

/**
 * The picture one viewer gave one person, for the screens that have no real one to show.
 *
 * Both halves are the viewer's own choice and both are usually null. `colors` is the gradient's
 * two ends — top left first, then bottom right — and is either two keys from [MarkColor] or null.
 * A client that finds it null draws a colour of its own from the identity token.
 */
@JsExport
@Serializable
class Mark {

    var emoji: String? = null

    /** The gradient's two ends, top left then bottom right: palette keys, never colour values. */
    var colors: Array<String>? = null
}
