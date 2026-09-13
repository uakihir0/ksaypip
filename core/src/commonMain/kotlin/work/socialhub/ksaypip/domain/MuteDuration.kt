package work.socialhub.ksaypip.domain

/**
 * How long a mute lasts: the closed set the product offers.
 *
 * The server turns the key into an end timestamp; the wire carries the decision, not the
 * arithmetic. `forever` is the one duration that must be lifted by hand.
 */
object MuteDuration {
    const val HOUR_1 = "1h"
    const val HOURS_24 = "24h"
    const val DAYS_7 = "7d"
    const val DAYS_30 = "30d"
    const val FOREVER = "forever"

    val ALL = arrayOf(HOUR_1, HOURS_24, DAYS_7, DAYS_30, FOREVER)
}
