package work.socialhub.ksaypip

/**
 * Thrown for every non-2xx answer from the API and for transport failures.
 *
 * [code] and [reason] are parsed from the error envelope (`{"error":{"code":…}}`) where the body
 * carries one, so a caller can branch on the code rather than on the status alone.
 */
class SaypipException : Exception {

    /** HTTP status code, or null for a transport-level failure. */
    var status: Int? = null

    /** Raw response body, or null for a transport-level failure. */
    var body: String? = null

    /** Parsed `error.code`, where the body was one of this API's error envelopes. */
    var code: String? = null

    /** Parsed `error.reason`, present only on `conflict` and `precondition_failed`. */
    var reason: String? = null

    constructor(m: String) : super(m)
    constructor(e: Exception) : super(e)

    constructor(
        status: Int,
        body: String,
    ) : super("status code: $status, body: $body") {
        this.status = status
        this.body = body
    }
}
