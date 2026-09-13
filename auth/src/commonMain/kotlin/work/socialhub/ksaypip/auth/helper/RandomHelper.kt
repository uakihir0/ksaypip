package work.socialhub.ksaypip.auth.helper

import kotlin.random.Random

object RandomHelper {

    /**
     * A random lowercase alphanumeric string, for the flow's `state`.
     */
    fun random(len: Int): String {
        val lists = ('a'..'z') + ('0'..'9')
        return (1..len)
            .map { lists.random() }
            .joinToString("")
    }
}
