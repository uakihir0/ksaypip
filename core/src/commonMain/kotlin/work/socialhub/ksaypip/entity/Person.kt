package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * A person, as one viewer sees them — or as everybody sees them, when they are identified.
 *
 * There is no user object with a stable public ID in this API. [identity] is valid only for the
 * requesting viewer; two viewers get different tokens for the same person, and the token dies
 * with the relationship. [identified] is the one exception, and it is the same for every viewer:
 * it appears where the writing is identified, and then [identity] is null. A response never
 * carries both fields for one account. [profile] is non-null only while a friendship is active.
 */
@JsExport
@Serializable
class Person {

    /** Null on an identified person: the public name is what everybody sees. */
    var identity: String? = null

    /** The viewer's own label. Never server-assigned. */
    var label: String? = null

    var mark: Mark = Mark()

    /** Non-null only while a friendship is active. */
    var profile: Profile? = null

    /** Non-null only on the public persona; then [identity] is null. */
    var identified: IdentifiedPerson? = null
}
