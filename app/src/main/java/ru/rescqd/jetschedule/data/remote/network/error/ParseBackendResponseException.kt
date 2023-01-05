package ru.rescqd.jetschedule.data.remote.network.error

class ParseBackendResponseException(e: Exception) : Throwable() {
    override val message = e.message
}

