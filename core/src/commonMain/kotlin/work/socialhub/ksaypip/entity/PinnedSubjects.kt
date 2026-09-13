package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The row of kept subjects, normalized and without the `#`, in the reader's own order.
 */
@JsExport
@Serializable
class PinnedSubjects {

    var items: Array<String> = arrayOf()
}
