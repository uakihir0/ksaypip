package work.socialhub.ksaypip.domain

/**
 * The colours a viewer may give a counterpart's picture.
 *
 * Keys and not colour values: the same key has to be a pale wash on a light theme and a deep one on
 * a dark theme, and only the client's stylesheet knows the difference.
 */
object MarkColor {
    const val ROSE = "rose"
    const val PEACH = "peach"
    const val BUTTER = "butter"
    const val SAGE = "sage"
    const val MINT = "mint"
    const val SKY = "sky"
    const val LILAC = "lilac"
    const val SAND = "sand"

    val ALL = arrayOf(ROSE, PEACH, BUTTER, SAGE, MINT, SKY, LILAC, SAND)
}
