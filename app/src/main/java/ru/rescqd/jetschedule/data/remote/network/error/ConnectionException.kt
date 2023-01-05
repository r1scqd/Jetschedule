package ru.rescqd.jetschedule.data.remote.network.error

import java.io.IOException

class ConnectionException(e: IOException) : Throwable() {
    override val message: String? = e.message
}