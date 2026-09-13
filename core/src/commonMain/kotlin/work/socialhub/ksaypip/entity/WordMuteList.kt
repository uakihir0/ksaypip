package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The words this reader is hiding, spent ones included.
 */
@JsExport
@Serializable
class WordMuteList {

    var items: Array<WordMute> = arrayOf()

    var nextCursor: String? = null
}
