package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * What `POST /api/reports` answers with. The server resolves the subject from the evidence, so
 * the report's own ID is the only handle a moderator action takes.
 */
@JsExport
@Serializable
class CreatedReport {

    var id: String = ""
}
