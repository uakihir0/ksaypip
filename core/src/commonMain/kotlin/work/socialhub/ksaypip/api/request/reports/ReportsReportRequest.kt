package work.socialhub.ksaypip.api.request.reports

import kotlin.js.JsExport

/**
 * A report, optionally with evidence.
 *
 * [targetId] is a post or reply ID, or — for `account` — the identity token the caller holds.
 * The server resolves the subject from the evidence, so no account is ever named by the request
 * or the answer.
 */
@JsExport
class ReportsReportRequest {
    var targetType: String? = null
    var targetId: String? = null
    var reason: String? = null
    var alsoBlock: Boolean? = null
    var idempotencyKey: String? = null
}
