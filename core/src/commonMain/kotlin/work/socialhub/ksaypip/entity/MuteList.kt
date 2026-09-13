package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The people this reader is hiding, newest mute first.
 */
@JsExport
@Serializable
class MuteList {

    var items: Array<Mute> = arrayOf()

    var nextCursor: String? = null
}
