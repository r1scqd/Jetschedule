package ru.rescqd.jetschedule.data.remote.okhttp

import okhttp3.OkHttpClient

class OkHttpConfig(
    val baseUrl: String,        // prefix for all endpoints
    val client: OkHttpClient,   // for making HTTP requests
)