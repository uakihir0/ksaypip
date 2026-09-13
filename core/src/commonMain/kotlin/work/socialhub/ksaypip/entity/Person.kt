package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A person, as one viewer sees them.
 *
 * There is no user object with a stable public ID in this API. [identity] is valid only for the
 * requesting viewer; two viewers get different tokens for the same person, and the token dies
 * with the relationship. [profile] is non-null only while a friendship is active.
 */
@JsExport
@Serializable
class Person {

    var identity: String = ""

    /** The viewer's own label. Never server-assigned. */
    var label: String? = null

    var mark: Mark = Mark()

    /** Non-null only while a friendship is active. */
    var profile: Profile? = null
}
