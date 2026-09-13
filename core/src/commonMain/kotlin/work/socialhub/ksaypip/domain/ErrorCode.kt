package work.socialhub.ksaypip.domain

/**
 * The closed error-code list this API answers with.
 *
 * The load-bearing rule is that anything the viewer may not see is `not_found`: revoked,
 * blocked, out-of-window and never-existed all produce the identical response.
 */
object ErrorCode {
    const val NOT_FOUND = "not_found"
    const val UNAUTHENTICATED = "unauthenticated"
    const val INSUFFICIENT_SCOPE = "insufficient_scope"
    const val VALIDATION_ERROR = "validation_error"
    const val CONFLICT = "conflict"
    const val PRECONDITION_FAILED = "precondition_failed"
    const val RATE_LIMITED = "rate_limited"
    const val PAYLOAD_TOO_LARGE = "payload_too_large"
    const val INTERNAL_ERROR = "internal_error"

    val ALL = arrayOf(
        NOT_FOUND,
        UNAUTHENTICATED,
        INSUFFICIENT_SCOPE,
        VALIDATION_ERROR,
        CONFLICT,
        PRECONDITION_FAILED,
        RATE_LIMITED,
        PAYLOAD_TOO_LARGE,
        INTERNAL_ERROR,
    )
}
