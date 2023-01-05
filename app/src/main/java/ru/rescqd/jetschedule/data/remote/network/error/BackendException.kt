package ru.rescqd.jetschedule.data.remote.network.error

class BackendException(httpCode: Int, message: String) : Throwable() {
    override val message: String = "$message for httpCode: $httpCode"
}