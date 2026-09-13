package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * One widget in the right-hand column, and whether the column draws it.
 *
 * A hidden widget is still in the arrangement, at its position, so that showing it again puts it
 * back where the reader had it.
 */
@JsExport
@Serializable
class AsideWidgetItem {

    var widget: String = ""

    var visible: Boolean = true
}
