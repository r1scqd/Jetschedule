package ru.rescqd.jetschedule.data.remote.source

import android.util.Log
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import ru.rescqd.jetschedule.data.remote.network.error.ConnectionException
import ru.rescqd.jetschedule.data.remote.network.error.ParseBackendResponseException
import ru.rescqd.jetschedule.data.remote.network.error.BackendException
import ru.rescqd.jetschedule.data.remote.okhttp.OkHttpConfig
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Base class for all OkHttp sources.
 */
open class BaseOkHttpSource(
    private val config: OkHttpConfig
) {
    val client: OkHttpClient = config.client

    protected val contentType = "application/x-www-form-urlencoded; charset=UTF-8".toMediaType()

    /**
     * Suspending function which wraps OkHttp [Call.enqueue] method for making
     * HTTP requests and wraps external exceptions into subclasses of [AppException].
     *
     * @throws ConnectionException
     * @throws BackendException
     * @throws ParseBackendResponseException
     */
    suspend fun Call.suspendEnqueue(): Response {
        return suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                cancel()
            }
            enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    val appException = ConnectionException(e)
                    Log.e("EXCEPTION", e.message.toString())
                    continuation.resumeWithException(appException)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        // well done
                        continuation.resume(response)
                    } else {
                        handleErrorResponse(response, continuation)
                    }
                }
            })
        }
    }

    /**
     * Concatenate the base URL with a path and query args.
     */
    fun Request.Builder.endpoint(endpoint: String): Request.Builder {
        url("${config.baseUrl}$endpoint")
        return this
    }


    fun  Response.parseJsonResponse(): JSONObject {
        try{
            return JSONObject(this.body.string())
        } catch (e : Exception){
            throw ParseBackendResponseException(e)
        }
    }

    /**
     * 1. Convert error response from the server into [BackendException] and throw the latter.
     * 2. Throw [ParseBackendResponseException] if error response parsing
     * process has been failed.
     */
    private fun handleErrorResponse(response: Response,
                                    continuation: CancellableContinuation<Response>) {
        val httpCode = response.code
        try {
            // parse error body:
            // {
            //   "error": "..."
            // }
            continuation.resumeWithException(BackendException(httpCode, "#TODO")) //TODO
        } catch (e: Exception) {
            // failed to parse error body -> throw parse exception
            val appException = ParseBackendResponseException(e)
            continuation.resumeWithException(appException)
        }
    }
}

