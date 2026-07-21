package com.example.movieexplorer.core.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        // HttpUrl and Request are immutable in OkHttp
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url

        val newUrl: HttpUrl = originalUrl.newBuilder()
            .addQueryParameter("apiKey", apiKey)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        // hands the modified request to the next interceptor
        return chain.proceed(newRequest)
    }
}