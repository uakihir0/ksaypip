package work.socialhub.ksaypip.domain

/**
 * The social providers a deployment may have both halves of a credential for.
 *
 * A code by mail is deliberately not in the list: it is a property of the product rather than of
 * a deployment, so a client may always offer it.
 */
object SignInProvider {
    const val GOOGLE = "google"
    const val APPLE = "apple"

    val ALL = arrayOf(GOOGLE, APPLE)
}
