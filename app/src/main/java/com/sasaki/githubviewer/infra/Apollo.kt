package com.sasaki.githubviewer.infra

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.okHttpClient
import com.sasaki.githubviewer.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response

object Apollo {

    fun getApolloClient(): ApolloClient {
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(AuthorizationInterceptor())
            .build()

        return ApolloClient.Builder()
            .serverUrl(Constants.githubServerUrl)
            .okHttpClient(okHttpClient)
            .build()
    }


}

private class AuthorizationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "" ?: "")
            .build()

        return chain.proceed(request)
    }
}
