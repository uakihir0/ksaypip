package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One word a reader is hiding, and the state of its window.
 *
 * Unlike a person mute, a spent row stays on the list: `endsAt` in the past with [active] false
 * is a word the reader can give a new window without typing it again.
 */
@JsExport
@Serializable
class WordMute {

    var id: String = ""

    var word: String = ""

    /** Null means until taken back; a past value is a spent word still on the list. */
    var endsAt: String? = null

    /** Whether it is hiding anything right now. The server's answer, computed against its clock. */
    var active: Boolean = false

    var createdAt: String = ""
}
