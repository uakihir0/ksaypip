package work.socialhub.ksaypip.entity

import kotlinx.serialization.Serializable
import kotlin.js.JsExport

/**
 * The public face of an identified persona, the same for every reader.
 *
 * The one stable public identifier this product has, and it belongs to the writing rather than to
 * the viewer: a post written in the identified mode carries this, and the anonymous persona is not
 * reachable from it. [verified] is always true when the object is present — a revoked persona is
 * no persona — so the field is the badge the client draws. [operator] decides *which* badge: the
 * account behind the persona keeps the deployment, and the client draws 管理者 instead of the seal.
 */
@JsExport
@Serializable
class IdentifiedPerson {

    var handle: String = ""

    /** The profile's own name, which is why the page and the persona show one string. */
    var displayName: String? = null

    var avatarUrl: String? = null

    /** Always true when the object is present. */
    var verified: Boolean = false

    /** Whether the account behind the persona is one of this deployment's operators. */
    var operator: Boolean = false
}
