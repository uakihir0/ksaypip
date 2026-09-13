package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The viewer's own account state, which most screens already ask for.
 */
@JsExport
@Serializable
class Me {

    var createdAt: String = ""

    var profile: Profile? = null

    /** How many of your posts have something new on them. */
    var unreadNotifications: Int = 0

    /** How many of your live conversations have something you have not read. */
    var unreadConversations: Int = 0

    /** How many people are waiting for an answer to a friend request. */
    var incomingFriendRequests: Int = 0

    /** Whether the friends' timeline is offered at all. A boolean and never a count. */
    var hasFriends: Boolean = false

    /** Which post is still asking to be talked to, or null. One ask at a time. */
    var wantsTalkPostId: String? = null

    /** The subjects kept along the top of the timeline, in order, without the `#`. */
    var pinnedSubjects: Array<String> = arrayOf()

    /** Every widget the product has, in the arranged order, with whether the column draws it. */
    var asideWidgets: Array<AsideWidgetItem> = arrayOf()

    /** Whether this account may open the moderation screens. */
    var isAdmin: Boolean = false

    /** Whether this Worker has somewhere to send feedback to. */
    var canSendFeedback: Boolean = false
}
