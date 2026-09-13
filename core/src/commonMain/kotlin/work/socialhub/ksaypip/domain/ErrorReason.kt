package work.socialhub.ksaypip.domain

/**
 * Machine-readable reasons, attached only to `conflict` and `precondition_failed`.
 *
 * Never attached to `not_found`, which must stay uniform across revoked, blocked, expired and
 * never-existed.
 */
object ErrorReason {
    const val CONVERSATION_THRESHOLD_NOT_MET = "conversation_threshold_not_met"
    const val FRIEND_REQUEST_ALREADY_PENDING = "friend_request_already_pending"
    const val ALREADY_FRIENDS = "already_friends"
    const val NOT_A_PARTICIPANT = "not_a_participant"
    const val CANNOT_TARGET_SELF = "cannot_target_self"
    const val CONVERSATION_ALREADY_STARTED = "conversation_already_started"
    const val RELATIONSHIP_NOT_ACTIVE = "relationship_not_active"
    const val MEDIA_TYPE_NOT_ALLOWED = "media_type_not_allowed"
    const val MEDIA_TOO_LARGE = "media_too_large"
    const val MEDIA_NOT_DECODABLE = "media_not_decodable"
    const val TOO_MANY_MEDIA = "too_many_media"
    const val TOO_MANY_REACTIONS = "too_many_reactions"
    const val MEDIA_ALREADY_ATTACHED = "media_already_attached"
    const val PROFILE_REQUIRED = "profile_required"
    const val TURNSTILE_FAILED = "turnstile_failed"
    const val REPORT_HAS_NO_WRITING = "report_has_no_writing"
    const val REPORT_NAMES_NO_ACCOUNT = "report_names_no_account"
    const val ACCOUNT_ALREADY_DELETED = "account_already_deleted"
    const val TOO_MANY_PINNED_SUBJECTS = "too_many_pinned_subjects"
    const val PINNED_SUBJECTS_CHANGED = "pinned_subjects_changed"
    const val TOO_MANY_WORD_MUTES = "too_many_word_mutes"
    const val WANTS_TALK_ALREADY_OPEN = "wants_talk_already_open"

    val ALL = arrayOf(
        CONVERSATION_THRESHOLD_NOT_MET,
        FRIEND_REQUEST_ALREADY_PENDING,
        ALREADY_FRIENDS,
        NOT_A_PARTICIPANT,
        CANNOT_TARGET_SELF,
        CONVERSATION_ALREADY_STARTED,
        RELATIONSHIP_NOT_ACTIVE,
        MEDIA_TYPE_NOT_ALLOWED,
        MEDIA_TOO_LARGE,
        MEDIA_NOT_DECODABLE,
        TOO_MANY_MEDIA,
        TOO_MANY_REACTIONS,
        MEDIA_ALREADY_ATTACHED,
        PROFILE_REQUIRED,
        TURNSTILE_FAILED,
        REPORT_HAS_NO_WRITING,
        REPORT_NAMES_NO_ACCOUNT,
        ACCOUNT_ALREADY_DELETED,
        TOO_MANY_PINNED_SUBJECTS,
        PINNED_SUBJECTS_CHANGED,
        TOO_MANY_WORD_MUTES,
        WANTS_TALK_ALREADY_OPEN,
    )
}
