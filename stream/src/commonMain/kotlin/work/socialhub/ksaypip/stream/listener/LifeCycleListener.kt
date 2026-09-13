package work.socialhub.ksaypip.stream.listener

interface LifeCycleListener {

    fun onConnect()

    fun onDisconnect()

    fun onError(e: Exception)
}
