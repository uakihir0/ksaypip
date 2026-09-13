package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The whole of a replacement for the viewer's local label, note and mark. An omitted field is a
 * field the caller is saying is no longer there.
 */
@JsExport
@Serializable
class Label {

    var label: String? = null

    var note: String? = null

    var mark: Mark = Mark()
}
